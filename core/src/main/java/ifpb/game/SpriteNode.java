package ifpb.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.OrderedMap;

import java.util.function.Consumer;

public class SpriteNode extends Sprite {
    private final SpriteNode parent;
    private final OrderedMap<String, SpriteNode> children = new OrderedMap<>();

    public SpriteNode(SpriteNode parent, Sprite sprite) {
        super(sprite);
        this.parent = parent;

    }
    public SpriteNode(SpriteNode parent) {
        super();
        this.parent = parent;

    }


    public void draw(SpriteBatch spriteBatch) {
        drawFlat(spriteBatch);
        children.forEach(((entry) -> entry.value.draw(spriteBatch)));
    }

    public void drawFlat(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
    }

    public void addChild(String key, SpriteNode sprite) {
        children.put(key, sprite);
    }

    public SpriteNode get(String key) {
        return children.get(key);
    }

    public void doToChildren(Consumer<SpriteNode> consumer) {
        children.forEach(entry -> consumer.accept(entry.value));
    }

    @Override
    public void translate(float xAmount, float yAmount) {
        super.translate(xAmount, yAmount);
        doToChildren(sprite -> sprite.translate(xAmount, yAmount));

    }

    @Override
    public void setScale(float scaleXY) {
        super.setScale(scaleXY);
        doToChildren(sprite -> sprite.setScale(scaleXY));
    }

    @Override
    public void setCenter(float x, float y) {
        super.setCenter(x, y);
        doToChildren(spriteNode -> spriteNode.setCenter(x, y));
    }

    @Override
    public void setRegion(int x, int y, int width, int height) {
        super.setRegion(x, y, width, height);
        doToChildren(spriteNode -> spriteNode.setRegion(x, y, width, height));
    }
}
