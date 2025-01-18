package ifpb.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;

public class MultiSpriteNode extends SpriteNode {
    private final HashMap<String, Sprite> sprites;
    private String current;

    public MultiSpriteNode(SpriteNode parent, HashMap<String, Sprite> sprites, String current) {
        super(parent);
        this.sprites = sprites;
        this.current = current;
    }

    @Override
    public void addChild(String key, SpriteNode sprite) {
        sprites.put(key, sprite);
    }

    public void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
        sprites.get(current).draw(spriteBatch);
    }

    public void setCurrent(String current) {
        this.current = current;
    }
}
