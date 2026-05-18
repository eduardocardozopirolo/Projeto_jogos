package br.com.eduardopirolo.fruitninja3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class faze1 {
    private static final int BOMB_KILL_FRAME_COUNT = 10;
    private static final int BOMB_KILL_EFFECT_COUNT = 4;
    private static final float BOMB_KILL_FRAME_DURATION = 0.05f;
    private static final int SMOKE_EFFECT_COUNT = 8;
    private static final int WHITE_SMOKE = 0;
    private static final int RED_SMOKE = 1;
    private static final float SMOKE_DURATION = 0.35f;
    private final Texture background;
    private final Texture ghostTexture;
    private final Texture ghostRedTexture;
    private final Texture bombTexture;
    private final Texture[] bombKillFrames;
    private final Texture whiteSmokeTexture;
    private final Texture redSmokeTexture;
    private final Texture controllerCursorTexture;
    private final float[] ghostX;
    private final float[] ghostY;
    private final float[] ghostSpeed;
    private final float[] ghostRedX;
    private final float[] ghostRedY;
    private final float[] ghostRedSpeed;
    private final float[] bombX;
    private final float[] bombY;
    private final float[] bombSpeed;
    private final float[] bombKillX;
    private final float[] bombKillY;
    private final float[] bombKillTime;
    private final float[] smokeX;
    private final float[] smokeY;
    private final float[] smokeTime;
    private final int[] smokeType;
    private final float ghostWidth;
    private final float ghostHeight;
    private int score;
    private float controllerX;
    private float controllerY;

    public faze1() {
        background = new Texture("fazes/faze1/fundo/fundo1.png");
        ghostTexture = new Texture("fazes/faze1/personas/fantasma.png");
        ghostRedTexture = new Texture("fazes/faze1/personas/fantasmaVermelho.png");
        bombTexture = new Texture("fazes/faze1/personas/bomba.png");
        bombKillFrames = new Texture[BOMB_KILL_FRAME_COUNT];
        for (int i = 0; i < bombKillFrames.length; i++) {
            bombKillFrames[i] = new Texture("fazes/faze1/personas/bomba_frame" + (i + 1) + ".png");
        }
        whiteSmokeTexture = new Texture("fazes/faze1/personas/fumacaBranca.png");
        redSmokeTexture = new Texture("fazes/faze1/personas/fumacaVermelha.png");
        controllerCursorTexture = createControllerCursorTexture();

        ghostWidth = Gdx.graphics.getWidth() * 0.12f;
        ghostHeight = ghostWidth;
        score = 0;
        controllerX = Gdx.graphics.getWidth() / 2f;
        controllerY = Gdx.graphics.getHeight() / 2f;

        ghostX = new float[4];
        ghostY = new float[4];
        ghostSpeed = new float[4];
        ghostRedX = new float[2];
        ghostRedY = new float[2];
        ghostRedSpeed = new float[2];
        bombX = new float[0];
        bombY = new float[0];
        bombSpeed = new float[0];
        bombKillX = new float[BOMB_KILL_EFFECT_COUNT];
        bombKillY = new float[BOMB_KILL_EFFECT_COUNT];
        bombKillTime = new float[BOMB_KILL_EFFECT_COUNT];
        for (int i = 0; i < bombKillTime.length; i++) {
            bombKillTime[i] = -1f;
        }
        smokeX = new float[SMOKE_EFFECT_COUNT];
        smokeY = new float[SMOKE_EFFECT_COUNT];
        smokeTime = new float[SMOKE_EFFECT_COUNT];
        smokeType = new int[SMOKE_EFFECT_COUNT];

        float screenWidth = Gdx.graphics.getWidth();
        for (int i = 0; i < ghostX.length; i++) {
            ghostX[i] = MathUtils.random(0, screenWidth - ghostWidth);
            ghostY[i] = MathUtils.random(-Gdx.graphics.getHeight(), -ghostHeight);
            ghostSpeed[i] = MathUtils.random(180f, 280f);
        }

        for (int i = 0; i < ghostRedX.length; i++) {
            ghostRedX[i] = -screenWidth * 2f;
        }
        for (int i = 0; i < bombX.length; i++) {
            bombX[i] = -screenWidth * 2f;
        }

        for (int i = 0; i < ghostRedX.length; i++) {
            ghostRedX[i] = randomSafeX(screenWidth);
            ghostRedY[i] = MathUtils.random(-Gdx.graphics.getHeight() * 3f, -Gdx.graphics.getHeight() * 1.2f);
            ghostRedSpeed[i] = MathUtils.random(90f, 150f);
        }

        for (int i = 0; i < bombX.length; i++) {
            bombX[i] = randomSafeX(screenWidth);
            bombY[i] = MathUtils.random(-Gdx.graphics.getHeight() * 5f, -Gdx.graphics.getHeight() * 2.5f);
            bombSpeed[i] = MathUtils.random(70f, 120f);
        }
    }

    public void render(SpriteBatch batch) {
        render(batch, false, 0f, 0f);
    }

    public void render(SpriteBatch batch, boolean useController, float controllerX, float controllerY) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float delta = Gdx.graphics.getDeltaTime();
        batch.draw(background, 0, 0, screenWidth, screenHeight);
        float currentSpeedMultiplier = getSpeedMultiplier();

        if (useController) {
            this.controllerX = MathUtils.clamp(controllerX, 0f, screenWidth);
            this.controllerY = MathUtils.clamp(controllerY, 0f, screenHeight);
            checkCollisionAt(this.controllerX, this.controllerY, ghostWidth * 0.45f);
        } else {
            checkMouseCollision();
        }

        for (int i = 0; i < ghostX.length; i++) {
            ghostY[i] += ghostSpeed[i] * currentSpeedMultiplier * delta;
            if (ghostY[i] > screenHeight) {
                ghostY[i] = -ghostHeight;
                ghostX[i] = MathUtils.random(0, screenWidth - ghostWidth);
                score = Math.max(0, score - 1);
            }
            batch.draw(ghostTexture, ghostX[i], ghostY[i], ghostWidth, ghostHeight);
        }

        for (int i = 0; i < ghostRedX.length; i++) {
            ghostRedY[i] += ghostRedSpeed[i] * currentSpeedMultiplier * delta * 0.85f;
            if (ghostRedY[i] > screenHeight) {
                ghostRedY[i] = MathUtils.random(-screenHeight * 4f, -screenHeight * 1.5f);
                ghostRedX[i] = randomSafeX(screenWidth);
            }
            batch.draw(ghostRedTexture, ghostRedX[i], ghostRedY[i], ghostWidth, ghostHeight);
        }

        for (int i = 0; i < bombX.length; i++) {
            bombY[i] += bombSpeed[i] * currentSpeedMultiplier * delta * 0.75f;
            if (bombY[i] > screenHeight) {
                bombY[i] = MathUtils.random(-screenHeight * 6f, -screenHeight * 3f);
                bombX[i] = randomSafeX(screenWidth);
            }
            batch.draw(bombTexture, bombX[i], bombY[i], ghostWidth, ghostHeight);
        }

        drawSmokeEffects(batch, delta);
        drawBombKillEffects(batch, delta);
        if (useController) {
            drawControllerCursor(batch);
        }
    }

    public void renderPaused(SpriteBatch batch) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        batch.draw(background, 0, 0, screenWidth, screenHeight);

        for (int i = 0; i < ghostX.length; i++) {
            batch.draw(ghostTexture, ghostX[i], ghostY[i], ghostWidth, ghostHeight);
        }

        for (int i = 0; i < ghostRedX.length; i++) {
            batch.draw(ghostRedTexture, ghostRedX[i], ghostRedY[i], ghostWidth, ghostHeight);
        }

        for (int i = 0; i < bombX.length; i++) {
            batch.draw(bombTexture, bombX[i], bombY[i], ghostWidth, ghostHeight);
        }

        drawSmokeEffects(batch, 0f);
        drawBombKillEffects(batch, 0f);
    }

    private float randomSafeX(float screenWidth) {
        float x;
        int attempts = 0;
        do {
            x = MathUtils.random(0, screenWidth - ghostWidth);
            attempts++;
            if (attempts > 50) {
                return x; // Give up after 50 attempts to avoid infinite loop
            }
        } while (!isSafeX(x));
        return x;
    }

    private void checkMouseCollision() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Convert to world coordinates

        checkCollisionAt(mouseX, mouseY);
    }

    private void checkCollisionAt(float mouseX, float mouseY) {
        checkCollisionAt(mouseX, mouseY, 0f);
    }

    private void checkCollisionAt(float mouseX, float mouseY, float hitPadding) {
        // Check collision with normal ghosts (+1 point)
        for (int i = 0; i < ghostX.length; i++) {
            if (mouseX >= ghostX[i] - hitPadding && mouseX <= ghostX[i] + ghostWidth + hitPadding &&
                mouseY >= ghostY[i] - hitPadding && mouseY <= ghostY[i] + ghostHeight + hitPadding) {
                resetGhost(i);
            }
        }

        // Check collision with red ghosts (-5 points)
        for (int i = 0; i < ghostRedX.length; i++) {
            if (mouseX >= ghostRedX[i] - hitPadding && mouseX <= ghostRedX[i] + ghostWidth + hitPadding &&
                mouseY >= ghostRedY[i] - hitPadding && mouseY <= ghostRedY[i] + ghostHeight + hitPadding) {
                resetGhostRed(i);
            }
        }

        // Check collision with bombs (-12 points)
        for (int i = 0; i < bombX.length; i++) {
            if (mouseX >= bombX[i] - hitPadding && mouseX <= bombX[i] + ghostWidth + hitPadding &&
                mouseY >= bombY[i] - hitPadding && mouseY <= bombY[i] + ghostHeight + hitPadding) {
                resetBomb(i);
            }
        }
    }

    private void drawControllerCursor(SpriteBatch batch) {
        float size = ghostWidth * 0.42f;
        float half = size / 2f;
        batch.draw(controllerCursorTexture, controllerX - half, controllerY - half, size, size);
    }

    private Texture createControllerCursorTexture() {
        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 0f, 0f);
        pixmap.fill();

        pixmap.setColor(0f, 0f, 0f, 0.75f);
        pixmap.drawCircle(32, 32, 18);
        pixmap.drawLine(32, 6, 32, 24);
        pixmap.drawLine(32, 40, 32, 58);
        pixmap.drawLine(6, 32, 24, 32);
        pixmap.drawLine(40, 32, 58, 32);

        pixmap.setColor(0f, 0.95f, 1f, 1f);
        pixmap.drawCircle(32, 32, 16);
        pixmap.drawCircle(32, 32, 17);
        pixmap.drawLine(32, 8, 32, 24);
        pixmap.drawLine(32, 40, 32, 56);
        pixmap.drawLine(8, 32, 24, 32);
        pixmap.drawLine(40, 32, 56, 32);

        pixmap.setColor(1f, 1f, 1f, 1f);
        pixmap.fillCircle(32, 32, 3);

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private void resetGhost(int index) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        spawnSmoke(ghostX[index], ghostY[index], WHITE_SMOKE);
        ghostY[index] = -ghostHeight;
        ghostX[index] = MathUtils.random(0, screenWidth - ghostWidth);
        score++; // Increment score when ghost is killed
    }

    private void resetGhostRed(int index) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        spawnSmoke(ghostRedX[index], ghostRedY[index], RED_SMOKE);
        ghostRedY[index] = MathUtils.random(-screenHeight * 4f, -screenHeight * 1.5f);
        ghostRedX[index] = randomSafeX(screenWidth);
        score = Math.max(0, score - 5); // Lose 5 points, minimum 0
    }

    private void resetBomb(int index) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        spawnBombKill(bombX[index], bombY[index]);
        bombY[index] = MathUtils.random(-screenHeight * 6f, -screenHeight * 3f);
        bombX[index] = randomSafeX(screenWidth);
        score = Math.max(0, score - 12); // Lose 12 points, minimum 0
    }

    private void spawnBombKill(float x, float y) {
        int slot = 0;
        float highestTime = bombKillTime[0];
        for (int i = 0; i < bombKillTime.length; i++) {
            if (bombKillTime[i] < 0f) {
                slot = i;
                break;
            }
            if (bombKillTime[i] > highestTime) {
                highestTime = bombKillTime[i];
                slot = i;
            }
        }

        bombKillX[slot] = x;
        bombKillY[slot] = y;
        bombKillTime[slot] = 0f;
    }

    private void spawnSmoke(float x, float y, int type) {
        int slot = 0;
        float lowestTime = smokeTime[0];
        for (int i = 0; i < smokeTime.length; i++) {
            if (smokeTime[i] <= 0f) {
                slot = i;
                break;
            }
            if (smokeTime[i] < lowestTime) {
                lowestTime = smokeTime[i];
                slot = i;
            }
        }

        smokeX[slot] = x;
        smokeY[slot] = y;
        smokeType[slot] = type;
        smokeTime[slot] = SMOKE_DURATION;
    }

    private void drawSmokeEffects(SpriteBatch batch, float delta) {
        for (int i = 0; i < smokeTime.length; i++) {
            if (smokeTime[i] <= 0f) {
                continue;
            }

            float progress = 1f - (smokeTime[i] / SMOKE_DURATION);
            float smokeSize = ghostWidth * (1.1f + progress * 0.25f);
            float smokeOffset = (smokeSize - ghostWidth) / 2f;
            Texture smokeTexture = smokeType[i] == RED_SMOKE ? redSmokeTexture : whiteSmokeTexture;
            batch.draw(smokeTexture, smokeX[i] - smokeOffset, smokeY[i] - smokeOffset, smokeSize, smokeSize);
            smokeTime[i] -= delta;
        }
    }

    private void drawBombKillEffects(SpriteBatch batch, float delta) {
        for (int i = 0; i < bombKillTime.length; i++) {
            if (bombKillTime[i] < 0f) {
                continue;
            }

            int frameIndex = (int)(bombKillTime[i] / BOMB_KILL_FRAME_DURATION);
            if (frameIndex >= bombKillFrames.length) {
                bombKillTime[i] = -1f;
                continue;
            }

            float effectSize = ghostWidth * 1.6f;
            float effectOffset = (effectSize - ghostWidth) / 2f;
            batch.draw(bombKillFrames[frameIndex], bombKillX[i] - effectOffset, bombKillY[i] - effectOffset, effectSize, effectSize);
            bombKillTime[i] += delta;
        }
    }

    private boolean isSafeX(float x) {
        for (float otherX : ghostX) {
            if (Math.abs(otherX - x) < ghostWidth * 1.2f) {
                return false;
            }
        }
        for (float otherX : ghostRedX) {
            if (Math.abs(otherX - x) < ghostWidth * 1.2f) {
                return false;
            }
        }
        for (float otherX : bombX) {
            if (Math.abs(otherX - x) < ghostWidth * 1.2f) {
                return false;
            }
        }
        return true;
    }

    public int getScore() {
        return score;
    }

    private float getSpeedMultiplier() {
        // Increase speed every 15 points
        return 1.0f + (score / 15) * 0.2f; // Base speed 1.0, +0.2 every 15 points
    }

    public void dispose() {
        background.dispose();
        ghostTexture.dispose();
        ghostRedTexture.dispose();
        bombTexture.dispose();
        for (Texture bombKillFrame : bombKillFrames) {
            bombKillFrame.dispose();
        }
        whiteSmokeTexture.dispose();
        redSmokeTexture.dispose();
        controllerCursorTexture.dispose();
    }
}
