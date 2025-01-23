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
        MainScreen.WORLD_WIDTH - Food.FOOD_SIZE - PADDING,
        MainScreen.WORLD_HEIGHT - Food.FOOD_SIZE - PADDING,
        Food.FOOD_SIZE,
        Food.FOOD_SIZE
    );

    private final ArrayList<Food> foods;
    public final Sprite buttonNext, buttonPrevious;
    private int index;

    public FoodBar() {
        this.foods = new ArrayList<>();
        this.index = 0;


//        foods.add(new Food(
//            new Texture("food/lubi.png"),
//            -.10f,
//            .10f,
//            .10f,
//            .10f,
////            Color.valueOf("FF3627")
//            Color.PINK
//        ));
        foods.add(new Food(
            new Texture("food/apple.png"),
            .01f,
            .30f,
            .30f,
            .2f,
//            Color.valueOf("FF3627")
            Color.RED
        ));
        foods.add(new Food(
            new Texture("food/chicken.png"),
            -.1f,
            .40f,
            .20f,
            .0f,
            Color.valueOf("B66C3B")
        ));
        foods.add(new Food(
            new Texture("food/menergy.png"),
            .50f,
            .05f,
            .10f,
            -.10f,
//            Color.valueOf("FF3627")
            Color.GREEN
        ));
        foods.forEach(food -> food.sprite.setPosition(
            FOOD_BOUNDS.x,
            FOOD_BOUNDS.y
        ));

        buttonNext = new Sprite(new Texture("mainScreen/buttonFood.png"));
        buttonNext.setSize(8, 8);
        buttonNext.setPosition(
            MainScreen.WORLD_WIDTH - 8 - PADDING,
            MainScreen.WORLD_HEIGHT - Food.FOOD_SIZE - 8 - (PADDING * 2)
        );

        buttonPrevious = new Sprite(buttonNext);
        buttonPrevious.setFlip(true, false);
        buttonPrevious.translateX(- Food.FOOD_SIZE + 8);
    }

    void next() {
        index++;
        if (index == foods.size()) {
            index = 0;
        }
    }

    void previous() {
        index--;
        if (index < 0) {
            index = foods.size() - 1;
        }
    }

    public Food getFood() {
        return foods.get(index);
    }


    void render(SpriteBatch spriteBatch) {
        foods.get(index).sprite.draw(spriteBatch);
        buttonNext.draw(spriteBatch);
        buttonPrevious.draw(spriteBatch);
    }
}
