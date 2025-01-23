package ifpb.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;

public class Lamp {
    private final SingleSpriteNode lamp;
    private final Sprite dim;

    public Lamp(float x, float y, float scale) {
        dim = new Sprite(new Texture("mainScreen/dim.png"));
        dim.setSize(MainScreen.WORLD_WIDTH, MainScreen.WORLD_HEIGHT);
        dim.setAlpha(.5f);

        lamp = new SingleSpriteNode(new Sprite(new Texture("mainScreen/lamp/lamp.png")));
        lamp.doToThis(sprite -> sprite.setColor(0, 0, 0, 0));

        HashMap<String, ISpriteLeaf> sprites = new HashMap<>();
        sprites.put(
            "on",
            new SingleSpriteNode(
                new Sprite(
                    new Texture(
                        "mainScreen/lamp/sun.png"
                    )
                )
            )
        );
        sprites.put(
            "off",
            new SingleSpriteNode(
                new Sprite(
                    new Texture(
                        "mainScreen/lamp/moon.png"
                    )
                )
            )
        );

        lamp.addChild("state", new MultiSpriteNode(sprites, GameState.isLightOn ? "on" : "off"));
        lamp.doToAll(sprite -> sprite.setPosition(x, y));
        lamp.doToAll(sprite -> sprite.setScale(scale));
    }

    public void render(SpriteBatch spriteBatch) {
        lamp.renderAll(spriteBatch);
        if (((MultiSpriteNode) lamp.get("state")).getCurrent().equals("off")) {
            dim.draw(spriteBatch);
        }
    }

    public void switchState() {
        GameState.isLightOn = !GameState.isLightOn;
        ((MultiSpriteNode) lamp.get("state")).setCurrent(GameState.isLightOn ? "on" : "off");
        GameState.sleeping = !GameState.isLightOn && GameState.getEnergy() < .9f;
    }

    public SingleSpriteNode getLamp() {
        return this.lamp;
    }
}
