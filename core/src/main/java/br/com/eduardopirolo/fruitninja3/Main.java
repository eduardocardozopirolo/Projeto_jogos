package br.com.eduardopirolo.fruitninja3;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private enum GameState {
        MENU,
        FASE1,
        FASE2,
        PAUSED
    }

    private SpriteBatch batch;
    private Menu menu;
    private faze1 faseUm;
    private faze2 faseDois;
    private GameState currentState;
    private GameState stateBeforePause;
    private BitmapFont scoreFont;
    private Texture pauseTexture;
    private Texture pauseHoverTexture;
    private Texture exitTexture;
    private Texture pauseBackgroundTexture;
    private Texture pausePanelTexture;
    private TextureRegion pauseButtonRegion;
    private TextureRegion pauseHoverButtonRegion;
    private TextureRegion exitButtonRegion;
    private float pauseButtonX;
    private float pauseButtonY;
    private float pauseButtonWidth;
    private float pauseButtonHeight;
    private float resumeButtonX;
    private float resumeButtonY;
    private float resumeButtonWidth;
    private float resumeButtonHeight;
    private float menuButtonX;
    private float menuButtonY;
    private float menuButtonWidth;
    private float menuButtonHeight;
    private boolean returnMenuRequested;
    private MpuSerialReader mpu;
    private boolean mpuConnected;
    private String mpuStatusText;
    private float mpuNeutralAx;
    private float mpuNeutralAy;
    private float calibrationAxSum;
    private float calibrationAySum;
    private float calibrationTime;
    private int calibrationSamples;
    private boolean mpuCalibrated;
    private float smoothedControllerX;
    private float smoothedControllerY;

    private static final String DEFAULT_PORTA_MPU = "COM5";
    private static final float MPU_TILT_TO_EDGE = 0.65f;
    private static final float MPU_CURSOR_SMOOTHING = 0.16f;
    private static final float MPU_TILT_DEADZONE = 0.06f;
    private static final float MPU_CALIBRATION_SECONDS = 1.2f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        menu = new Menu();
        faseUm = new faze1();
        faseDois = null;
        currentState = GameState.MENU;
        stateBeforePause = GameState.FASE1;
        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.WHITE);
        scoreFont.getData().setScale(1.5f);
        pauseTexture = new Texture("buttons/pause.png");
        pauseHoverTexture = new Texture("buttons/pause_houver.png");
        exitTexture = new Texture("buttons/exit.png");
        pauseBackgroundTexture = new Texture("menu/fundoLiso.png");
        pausePanelTexture = new Texture("buttons/fundoMenu.png");
        pauseButtonRegion = new TextureRegion(pauseTexture, 28, 147, 446, 202);
        pauseHoverButtonRegion = new TextureRegion(pauseHoverTexture, 117, 248, 1311, 604);
        exitButtonRegion = new TextureRegion(exitTexture, 63, 111, 490, 183);
        returnMenuRequested = false;
        mpu = new MpuSerialReader();
        mpuStatusText = "MPU: conectando...";
        resetMpuCalibration();
        connectMpu();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();

        if (currentState == GameState.MENU) {
            menu.render(batch);

            // Check for mouse click on menu button
            if (Gdx.input.justTouched()) {
                float mouseX = Gdx.input.getX();
                float mouseY = Gdx.input.getY();
                if (menu.isStartButtonClicked(mouseX, mouseY)) {
                    currentState = GameState.FASE1;
                } else if (menu.isExitButtonClicked(mouseX, mouseY)) {
                    Gdx.app.exit();
                }
            }
        } else if (currentState == GameState.FASE1) {
            MpuControllerState controller = readMpuController();
            if (controller.active) {
                faseUm.render(batch, true, controller.x, controller.y);
            } else {
                faseUm.render(batch);
            }
            int currentScore = faseUm.getScore();
            if (currentScore > 100) {
                faseDois = new faze2(currentScore);
                currentState = GameState.FASE2;
            }

            // Draw score
            String scoreText = "Pontuacao: " + currentScore;
            scoreFont.draw(batch, scoreText, 20, Gdx.graphics.getHeight() - 20);
            drawMpuStatus(batch);
            drawPauseButton(batch);
            handlePauseButtonClick(currentState == GameState.FASE2 ? GameState.FASE2 : GameState.FASE1);
        } else if (currentState == GameState.FASE2) {
            MpuControllerState controller = readMpuController();
            if (controller.active) {
                faseDois.render(batch, true, controller.x, controller.y);
            } else {
                faseDois.render(batch);
            }
            String scoreText = "Pontuacao: " + faseDois.getScore();
            scoreFont.draw(batch, scoreText, 20, Gdx.graphics.getHeight() - 20);
            drawMpuStatus(batch);
            drawPauseButton(batch);
            handlePauseButtonClick(GameState.FASE2);
        } else if (currentState == GameState.PAUSED) {
            renderPausedGame(batch);
            drawPauseMenu(batch);
            handlePauseMenuClick();
        }

        batch.end();

        handlePendingGameReset();
    }

    private void drawPauseButton(SpriteBatch batch) {
        updatePauseButtonBounds();
        TextureRegion currentPauseTexture = isInsideImageButton(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(),
            pauseButtonX, pauseButtonY, pauseButtonWidth, pauseButtonHeight)
            ? pauseHoverButtonRegion
            : pauseButtonRegion;
        batch.draw(currentPauseTexture, pauseButtonX, pauseButtonY, pauseButtonWidth, pauseButtonHeight);
    }

    private void handlePauseButtonClick(GameState currentGameState) {
        if (!Gdx.input.justTouched()) {
            return;
        }

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        if (isInsideImageButton(mouseX, mouseY, pauseButtonX, pauseButtonY, pauseButtonWidth, pauseButtonHeight)) {
            stateBeforePause = currentGameState;
            currentState = GameState.PAUSED;
        }
    }

    private void updatePauseButtonBounds() {
        pauseButtonWidth = Gdx.graphics.getWidth() * 0.12f;
        pauseButtonHeight = pauseButtonWidth * pauseButtonRegion.getRegionHeight() / pauseButtonRegion.getRegionWidth();
        pauseButtonX = Gdx.graphics.getWidth() - pauseButtonWidth - 24f;
        pauseButtonY = Gdx.graphics.getHeight() - pauseButtonHeight - 16f;
    }

    private void renderPausedGame(SpriteBatch batch) {
        if (stateBeforePause == GameState.FASE2 && faseDois != null) {
            faseDois.renderPaused(batch);
            String scoreText = "Pontuacao: " + faseDois.getScore();
            scoreFont.draw(batch, scoreText, 20, Gdx.graphics.getHeight() - 20);
        } else {
            faseUm.renderPaused(batch);
            String scoreText = "Pontuacao: " + faseUm.getScore();
            scoreFont.draw(batch, scoreText, 20, Gdx.graphics.getHeight() - 20);
        }
    }

    private void drawPauseMenu(SpriteBatch batch) {
        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;
        updatePauseMenuBounds(centerX, centerY);

        batch.setColor(1f, 1f, 1f, 0.92f);
        batch.draw(pauseBackgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(1f, 1f, 1f, 1f);

        float panelWidth = Gdx.graphics.getWidth() * 0.45f;
        float panelHeight = panelWidth * pausePanelTexture.getHeight() / pausePanelTexture.getWidth();
        batch.draw(pausePanelTexture, centerX - panelWidth / 2f, centerY - panelHeight / 2f, panelWidth, panelHeight);

        TextureRegion currentResumeTexture = isInsideImageButton(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(),
            resumeButtonX, resumeButtonY, resumeButtonWidth, resumeButtonHeight)
            ? pauseHoverButtonRegion
            : pauseButtonRegion;
        batch.draw(currentResumeTexture, resumeButtonX, resumeButtonY, resumeButtonWidth, resumeButtonHeight);

        batch.draw(exitButtonRegion, menuButtonX, menuButtonY, menuButtonWidth, menuButtonHeight);
    }

    private void handlePauseMenuClick() {
        if (!Gdx.input.justTouched()) {
            return;
        }

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        if (isInsideImageButton(mouseX, mouseY, resumeButtonX, resumeButtonY, resumeButtonWidth, resumeButtonHeight)) {
            currentState = stateBeforePause;
        } else if (isInsideImageButton(mouseX, mouseY, menuButtonX, menuButtonY, menuButtonWidth, menuButtonHeight)) {
            Gdx.app.exit();
        }
    }

    private void updatePauseMenuBounds(float centerX, float centerY) {
        resumeButtonWidth = Gdx.graphics.getWidth() * 0.22f;
        resumeButtonHeight = resumeButtonWidth * pauseButtonRegion.getRegionHeight() / pauseButtonRegion.getRegionWidth();
        resumeButtonX = centerX - resumeButtonWidth / 2f;
        resumeButtonY = centerY + 28f;

        menuButtonWidth = resumeButtonWidth;
        menuButtonHeight = menuButtonWidth * exitButtonRegion.getRegionHeight() / exitButtonRegion.getRegionWidth();
        menuButtonX = centerX - menuButtonWidth / 2f;
        menuButtonY = centerY - menuButtonHeight - 72f;
    }

    private boolean isInsideImageButton(float mouseX, float mouseY, float x, float y, float width, float height) {
        return mouseX >= x &&
               mouseX <= x + width &&
               mouseY >= y &&
               mouseY <= y + height;
    }

    private void restartGame() {
        faseUm.dispose();
        if (faseDois != null) {
            faseDois.dispose();
        }

        faseUm = new faze1();
        faseDois = null;
        resetMpuCalibration();
        stateBeforePause = GameState.FASE1;
        currentState = GameState.FASE1;
    }

    private void handlePendingGameReset() {
        if (returnMenuRequested) {
            returnMenuRequested = false;
            restartGame();
            currentState = GameState.MENU;
        }
    }

    private void connectMpu() {
        String preferredPort = getPreferredMpuPort();
        if (tryConnectMpu(preferredPort)) {
            return;
        }

        String[] ports = MpuSerialReader.listarPortasDisponiveis();
        for (String port : ports) {
            if (!port.equalsIgnoreCase(preferredPort) && tryConnectMpu(port)) {
                return;
            }
        }

        mpuConnected = false;
        mpuStatusText = "MPU: nao conectado. Portas vistas: " + formatPorts(ports);
        System.out.println(mpuStatusText + ". Jogo segue com mouse.");
    }

    private boolean tryConnectMpu(String port) {
        if (port == null || port.trim().isEmpty()) {
            return false;
        }

        boolean connected = mpu.conectar(port.trim());
        if (connected) {
            mpuConnected = true;
            mpuStatusText = "MPU: conectado em " + mpu.getConnectedPortName();
            System.out.println(mpuStatusText);
        }
        return connected;
    }

    private String getPreferredMpuPort() {
        String propertyPort = System.getProperty("mpu.port");
        if (propertyPort != null && !propertyPort.trim().isEmpty()) {
            return propertyPort.trim();
        }

        String envPort = System.getenv("MPU_PORT");
        if (envPort != null && !envPort.trim().isEmpty()) {
            return envPort.trim();
        }

        return DEFAULT_PORTA_MPU;
    }

    private String formatPorts(String[] ports) {
        if (ports.length == 0) {
            return "nenhuma";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ports.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(ports[i]);
        }
        return builder.toString();
    }

    private void drawMpuStatus(SpriteBatch batch) {
        scoreFont.draw(batch, mpuStatusText, 20, Gdx.graphics.getHeight() - 56);
        if (mpuConnected && mpu != null && mpu.isConnected()) {
            scoreFont.draw(batch, "AX " + formatOneDecimal(mpu.ax) + "  AY " + formatOneDecimal(mpu.ay) +
                "  GX " + formatOneDecimal(mpu.gx) + "  GZ " + formatOneDecimal(mpu.gz),
                20, Gdx.graphics.getHeight() - 92);
        }
    }

    private String formatOneDecimal(float value) {
        return String.format(java.util.Locale.US, "%.1f", value);
    }

    private MpuControllerState readMpuController() {
        if (!mpuConnected || mpu == null || !mpu.isConnected()) {
            mpuConnected = false;
            mpuStatusText = "MPU: desconectado. Jogo segue com mouse.";
            return MpuControllerState.inactive();
        }

        updateMpuCalibration();
        if (!mpuCalibrated) {
            return new MpuControllerState(smoothedControllerX, smoothedControllerY, true);
        }

        float halfWidth = Gdx.graphics.getWidth() / 2f;
        float halfHeight = Gdx.graphics.getHeight() / 2f;
        float horizontalTilt = applyTiltDeadzone((mpu.ay - mpuNeutralAy) / MPU_TILT_TO_EDGE);
        float verticalTilt = applyTiltDeadzone((mpu.ax - mpuNeutralAx) / MPU_TILT_TO_EDGE);
        float targetX = halfWidth - (horizontalTilt * halfWidth);
        float targetY = halfHeight - (verticalTilt * halfHeight);
        smoothedControllerX += (targetX - smoothedControllerX) * MPU_CURSOR_SMOOTHING;
        smoothedControllerY += (targetY - smoothedControllerY) * MPU_CURSOR_SMOOTHING;

        return new MpuControllerState(smoothedControllerX, smoothedControllerY, true);
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    private float applyTiltDeadzone(float value) {
        float clampedValue = clamp(value, -1f, 1f);
        float absValue = Math.abs(clampedValue);
        if (absValue < MPU_TILT_DEADZONE) {
            return 0f;
        }

        float normalizedValue = (absValue - MPU_TILT_DEADZONE) / (1f - MPU_TILT_DEADZONE);
        return Math.signum(clampedValue) * normalizedValue;
    }

    private void resetMpuCalibration() {
        mpuNeutralAx = 0f;
        mpuNeutralAy = 0f;
        calibrationAxSum = 0f;
        calibrationAySum = 0f;
        calibrationTime = 0f;
        calibrationSamples = 0;
        mpuCalibrated = false;
        smoothedControllerX = Gdx.graphics.getWidth() / 2f;
        smoothedControllerY = Gdx.graphics.getHeight() / 2f;
    }

    private void updateMpuCalibration() {
        if (mpuCalibrated) {
            return;
        }

        calibrationTime += Gdx.graphics.getDeltaTime();
        calibrationAxSum += mpu.ax;
        calibrationAySum += mpu.ay;
        calibrationSamples++;
        mpuStatusText = "MPU: calibrando... mantenha o pulso no centro";

        if (calibrationTime >= MPU_CALIBRATION_SECONDS && calibrationSamples > 0) {
            mpuNeutralAx = calibrationAxSum / calibrationSamples;
            mpuNeutralAy = calibrationAySum / calibrationSamples;
            mpuCalibrated = true;
            mpuStatusText = "MPU: conectado em " + mpu.getConnectedPortName();
        }
    }

    private static class MpuControllerState {
        final float x;
        final float y;
        final boolean active;

        MpuControllerState(float x, float y, boolean active) {
            this.x = x;
            this.y = y;
            this.active = active;
        }

        static MpuControllerState inactive() {
            return new MpuControllerState(0f, 0f, false);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        menu.dispose();
        faseUm.dispose();
        if (faseDois != null) {
            faseDois.dispose();
        }
        scoreFont.dispose();
        pauseTexture.dispose();
        pauseHoverTexture.dispose();
        exitTexture.dispose();
        pauseBackgroundTexture.dispose();
        pausePanelTexture.dispose();
        if (mpu != null) {
            mpu.desconectar();
        }
    }
}
