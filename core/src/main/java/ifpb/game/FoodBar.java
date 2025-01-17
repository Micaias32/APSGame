package ifpb.game;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class FoodBar {
    public static final float PADDING = 2;
    public static final Rectangle FOOD_BOUNDS = new Rectangle(
        MainScreen.WORLD_WIDTH-Food.FOOD_SIZE-PADDING,
        MainScreen.WORLD_HEIGHT-Food.FOOD_SIZE-PADDING,
        Food.FOOD_SIZE,
        Food.FOOD_SIZE
        );

    private ArrayList<Food> foods;
    private int index;
    private final Sprite buttonNext;

    public FoodBar() {
        this.foods = new ArrayList<>();
        this.index = 0;


        foods.add(new Food(
            new Texture("food/lubi.png"),
            .10f,
            .10f,
            .10f,
            .10f,
            Color.valueOf("FF3627")
        ));
        foods.get(0).sprite.setPosition(
            FOOD_BOUNDS.x,
            FOOD_BOUNDS.y
        );

        buttonNext = new Sprite(new Texture("mainScreen/buttonFood.png"));
        buttonNext.setSize(8, 8);
        buttonNext.setPosition(
            MainScreen.WORLD_WIDTH - 8 - PADDING,
            MainScreen.WORLD_HEIGHT - Food.FOOD_SIZE - 8 - (PADDING*2)
        );
    }

    Food next() {
        Food food = foods.get(index);
        index++;
        if (index == foods.size()) {
            index = 0;
        }
        return food;
    }

    public Food getFood() {
        return foods.get(index);
    }


    void render(SpriteBatch spriteBatch) {
        foods.get(index).sprite.draw(spriteBatch);
        buttonNext.draw(spriteBatch);
    }
}
