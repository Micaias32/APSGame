package ifpb.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;

public class Lamp {
    private final SingleSpriteNode lamp;
    private final Sprite dim;
    private boolean isOn;

    public Lamp(float x, float y, float scale) {
        dim = new Sprite(new Texture("mainScreen/dim.png"));
        dim.setSize(MainScreen.WORLD_WIDTH, MainScreen.WORLD_HEIGHT);

        lamp = new SingleSpriteNode(new Sprite(new Texture("mainScreen/lamp/lamp.png")));

        HashMap<String, ISpriteLeaf> sprites = new HashMap<>();
        sprites.put(
            "on",
            new SingleSpriteNode(
                new Sprite(
                    new Texture(
                        "mainScreen/lamp/on.png"
                    )
                )
            )
        );
        sprites.put(
            "off",
            new SingleSpriteNode(
                new Sprite(
                    new Texture(
                        "mainScreen/lamp/off.png"
                    )
                )
            )
        );

        lamp.addChild("state", new MultiSingleSpriteNode(lamp, sprites, "on"));
        isOn = true;
        lamp.doToAll(sprite -> sprite.setPosition(x, y));
        lamp.doToAll(sprite -> sprite.setScale(scale));
    }

    public void render(SpriteBatch spriteBatch) {
        lamp.renderAll(spriteBatch);
        if (((MultiSingleSpriteNode) lamp.get("state")).getCurrent().equals("off")) {
            dim.draw(spriteBatch);
        }
    }

    public void switchState() {
        ((MultiSingleSpriteNode) lamp.get("state")).setCurrent(isOn? "off" : "on");
        isOn = !isOn;
    }
}
