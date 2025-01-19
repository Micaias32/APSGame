package ifpb.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


public class Dude {
    public static final float scaleFactor = .5f;
    private float hunger, happiness, energy;
    final SingleSpriteNode holiSprite;
    final SingleSpriteNode eyes, body, head;

    private float timeBlinking;

    public Dude() {
        hunger = .50f;
        happiness = .75f;
        energy = .90f;

        Sprite body, head;

        body = new Sprite(new Texture("character/nocolor/body.png"));
        head = new Sprite(new Texture("character/nocolor/head.png"));

        holiSprite = new SingleSpriteNode(
            new Sprite(
                new TextureRegion(
                    new Texture("food/lubi.png"),
                    0,
                    0,
                    0,
                    0
                )
            )
        );
        this.head = new SingleSpriteNode(head);
        this.body = new SingleSpriteNode(body);
        holiSprite.addChild("body", this.body);
        holiSprite.addChild("head", this.head);

        Sprite mouth, eyes, pupils;

        mouth = new Sprite(new Texture("character/mouth.png"));

        eyes = new Sprite(new Texture("character/eyes.png"));
        pupils = new Sprite(new Texture("character/pupils.png"));

        SingleSpriteNode headNode = (SingleSpriteNode) holiSprite.get("head");
        this.eyes = new SingleSpriteNode(eyes);
        headNode.addChild("eyes", this.eyes);
        headNode.addChild("mouth", new SingleSpriteNode(mouth));

        SingleSpriteNode eyesNode = (SingleSpriteNode) headNode.get("eyes");
        eyesNode.addChild("pupils", new SingleSpriteNode(pupils));

        Sprite legs = new Sprite(new Texture("character/legs.png"));

        ((SingleSpriteNode) holiSprite.get("body")).addChild("legs", new SingleSpriteNode(legs));

        holiSprite.doToAll(sprite -> sprite.setScale(scaleFactor));
        changeColor(Color.valueOf("0fdcdf"));


        timeBlinking = -5f;
    }

    public void doPhysics(float d, float delta) {
        holiSprite.get("head")
            .doToAll(
                sprite -> sprite.translate(
                    0,
                    ((float) Math.sin(d)) - ((float) Math.sin(d-delta))
                )
            );
        updateBlinking(delta);
        timeBlinking %= 5;
    }

    private void updateBlinking(float time) {
        timeBlinking += time;
        float amount = 1 - MathUtils.blinkingFunction(timeBlinking);
        float spriteHeight = eyes.getSprite().getHeight();
        int height = (int) (spriteHeight * amount);
        Sprite sprite = eyes.getSprite();
        int x = sprite.getRegionX();
        int y = sprite.getRegionY();
        int width = sprite.getRegionWidth();
        eyes.doToAll(sprite1 -> sprite1.setRegion(x, y, width, height));
    }

    public void render(@NotNull SpriteBatch spriteBatch) {
        holiSprite.renderAll(spriteBatch);
    }

    public void setScale(float scaleFactor) {
        holiSprite.doToAll(sprite -> sprite.setScale(scaleFactor));
    }

    public void changeColor(Color color) {
        Consumer<Sprite> consumer = sprite -> sprite.setColor(color);
        head.doToThis(consumer);
        body.doToThis(consumer);
    }

    public float getHunger() {
        return hunger;
    }

    public float getHappiness() {
        return happiness;
    }

    public float getEnergy() {
        return energy;
    }

    public void consumeFood(@NotNull Food food) {
        this.energy = Math.min(this.getEnergy() + food.energyLevel, 1.0f);
        this.happiness = Math.min(this.getHappiness() + food.happinessLevel, 1.0f);
        this.hunger = Math.min(this.getHunger()+food.hungerLevel, 1.0f);
        holiSprite.doToChildren(spriteNode -> spriteNode.setColor(food.color));
    }

    public Rectangle getBoundingBox() {
        return ((SingleSpriteNode) holiSprite.get("head")).getSprite().getBoundingRectangle();
    }
}
