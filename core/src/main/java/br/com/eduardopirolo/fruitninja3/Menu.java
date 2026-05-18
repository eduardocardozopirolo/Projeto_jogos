package br.com.eduardopirolo.fruitninja3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Menu {
    private final Texture backgroundTexture;
    private final Texture logoTexture;
    private final Texture startTexture;
    private final Texture startHoverTexture;
    private final Texture exitTexture;
    private float startX;
    private float startY;
    private float startWidth;
    private float startHeight;
    private float exitX;
    private float exitY;
    private float exitWidth;
    private float exitHeight;

    public Menu() {
        backgroundTexture = new Texture("menu/background.png");
        logoTexture = new Texture("logo/logo.png");
        startTexture = new Texture("buttons/start.png");
        startHoverTexture = new Texture("buttons/start_hover.png");
        exitTexture = new Texture("buttons/exit.png");
        updateButtonBounds();
    }

    public void render(SpriteBatch batch) {
        updateButtonBounds();

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        batch.draw(backgroundTexture, 0, 0, screenWidth, screenHeight);
        drawLogo(batch, screenWidth, screenHeight);

        Texture currentStartTexture = isInsideButton(Gdx.input.getX(), Gdx.input.getY(), startX, startY, startWidth, startHeight)
            ? startHoverTexture
            : startTexture;
        batch.draw(currentStartTexture, startX, startY, startWidth, startHeight);
        batch.draw(exitTexture, exitX, exitY, exitWidth, exitHeight);
    }

    public boolean isStartButtonClicked(float mouseX, float mouseY) {
        return isInsideButton(mouseX, mouseY, startX, startY, startWidth, startHeight);
    }

    public boolean isExitButtonClicked(float mouseX, float mouseY) {
        return isInsideButton(mouseX, mouseY, exitX, exitY, exitWidth, exitHeight);
    }

    private void updateButtonBounds() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        startWidth = screenWidth * 0.24f;
        startHeight = startWidth * startTexture.getHeight() / startTexture.getWidth();
        exitWidth = startWidth;
        exitHeight = exitWidth * exitTexture.getHeight() / exitTexture.getWidth();

        startX = (screenWidth - startWidth) / 2f;
        startY = screenHeight * 0.33f;
        exitX = (screenWidth - exitWidth) / 2f;
        exitY = startY - exitHeight - 4f;
    }

    private boolean isInsideButton(float mouseX, float mouseY, float x, float y, float width, float height) {
        float worldY = Gdx.graphics.getHeight() - mouseY;
        return mouseX >= x && mouseX <= x + width &&
               worldY >= y && worldY <= y + height;
    }

    private void drawLogo(SpriteBatch batch, float screenWidth, float screenHeight) {
        float maxLogoWidth = screenWidth * 0.32f;
        float logoWidth = Math.min(maxLogoWidth, logoTexture.getWidth());
        float logoHeight = logoWidth * logoTexture.getHeight() / logoTexture.getWidth();
        float logoX = (screenWidth - logoWidth) / 2f;
        float logoY = screenHeight * 0.63f;
        batch.draw(logoTexture, logoX, logoY, logoWidth, logoHeight);
    }

    public void dispose() {
        backgroundTexture.dispose();
        logoTexture.dispose();
        startTexture.dispose();
        startHoverTexture.dispose();
        exitTexture.dispose();
    }
}
