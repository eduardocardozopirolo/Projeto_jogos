package br.com.eduardopirolo.fruitninja;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private enum GameState {
        MENU,
        GAME
    }

    private SpriteBatch batch;
    private Menu menu;
    private faze1 fase;
    private GameState currentState;
    private BitmapFont scoreFont;

    @Override
    public void create() {
        batch = new SpriteBatch();
        menu = new Menu();
        fase = new faze1();
        currentState = GameState.MENU;
        scoreFont = new BitmapFont();
        scoreFont.setColor(Color.WHITE);
        scoreFont.getData().setScale(1.5f);
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
                if (menu.isButtonClicked(mouseX, mouseY)) {
                    currentState = GameState.GAME;
                }
            }
        } else if (currentState == GameState.GAME) {
            fase.render(batch);
            // Draw score
            String scoreText = "Pontuacao: " + fase.getScore();
            scoreFont.draw(batch, scoreText, 20, Gdx.graphics.getHeight() - 20);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        menu.dispose();
        fase.dispose();
        scoreFont.dispose();
    }
}
