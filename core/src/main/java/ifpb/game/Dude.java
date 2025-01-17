package ifpb.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.OrderedMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Dude {
    public static final float scaleFactor = .5f;
    public static final float moveFactor = 3;
    private float hunger, happiness, energy;
    private String name;
    private OrderedMap<String, Sprite> sprites;

    private boolean isBlinking;
    private float timeBlinking;

    public Dude(@NotNull String name, boolean isOC) {
        this.name = name;
        hunger = .50f;
        happiness = .75f;
        energy = .90f;
        isBlinking = true;

        sprites = new OrderedMap<>();

        if (isOC) {
            sprites.put("body", new Sprite(new Texture("borrachinhaRica/corpo.png")));
            sprites.put("head", new Sprite(new Texture("borrachinhaRica/cabeca.png")));
            sprites.put("armLeft", new Sprite(new Texture("borrachinhaRica/braco_esquerda.png")));
            sprites.put("armRight", new Sprite(new Texture("borrachinhaRica/braco_direita.png")));
            sprites.put("eye", new Sprite(new Texture("borrachinhaRica/olho.png")));
            sprites.put("pupil", new Sprite(new Texture("borrachinhaRica/pupila.png")));
            sprites.put("eyelid", new Sprite(new Texture("borrachinhaRica/palpebra.png")));
        } else {
            sprites.put("legs", new Sprite(new Texture("character/legs.png")));
            sprites.put("body", new Sprite(new Texture("character/nocolor/body.png")));
            sprites.put("head", new Sprite(new Texture("character/nocolor/head.png")));
            sprites.put("eyes", new Sprite(new Texture("character/eyes.png")));
            sprites.put("pupils", new Sprite(new Texture("character/pupils.png")));
            sprites.put("mouth", new Sprite(new Texture("character/mouth.png")));
        }

        doToSprites(sprite -> {
            sprite.setScale(scaleFactor);
            sprite.setX(80);
        });
        doToSpritesFiltered("body_blue", sprite -> sprite.setColor(15/255f, 220/255f, 223/255f, 1));
        timeBlinking = -5f;
    }

    public void doToSpritesFiltered(@NotNull String filter, @NotNull Consumer<Sprite> consumer) {
        ArrayList<String> sprites = new ArrayList<>();

        switch (filter) {
            case "head" -> {
                sprites.add("head");
                sprites.add("eyes");
                sprites.add("pupils");
                sprites.add("mouth");
            }
            case "eyes" -> {
                sprites.add("eyes");
                sprites.add("pupils");
            }
            case "body_blue" -> {
                sprites.add("body");
                sprites.add("head");
            }
        }

        for (String s : sprites) {
            consumer.accept(this.sprites.get(s));
        }
    }

    public void doPhysics(float time, float delta) {
        doToSpritesFiltered("head", sprite -> sprite.setY((float) Math.sin(time * 2.5f) / 2 - 42.5f / 2f));
        updateBlinking(delta);
        timeBlinking %= 5;
    }

    private void blink() {

    }

    private void updateBlinking(float time) {
        timeBlinking += time;
        float amount = 1 - MathUtils.blinkingFunction(timeBlinking);
        float spriteHeight = sprites.get("eyes").getHeight();
        int height = (int) (spriteHeight * amount);
        doToSpritesFiltered("eyes", sprite -> {
            int x = sprite.getRegionX();
            int y = sprite.getRegionY();
            int width = sprite.getRegionWidth();
            sprite.setRegion(x, y, width, height);
        });
    }

    public void render(@NotNull SpriteBatch spriteBatch) {
        doToSprites(sprite -> sprite.draw(spriteBatch));
    }

    public void doToSprites(@NotNull Consumer<Sprite> consumer) {
        for (var sprite : sprites.values()) {
            consumer.accept(sprite);
        }
    }

    public void setScale(float scaleFactor) {
        doToSprites(sprite -> sprite.setScale(scaleFactor));
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

    public @NotNull String getName() {
        return name;
    }

    public void consumeFood(@NotNull Food food) {
        this.energy = Math.min(this.getEnergy() + food.energyLevel, 1.0f);
        this.happiness = Math.min(this.getHappiness() + food.happinessLevel, 1.0f);
        this.hunger = Math.min(this.getHunger()+food.hungerLevel, 1.0f);
        doToSpritesFiltered("body_blue", sprite -> sprite.setColor(food.color));
    }

    public Rectangle getBoundingBox() {
        return sprites.get("head").getBoundingRectangle();
    }
}
