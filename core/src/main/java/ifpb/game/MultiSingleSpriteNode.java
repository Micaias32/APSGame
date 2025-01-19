package ifpb.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.function.Consumer;

public class MultiSingleSpriteNode implements ISpriteLeaf {
    private final HashMap<String, ISpriteLeaf> sprites;
    private String current;

    public MultiSingleSpriteNode(SingleSpriteNode parent, HashMap<String, ISpriteLeaf> sprites, String current) {
        this.sprites = sprites;
        this.current = current;
    }

    public void addOptions(String key, SingleSpriteNode sprite) {
        sprites.put(key, sprite);
    }


    public void setCurrent(String current) {
        this.current = current;
    }

    public String getCurrent() {
        return current;
    }

    @Override
    public void renderThis(SpriteBatch spriteBatch) {
        sprites.get(current).renderThis(spriteBatch);
    }

    @Override
    public void renderChildren(SpriteBatch spriteBatch) {
        sprites.get(current).renderChildren(spriteBatch);
    }

    @Override
    public void doToThis(Consumer<Sprite> consumer) {
        sprites.values().forEach(leaf -> leaf.doToThis(consumer));
    }

    @Override
    public void doToChildren(Consumer<Sprite> consumer) {
        sprites.values().forEach(iSpriteLeaf -> iSpriteLeaf.doToChildren(consumer));
    }
}
