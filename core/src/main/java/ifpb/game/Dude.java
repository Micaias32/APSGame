package ifpb.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.OrderedMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Dude {
    public static final float scaleFactor = .5f;
    private int hunger, happiness, energy;
    private String name;
    private OrderedMap<String, Sprite> sprites;

    private boolean isBlinking;

    public Dude(@NotNull String name, boolean isOC) {
        this.name = name;
        hunger = 50;
        happiness = 75;
        energy = 90;
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
            sprites.put("body", new Sprite(new Texture("character/body.png")));
            sprites.put("head", new Sprite(new Texture("character/head.png")));
            sprites.put("eyes", new Sprite(new Texture("character/eyes.png")));
            sprites.put("pupils", new Sprite(new Texture("character/pupils.png")));
            sprites.put("mouth", new Sprite(new Texture("character/mouth.png")));
        }

        doToSprites(sprite -> {
            sprite.setScale(scaleFactor);
            sprite.setX(80);
        });
    }

    public void doToSpritesFiltered(@NotNull String filter, @NotNull Consumer<Sprite> consumer) {
        ArrayList<String> sprites = new ArrayList<>();

        if (filter.equals("head")) {
            sprites.add("head");
            sprites.add("eyes");
            sprites.add("pupils");
            sprites.add("mouth");
        }

        for (String s : sprites) {
            consumer.accept(this.sprites.get(s));
        }
    }

    public void doPhysics(float time) {
        doToSpritesFiltered("head", sprite -> sprite.setY((float) Math.sin(time * 2.5f) / 2 - 42.5f / 2f));
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

    public int getHunger() {
        return hunger;
    }

    public int getHappiness() {
        return happiness;
    }

    public int getEnergy() {
        return energy;
    }

    public @NotNull String getName() {
        return name;
    }

}
