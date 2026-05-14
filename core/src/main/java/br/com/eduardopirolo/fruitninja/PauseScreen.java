package br.com.eduardopirolo.fruitninja;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PauseScreen extends ScreenAdapter {
    private final Main game;
    private Stage stage;
    private Texture backgroundTexture;
    private Texture pauseTexture;
    private Texture pauseHoverTexture;
    private Texture exitTexture;
    private Texture exitHoverTexture;
    private ImageButton pauseButton;
    private ImageButton exitButton;
    private Image background;

    public PauseScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        // Load textures
        backgroundTexture = new Texture("menu/fundoLiso.png");
        pauseTexture = new Texture("buttons/pause.png");
        pauseHoverTexture = new Texture("buttons/pause_houver.png");
        exitTexture = new Texture("buttons/exit.png");
        exitHoverTexture = new Texture("buttons/exit.png");

        // Background
        background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        float buttonWidth = Gdx.graphics.getWidth() * 0.26f;
        float pauseAspect = pauseTexture.getHeight() / (float) pauseTexture.getWidth();
        float buttonHeight = buttonWidth * pauseAspect;
        float exitAspect = exitTexture.getHeight() / (float) exitTexture.getWidth();
        float exitHeight = buttonWidth * exitAspect;

        // Pause Button
        TextureRegionDrawable pauseUp = new TextureRegionDrawable(new TextureRegion(pauseTexture));
        TextureRegionDrawable pauseDown = new TextureRegionDrawable(new TextureRegion(pauseHoverTexture));
        pauseButton = new ImageButton(pauseUp, pauseDown);
        pauseButton.setTransform(true);
        pauseButton.setSize(buttonWidth, buttonHeight);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(Actions.sequence(
                    Actions.fadeOut(0.5f),
                    Actions.run(() -> {
                        GameScreen gameScreen = game.getGameScreen();
                        if (gameScreen != null) {
                            gameScreen.resumeGame();
                            game.setScreen(gameScreen);
                        }
                    })
                ));
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

        // Exit Button
        TextureRegionDrawable exitUp = new TextureRegionDrawable(new TextureRegion(exitTexture));
        TextureRegionDrawable exitDown = new TextureRegionDrawable(new TextureRegion(exitHoverTexture));
        exitButton = new ImageButton(exitUp, exitDown);
        exitButton.setTransform(true);
        exitButton.setSize(buttonWidth, buttonWidth * exitAspect);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Exit to menu
                stage.addAction(Actions.sequence(
                    Actions.fadeOut(0.5f),
                    Actions.run(() -> game.setScreen(new MenuScreen(game)))
                ));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                exitButton.addAction(Actions.scaleTo(1.1f, 1.1f, 0.2f));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                exitButton.addAction(Actions.scaleTo(1.0f, 1.0f, 0.2f));
            }
        });

        // Layout
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(pauseButton).size(buttonWidth, buttonHeight).padBottom(16).row();
        table.add(exitButton).size(buttonWidth, exitHeight);
        stage.addActor(table);

        // Fade in
        stage.addAction(Actions.fadeIn(1.0f));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        pauseTexture.dispose();
        pauseHoverTexture.dispose();
        exitTexture.dispose();
        exitHoverTexture.dispose();
    }
}