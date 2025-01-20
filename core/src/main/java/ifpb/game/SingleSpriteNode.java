package ifpb.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.OrderedMap;

import java.util.HashSet;
import java.util.function.Consumer;

public class SingleSpriteNode implements ISpriteLeaf {
    private final Sprite self;
    private final OrderedMap<String, ISpriteLeaf> children = new OrderedMap<>();
    HashSet<String> disabled = new HashSet<>();

    public SingleSpriteNode(Sprite self) {
        this.self = self;
    }

    public void addChild(String key, ISpriteLeaf sprite) {
        children.put(key, sprite);
    }

    public ISpriteLeaf get(String key) {
        return children.get(key);
    }

    @Override
    public void renderThis(SpriteBatch spriteBatch) {
        self.draw(spriteBatch);
    }

    @Override
    public void renderChildren(SpriteBatch spriteBatch) {
        children.forEach(entry -> {
            if (!disabled.contains(entry.key))
                entry.value.renderAll(spriteBatch);
        });
    }

    @Override
    public void doToThis(Consumer<Sprite> consumer) {
        consumer.accept(self);
    }

    @Override
    public void doToChildren(Consumer<Sprite> consumer) {
        children.values().forEach(iSpriteLeaf -> iSpriteLeaf.doToAll(consumer));
    }

    @Override
    public void doToAll(Consumer<Sprite> consumer) {
        doToThis(consumer);
        doToChildren(consumer);
    }

    public Sprite getSprite() {
        return self;
    }

    public void disable(String key) {
        disabled.add(key);
    }

    public void enable(String key) {
        disabled.remove(key);
    }
}
