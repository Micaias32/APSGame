package ifpb.game;

import com.badlogic.gdx.Game;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {
    @Override
    public void create() {
        System.out.println(GameState.statePath);
        setScreen(new LoadingScreen(this));
    }
}
