package ifpb.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.function.Consumer;

public interface ISpriteLeaf {

    void renderThis(SpriteBatch spriteBatch);

    void renderChildren(SpriteBatch spriteBatch);

    default void renderAll(SpriteBatch spriteBatch) {
        renderThis(spriteBatch);
        renderChildren(spriteBatch);
    }

    void doToThis(Consumer<Sprite> consumer);

    void doToChildren(Consumer<Sprite> consumer);

    default void doToAll(Consumer<Sprite> consumer) {
        doToThis(consumer);
        doToChildren(consumer);
    }
}
