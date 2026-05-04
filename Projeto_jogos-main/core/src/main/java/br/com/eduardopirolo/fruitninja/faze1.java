package br.com.eduardopirolo.fruitninja;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class faze1 {
    private final Texture background;
    private final Texture ghostTexture;
    private final Texture ghostRedTexture;
    private final Texture bombTexture;
    private final float[] ghostX;
    private final float[] ghostY;
    private final float[] ghostSpeed;
    private final float[] ghostRedX;
    private final float[] ghostRedY;
    private final float[] ghostRedSpeed;
    private final float[] bombX;
    private final float[] bombY;
    private final float[] bombSpeed;
    private final float ghostWidth;
    private final float ghostHeight;
    private int score;

    public faze1() {
        background = new Texture("fazes/fundo1.png");
        ghostTexture = new Texture("personas/fantasma.png");
        ghostRedTexture = new Texture("personas/fantasmaVermelho.png");
        bombTexture = new Texture("personas/bomba.png");

        ghostWidth = Gdx.graphics.getWidth() * 0.12f;
        ghostHeight = ghostWidth;
        score = 0;

        ghostX = new float[4];
        ghostY = new float[4];
        ghostSpeed = new float[4];
        ghostRedX = new float[2];
        ghostRedY = new float[2];
        ghostRedSpeed = new float[2];
        bombX = new float[2];
        bombY = new float[2];
        bombSpeed = new float[2];

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
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        batch.draw(background, 0, 0, screenWidth, screenHeight);

        float delta = Gdx.graphics.getDeltaTime();
        float currentSpeedMultiplier = getSpeedMultiplier();

        // Check mouse collision for ghosts
        checkMouseCollision();

        for (int i = 0; i < ghostX.length; i++) {
            ghostY[i] += ghostSpeed[i] * currentSpeedMultiplier * delta;
            if (ghostY[i] > screenHeight) {
                ghostY[i] = -ghostHeight;
                ghostX[i] = MathUtils.random(0, screenWidth - ghostWidth);
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

        // Check collision with normal ghosts (+1 point)
        for (int i = 0; i < ghostX.length; i++) {
            if (mouseX >= ghostX[i] && mouseX <= ghostX[i] + ghostWidth &&
                mouseY >= ghostY[i] && mouseY <= ghostY[i] + ghostHeight) {
                resetGhost(i);
            }
        }

        // Check collision with red ghosts (-5 points)
        for (int i = 0; i < ghostRedX.length; i++) {
            if (mouseX >= ghostRedX[i] && mouseX <= ghostRedX[i] + ghostWidth &&
                mouseY >= ghostRedY[i] && mouseY <= ghostRedY[i] + ghostHeight) {
                resetGhostRed(i);
            }
        }

        // Check collision with bombs (-12 points)
        for (int i = 0; i < bombX.length; i++) {
            if (mouseX >= bombX[i] && mouseX <= bombX[i] + ghostWidth &&
                mouseY >= bombY[i] && mouseY <= bombY[i] + ghostHeight) {
                resetBomb(i);
            }
        }
    }

    private void resetGhost(int index) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        ghostY[index] = -ghostHeight;
        ghostX[index] = MathUtils.random(0, screenWidth - ghostWidth);
        score++; // Increment score when ghost is killed
    }

    private void resetGhostRed(int index) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        ghostRedY[index] = MathUtils.random(-screenHeight * 4f, -screenHeight * 1.5f);
        ghostRedX[index] = randomSafeX(screenWidth);
        score = Math.max(0, score - 5); // Lose 5 points, minimum 0
    }

    private void resetBomb(int index) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        bombY[index] = MathUtils.random(-screenHeight * 6f, -screenHeight * 3f);
        bombX[index] = randomSafeX(screenWidth);
        score = Math.max(0, score - 12); // Lose 12 points, minimum 0
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
    }
}
