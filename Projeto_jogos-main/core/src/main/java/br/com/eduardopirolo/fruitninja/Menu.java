package br.com.eduardopirolo.fruitninja;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.Color;

public class Menu {
    private final Texture logoTexture;
    private final BitmapFont font;
    private final GlyphLayout layout;
    private final String buttonText;
    private final float buttonX;
    private final float buttonY;
    private final float buttonWidth;
    private final float buttonHeight;

    public Menu() {
        logoTexture = new Texture("logo/logo.png");
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(2.0f);
        layout = new GlyphLayout();
        buttonText = "INICIAR";

        // Calculate button position (centered)
        layout.setText(font, buttonText);
        buttonWidth = layout.width;
        buttonHeight = layout.height;
        buttonX = (Gdx.graphics.getWidth() - buttonWidth) / 2;
        buttonY = Gdx.graphics.getHeight() * 0.3f;
    }

    public void render(SpriteBatch batch) {
        // Draw logo centered
        float logoX = (Gdx.graphics.getWidth() - logoTexture.getWidth()) / 2;
        float logoY = Gdx.graphics.getHeight() * 0.6f;
        batch.draw(logoTexture, logoX, logoY);

        // Draw button text
        font.draw(batch, buttonText, buttonX, buttonY);
    }

    public boolean isButtonClicked(float mouseX, float mouseY) {
        // Convert screen coordinates to world coordinates
        mouseY = Gdx.graphics.getHeight() - mouseY;
        return mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
               mouseY >= buttonY - buttonHeight && mouseY <= buttonY;
    }

    public void dispose() {
        logoTexture.dispose();
        font.dispose();
    }
}
