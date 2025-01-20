package ifpb.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


public class Dude {
    public static final float scaleFactor = .5f;

    float transitionTime = 0, transitionDuration = 4;

    private float hunger;
    private float happiness;
    private float energy;
    private float health;
    private Color startColor, endColor, current;

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        if (health < 0) {
            this.health = 0;
            return;
        }
        if (health > 1) {
            this.health = 1;
            return;
        }
        this.health = health;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        if (energy < 0) {
            this.energy = 0;
            return;
        }
        if (energy > 1) {
            this.energy = 1;
            return;
        }
        this.energy = energy;
    }

    public float getHappiness() {
        return happiness;
    }

    public void setHappiness(float happiness) {
        if (happiness < 0) {
            this.happiness = 0;
            return;
        }
        if (happiness > 1) {
            this.happiness = 1;
            return;
        }
        this.happiness = happiness;
    }

    public float getHunger() {
        return hunger;
    }

    public void setHunger(float hunger) {
        if (hunger < 0) {
            this.hunger = 0;
            return;
        }
        if (hunger > 1) {
            this.hunger = 1;
            return;
        }
        this.hunger = hunger;
    }


    final SingleSpriteNode holiSprite;
    final SingleSpriteNode eyes, body, head;

    private float timeBlinking;

    public Dude() {
        hunger = .0f;
        happiness = .0f;
        energy = .0f;
        health = .0f;

        startColor = Color.valueOf("0fdcdf");
        endColor = Color.valueOf("0fdcdf");
        current = Color.valueOf("0fdcdf");

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
        applyColor(current);


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

        updateColorTransition(delta);
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
        holiSprite.renderChildren(spriteBatch);
    }

    public void setScale(float scaleFactor) {
        holiSprite.doToAll(sprite -> sprite.setScale(scaleFactor));
    }

    public void changeColor(Color color) {
        startColor = current;
        endColor = color;
        transitionDuration = 4.0f;
        transitionTime = 0.0f;
    }

    public void applyColor(Color color) {
        Consumer<Sprite> consumer = sprite -> sprite.setColor(color);
        head.doToThis(consumer);
        body.doToThis(consumer);
    }

    private void updateColorTransition(float delta) {
        if (transitionTime < transitionDuration) {
            transitionTime += delta;
            float progress = MathUtils.colorFunction(transitionTime);
            current = new Color(
                Interpolation.linear.apply(startColor.r, endColor.r, progress),
                Interpolation.linear.apply(startColor.g, endColor.g, progress),
                Interpolation.linear.apply(startColor.b, endColor.b, progress),
                Interpolation.linear.apply(startColor.a, endColor.a, progress)
            );
        } else {
            changeColor(Color.valueOf("0fdcdf"));
        }
        applyColor(current);
    }

    public void consumeFood(@NotNull Food food) {
        this.setEnergy(this.getEnergy() + food.energyLevel);
        this.setHappiness(this.getHappiness() + food.happinessLevel);
        this.setHunger(this.getHunger()+food.hungerLevel);
        this.setHealth(this.getHealth()+food.healthLevel);
        changeColor(food.color);
    }

    public Rectangle getBoundingBox() {
        return ((SingleSpriteNode) holiSprite.get("head")).getSprite().getBoundingRectangle();
    }
}
