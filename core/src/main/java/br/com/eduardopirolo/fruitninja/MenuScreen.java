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

public class MenuScreen extends ScreenAdapter {
    private final Main game;
    private Stage stage;
    private Texture backgroundTexture;
    private Texture startTexture;
    private Texture startHoverTexture;
    private Texture exitTexture;
    private Texture exitHoverTexture;
    private ImageButton startButton;
    private ImageButton exitButton;
    private Image background;

    public MenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        // Load textures
        backgroundTexture = new Texture("menu/background.png");
        startTexture = new Texture("buttons/start.png");
        startHoverTexture = new Texture("buttons/start_hover.png");
        exitTexture = new Texture("buttons/exit.png");
        exitHoverTexture = new Texture("buttons/exit.png"); // Assuming no hover for exit, or create one

        // Background
        background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        float buttonWidth = Gdx.graphics.getWidth() * 0.24f;
        float startAspect = startTexture.getHeight() / (float) startTexture.getWidth();
        final float buttonHeight = buttonWidth * startAspect;

        // Start Button
        TextureRegionDrawable startUp = new TextureRegionDrawable(new TextureRegion(startTexture));
        TextureRegionDrawable startDown = new TextureRegionDrawable(new TextureRegion(startHoverTexture));
        startButton = new ImageButton(startUp, startDown);
        startButton.setTransform(true);
        startButton.setSize(buttonWidth, buttonHeight);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition to game screen
                stage.addAction(Actions.sequence(
                    Actions.fadeOut(0.5f),
                    Actions.run(() -> game.setScreen(new GameScreen(game)))
                ));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                // Glow effect: scale up
                startButton.addAction(Actions.scaleTo(1.1f, 1.1f, 0.2f));
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                // Reset scale
                startButton.addAction(Actions.scaleTo(1.0f, 1.0f, 0.2f));
            }
        });

        // Exit Button
        TextureRegionDrawable exitUp = new TextureRegionDrawable(new TextureRegion(exitTexture));
        TextureRegionDrawable exitDown = new TextureRegionDrawable(new TextureRegion(exitHoverTexture));
        exitButton = new ImageButton(exitUp, exitDown);
        exitButton.setTransform(true);
        float exitAspect = exitTexture.getHeight() / (float) exitTexture.getWidth();
        exitButton.setSize(buttonWidth, buttonWidth * exitAspect);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
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
        table.top().padTop(Gdx.graphics.getHeight() * 0.45f);

        table.add(startButton).size(buttonWidth, buttonHeight).padBottom(4).row();
        table.add(exitButton).size(buttonWidth, buttonWidth * exitAspect);

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
        startTexture.dispose();
        startHoverTexture.dispose();
        exitTexture.dispose();
        exitHoverTexture.dispose();
    }
}