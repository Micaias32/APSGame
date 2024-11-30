package ifpb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class DialogScreen implements Screen {

    FitViewport viewport;
    SpriteBatch spriteBatch;
    float d;
    Music music;
    Dude dude;

    @Override
    public void show() {
        // Prepare your screen here.

        viewport = new FitViewport(160, 90);
        spriteBatch = new SpriteBatch();

        dude = new Dude("Borrachinha", true);

        music = Gdx.audio.newMusic(Gdx.files.internal("borrachinhaRica/stayin.mp3"));

        music.setLooping(true);
        music.setVolume(.5f);
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        input();
        logic(delta);
        draw();

    }
    //    b    b    b    b    a

    private void input() {

    }

    private void logic(float delta) {
        d += delta;
        float barDuration = (4 * 60) / 104f;
        float elapsedTime = d;
        float phase = (elapsedTime % barDuration) / barDuration;
        float angle = (float) (phase * 2 * Math.PI);
//        System.out.println(angle);

        dude.doToSprites(sprite -> sprite.setScale((float) Math.sin((angle)) * .5f));
        dude.doToSprites(sprite -> sprite.translateX((float) Math.sin(angle)));
        dude.doToSprites(sprite -> sprite.translateY((float) Math.cos(angle)));
        music.play();
    }

    private void draw() {
        ScreenUtils.clear(Color.GRAY);
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
