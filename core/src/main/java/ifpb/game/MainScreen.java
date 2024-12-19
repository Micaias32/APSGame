package ifpb.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class MainScreen implements Screen {

    FitViewport viewport;
    SpriteBatch spriteBatch;
    float d;
    Dude dude;

    @Override
    public void show() {
        // Prepare your screen here.

        viewport = new FitViewport(160, 90);
        spriteBatch = new SpriteBatch();

        dude = new Dude("Borrachinha", false);
        dude.setScale(0.5f);
        dude.doToSprites(sprite -> {
            sprite.setCenterX(80);
            sprite.setCenterY(45);
        });

    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        input();
        logic(delta);
        draw();

    }

    private void input() {

    }

    private void logic(float delta) {
        d += delta;
        dude.doPhysics(d, delta);
    }

    private void draw() {
        ScreenUtils.clear(new Color(.2f, .2f, .2f, 1f));
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        dude.render(spriteBatch);

        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
        viewport.update(width, height, true); // true centers the camera
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }
}
