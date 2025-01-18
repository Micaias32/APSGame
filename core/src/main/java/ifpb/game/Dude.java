package ifpb.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import org.jetbrains.annotations.NotNull;


public class Dude {
    public static final float scaleFactor = .5f;
    private float hunger, happiness, energy;
    final SpriteNode holiSprite;

    private float timeBlinking;

    public Dude() {
        hunger = .50f;
        happiness = .75f;
        energy = .90f;

        Sprite body, head;

        body = new Sprite(new Texture("character/nocolor/body.png"));
        head = new Sprite(new Texture("character/nocolor/head.png"));

        holiSprite = new SpriteNode(
            null,
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
        holiSprite.addChild("body", new SpriteNode(holiSprite, body));
        holiSprite.addChild("head", new SpriteNode(holiSprite, head));

        Sprite mouth, eyes, pupils;

        mouth = new Sprite(new Texture("character/mouth.png"));

        eyes = new Sprite(new Texture("character/eyes.png"));
        pupils = new Sprite(new Texture("character/pupils.png"));

        SpriteNode headNode = holiSprite.get("head");
        headNode.addChild("eyes", new SpriteNode(headNode, eyes));
        headNode.addChild("mouth", new SpriteNode(headNode, mouth));

        SpriteNode eyesNode = headNode.get("eyes");
        eyesNode.addChild("pupils", new SpriteNode(eyesNode, pupils));

        Sprite legs = new Sprite(new Texture("character/legs.png"));

        holiSprite.get("body").addChild("legs", new SpriteNode(holiSprite.get("body"), legs));

        holiSprite.setScale(scaleFactor);
        holiSprite.doToChildren(sprite -> sprite.setColor(15/255f, 220/255f, 223/255f, 1));


        timeBlinking = -5f;
    }

    public void doPhysics(float d, float delta) {
        holiSprite.get("head").translate(0, ((float) Math.sin(d)) - ((float) Math.sin(d-delta)));
        updateBlinking(delta);
        timeBlinking %= 5;
    }

    private void updateBlinking(float time) {
        timeBlinking += time;
        float amount = 1 - MathUtils.blinkingFunction(timeBlinking);
        float spriteHeight = holiSprite.get("head").get("eyes").getHeight();
        int height = (int) (spriteHeight * amount);
        SpriteNode sprite = holiSprite.get("head").get("eyes");
        int x = sprite.getRegionX();
        int y = sprite.getRegionY();
        int width = sprite.getRegionWidth();
        sprite.setRegion(x, y, width, height);
    }

    public void render(@NotNull SpriteBatch spriteBatch) {
        holiSprite.draw(spriteBatch);
    }

    public void setScale(float scaleFactor) {
        holiSprite.setScale(scaleFactor);
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
        return holiSprite.get("head").getBoundingRectangle();
    }
}
