package ifpb.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Consumer;

import static ifpb.game.GameState.getEnergy;
import static ifpb.game.GameState.isLightOn;


public class Dude {
    public static final float scaleFactor = .5f;
    public static final Color MAIN_COLOR = Color.valueOf("0fdcdf");
    final SingleSpriteNode holiSprite;
    final SingleSpriteNode eyes, body, head;
    final MultiSpriteNode mouth;
    float transitionTime = 0, transitionDuration = 4;
    private Color startColor, endColor, current;
    private float timeBlinking;

    public Dude() {

        startColor = MAIN_COLOR;
        endColor = MAIN_COLOR;
        current = MAIN_COLOR;

        Sprite body, head;

        body = new Sprite(new Texture("character/nocolor/body.png"));
        head = new Sprite(new Texture("character/nocolor/head.png"));

        holiSprite = new SingleSpriteNode(
            new Sprite(
                new TextureRegion(
                    new Texture("character/holi_main.png"),
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

        Sprite mouthHappy, mouthSerious, eyes, eyelash, pupils;

        mouthHappy = new Sprite(new Texture("character/mouth.png"));
        mouthSerious = new Sprite(new Texture("character/mouth_neutral.png"));

        eyes = new Sprite(new Texture("character/eyes.png"));
        eyelash = new Sprite(new Texture("character/nocolor/eyelash.png"));
        pupils = new Sprite(new Texture("character/pupils.png"));

        SingleSpriteNode headNode = (SingleSpriteNode) holiSprite.get("head");
        this.eyes = new SingleSpriteNode(eyes);
        headNode.addChild("eyes", this.eyes);

        headNode.addChild("eyelash", new SingleSpriteNode(eyelash));

        HashMap<String, ISpriteLeaf> options = new HashMap<>();
        options.put("happy", new SingleSpriteNode(mouthHappy));
        options.put("serious", new SingleSpriteNode(mouthSerious));
        this.mouth = new MultiSpriteNode(options, "happy");
        headNode.addChild("mouth", this.mouth);

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
                    ((float) Math.sin(d)) - ((float) Math.sin(d - delta))
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
        head.get("eyelash").doToThis(consumer);
    }

    private void updateColorTransition(float delta) {
        if (transitionTime < transitionDuration) {
            transitionTime += delta;
            float progress = MathUtils.colorFunction(transitionTime);

            if (endColor.equals(MAIN_COLOR) && transitionTime > 2) {
                startColor = MAIN_COLOR;
            }
            current = new Color(
                Interpolation.linear.apply(startColor.r, endColor.r, progress),
                Interpolation.linear.apply(startColor.g, endColor.g, progress),
                Interpolation.linear.apply(startColor.b, endColor.b, progress),
                Interpolation.linear.apply(startColor.a, endColor.a, progress)
            );
        } else {
            changeColor(MAIN_COLOR);
        }
        applyColor(current);
    }

    public void consumeFood(@NotNull Food food) {
        GameState.setEnergy(GameState.getEnergy() + food.energyLevel);
        GameState.setHappiness(GameState.getHappiness() + food.happinessLevel);
        GameState.setHunger(GameState.getHunger() + food.hungerLevel);
        GameState.setHealth(GameState.getHealth() + food.healthLevel);
        changeColor(food.color);
    }

    public Rectangle getBoundingBox() {
        return ((SingleSpriteNode) holiSprite.get("head")).getSprite().getBoundingRectangle();
    }

    public void doFaceExpressionCalculations() {
        boolean isTired = getEnergy() < .3f && !GameState.sleeping;

        if (GameState.sleeping) {
            head.disable("eyes");
        } else {
            head.enable("eyes");
        }
        if (isTired) {
            head.enable("eyelash");
        } else {
            head.disable("eyelash");
        }

        String current;
        if (isLightOn && !isTired) {
            current = "happy";
        } else {
            current = "serious";
        }

        mouth.setCurrent(current);
    }
}
// PRIMARY KEY AUTOINCREMENT
