package br.com.eduardopirolo.fruitninja;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen extends ScreenAdapter {
    private final Main game;
    private SpriteBatch batch;
    private faze1 fase;
    private Stage uiStage;
    private ImageButton pauseButton;
    private Texture pauseTexture;
    private Texture pauseHoverTexture;
    private BitmapFont scoreFont;
    private boolean paused = false;
    private boolean initialized = false;

    public GameScreen(Main game) {
        this.game = game;
        this.game.setGameScreen(this);
    }

    @Override
    public void show() {
        if (!initialized) {
            batch = new SpriteBatch();
            fase = new faze1();
            scoreFont = new BitmapFont();
            scoreFont.setColor(Color.WHITE);
            scoreFont.getData().setScale(1.5f);

            uiStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
            Gdx.input.setInputProcessor(uiStage);

            // Load pause button textures
            pauseTexture = new Texture("buttons/pause.png");
            pauseHoverTexture = new Texture("buttons/pause_houver.png");

            TextureRegionDrawable pauseUp = new TextureRegionDrawable(new TextureRegion(pauseTexture));
            TextureRegionDrawable pauseDown = new TextureRegionDrawable(new TextureRegion(pauseHoverTexture));
            pauseButton = new ImageButton(pauseUp, pauseDown);
            pauseButton.setTransform(true);
            float buttonWidth = Gdx.graphics.getWidth() * 0.08f;
            float pauseAspect = pauseTexture.getHeight() / (float) pauseTexture.getWidth();
            float buttonHeight = buttonWidth * pauseAspect;
            pauseButton.setSize(buttonWidth, buttonHeight);
            pauseButton.setPosition(Gdx.graphics.getWidth() - buttonWidth - 20, Gdx.graphics.getHeight() - buttonHeight - 20);
            pauseButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    paused = true;
                    game.setScreen(new PauseScreen(game));
                }

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    pauseButton.addAction(Actions.scaleTo(1.1f, 1.1f, 0.2f));
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    super.exit(event, x, y, pointer, toActor);
                    pauseButton.addAction(Actions.scaleTo(1.0f, 1.0f, 0.2f));
                }
            });

            uiStage.addActor(pauseButton);
            initialized = true;
        } else {
            Gdx.input.setInputProcessor(uiStage);
        }
    }

    @Override
    public void render(float delta) {
        if (!paused) {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();
            fase.render(batch);
            // Draw score
            String scoreText = "Pontuacao: " + fase.getScore();
            scoreFont.draw(batch, scoreText, 20, Gdx.graphics.getHeight() - 20);
            batch.end();
        }

        uiStage.act(delta);
        uiStage.draw();

        // Check for pause key
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            paused = true;
            game.setScreen(new PauseScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        fase.dispose();
        uiStage.dispose();
        pauseTexture.dispose();
        pauseHoverTexture.dispose();
        scoreFont.dispose();
    }

    public void resumeGame() {
        paused = false;
    }
}