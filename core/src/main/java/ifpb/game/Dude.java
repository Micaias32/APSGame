package ifpb.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.OrderedMap;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class Dude {
    public static final float scaleFactor = .5f;
    private int hunger, happiness, energy;
    private String name;
    private OrderedMap<String, Sprite> sprites;

    public Dude(@NotNull String name, boolean isOC) {
        this.name = name;
        hunger = 50;
        happiness = 75;
        energy = 90;

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
        }

        doToSprites(sprite -> sprite.setScale(scaleFactor));
    }

    public void render(@NotNull SpriteBatch spriteBatch) {
        doToSprites(sprite -> sprite.draw(spriteBatch));
    }

    public void doToSprites(@NotNull Consumer<Sprite> consumer) {
        sprites.values().forEach(consumer);
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
