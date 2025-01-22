package ifpb.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Food {
    public static final float FOOD_SIZE = 24;
    final float energyLevel;
    final float hungerLevel;
    final float happinessLevel;
    final float healthLevel;
    final Color color;
    Sprite sprite;

    public Food(Texture texture, float energyLevel, float hungerLevel, float happinessLevel, float healthLevel, Color color) {
        this.sprite = new Sprite(texture);
        this.energyLevel = energyLevel;
        this.hungerLevel = hungerLevel;
        this.happinessLevel = happinessLevel;
        this.healthLevel = healthLevel;
        this.color = color;

        sprite.setSize(FOOD_SIZE, FOOD_SIZE);
    }

}
