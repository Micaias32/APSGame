package ifpb.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Scanner;

import static ifpb.game.GameState.statePath;

public class LoadingScreen extends InputAdapter implements Screen {
    Game game;
    Sprite littleDude = new Sprite(new Texture("character/holi_main.png"));
    int width, height;
    boolean loaded = false;


    SpriteBatch spriteBatch = new SpriteBatch();

    public LoadingScreen(Game game) {
        this.game = game;

        new Loader(statePath, () -> loaded = true).start();

    }


    @Override
    public void show() {

        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        littleDude.setCenter(width / 2f, height / 2f);

    }

    @Override
    public void render(float delta) {
        if (loaded) {
            game.setScreen(new MainScreen());
            this.hide();
        }

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        spriteBatch.begin();

        littleDude.draw(spriteBatch);

        spriteBatch.end();

        littleDude.rotate(-100f * delta);

    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    interface LoaderCallback {
        void onLoadComplete();
    }

    static class Loader extends Thread {
        String path;
        LoaderCallback callback;

        public Loader(String path, LoaderCallback callback) {
            this.path = path;
            this.callback = callback;
        }

        public void run() {

            HashMap<String, String> things = getStringStringHashMap();

            if (!things.isEmpty()) {
                GameState.setEnergy(Float.parseFloat(things.get("energy")));
                GameState.setHealth(Float.parseFloat(things.get("health")));
                GameState.setHunger(Float.parseFloat(things.get("hunger")));
                GameState.setHappiness(Float.parseFloat(things.get("happiness")));
                GameState.isLightOn = Boolean.parseBoolean(things.get("light"));
                GameState.sleeping = Boolean.parseBoolean(things.get("sleeping"));
                LocalDateTime date = LocalDateTime.now();
                GameState.timeSinceUpdate = date.toEpochSecond(ZoneOffset.ofHours(-3)) - Long.parseLong(things.get("time"));

                GameState.updateTime();

            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (callback != null) {
                callback.onLoadComplete();
            }

        }

        @NotNull
        private HashMap<String, String> getStringStringHashMap() {
            File file = new File(path);
            try {
                @SuppressWarnings("unused")
                var created = file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Scanner scanner;
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            HashMap<String, String> things = new HashMap<>();

            do {
                String[] text = scanner.nextLine().split(",");
                if (text[0].isEmpty()) {
                    continue;
                }
                String key = text[0], value = text[1];

                things.put(key, value);

            } while (scanner.hasNextLine());

            scanner.close();
            return things;
        }
    }

}
