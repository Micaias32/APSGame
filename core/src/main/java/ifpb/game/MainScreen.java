package ifpb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static ifpb.game.FoodBar.FOOD_BOUNDS;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class MainScreen implements Screen {
    public static final float WORLD_WIDTH = 160;
    public static final float WORLD_HEIGHT = 90;

    FitViewport viewport;
    SpriteBatch spriteBatch;
    ShapeRenderer shapeRenderer;

    float d;
    Dude dude;
    Bar energyBar;
    Bar hungerBar;
    Bar happinessBar;
    FoodBar foodBar;
    Sprite heldFood;

    HoldingState holdingState;


    @Override
    public void show() {
        // Prepare your screen here.

        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        dude = new Dude("Borrachinha", false);
        dude.setScale(0.5f);
        dude.doToSprites(sprite -> {
            sprite.setCenterX(80);
            sprite.setCenterY(45);
        });

        Vector2 barPos = new Vector2(1, 80);
        Vector2 barSize = new Vector2(20, 5);
        float gap = 6;

        Bar.Builder builder = new Bar.Builder()
            .withColor(Color.WHITE)
            .withX(barPos.x)
            .withWidth(barSize.x)
            .withHeight(barSize.y);

        energyBar = builder.
            withValue(dude.getEnergy())
            .withY(barPos.y)
            .withIcon(new Sprite(new Texture("mainScreen/energy.png")))
            .build();
        happinessBar = builder.
            withValue(dude.getEnergy())
            .withY(barPos.y-gap*1)
            .withIcon(new Sprite(new Texture("mainScreen/happiness.png")))
            .build();
        hungerBar = builder.
            withValue(dude.getHunger())
            .withY(barPos.y-gap*2)
            .withIcon(new Sprite(new Texture("mainScreen/hunger.png")))
            .build();

        foodBar = new FoodBar();
        holdingState = HoldingState.NOT_HOLDING;

        dragPos = new Vector2();
        endPos = new Vector2();
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        input();
        logic(delta);
        draw();

    }

    Vector2 dragPos, endPos;
    private void input() {
        Vector2 cursorPos = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        if (Gdx.input.justTouched()) {
            dragPos = cursorPos;
            if (FOOD_BOUNDS.contains(dragPos)) {
                holdingState = HoldingState.IS_HOLDING_FOOD;
            } else {
                holdingState = HoldingState.IS_HOLDING;
            }
            return;
        }
        if (Gdx.input.isTouched()) {
            endPos = cursorPos;
        } else if (holdingState != HoldingState.STOPPED_HOLDING && holdingState != HoldingState.NOT_HOLDING && holdingState != HoldingState.STOPPED_HOLDING_FOOD){
            if (holdingState == HoldingState.IS_HOLDING_FOOD) {
                holdingState = HoldingState.STOPPED_HOLDING_FOOD;
            } else {
                holdingState = HoldingState.STOPPED_HOLDING;
            }

            return;
        }
        if (holdingState == HoldingState.STOPPED_HOLDING || holdingState == HoldingState.STOPPED_HOLDING_FOOD) {

            holdingState = HoldingState.NOT_HOLDING;
            System.out.printf("begin { x: %.2f, y: %.2f }\n", dragPos.x, dragPos.y);
            System.out.printf("end   { x: %.2f, y: %.2f }\n\n", endPos.x, endPos.y);
        }
    }

    private void logic(float delta) {
        d += delta;
        dude.doPhysics(d, delta);
        handleFood();

        energyBar.setValue(dude.getEnergy());
        happinessBar.setValue(dude.getHappiness());
        hungerBar.setValue(dude.getHunger());
    }

    private void handleFood() {
        Food foodRn = foodBar.getFood();
        if (holdingState == HoldingState.IS_HOLDING_FOOD) {
            heldFood = new Sprite(foodRn.sprite);
            heldFood.setCenter(endPos.x, endPos.y);
        } else if (holdingState == HoldingState.STOPPED_HOLDING_FOOD) {
            if (dude.getBoundingBox().contains(endPos)) {
                dude.consumeFood(foodRn);
            }
            heldFood = null;
            endPos = FOOD_BOUNDS.getCenter(dragPos);
        }
    }

    private void draw() {
        ScreenUtils.clear(new Color(.2f, .2f, .2f, 1f));
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        dude.render(spriteBatch);

        energyBar.render(shapeRenderer, spriteBatch);
        happinessBar.render(shapeRenderer, spriteBatch);
        hungerBar.render(shapeRenderer, spriteBatch);
        foodBar.render(spriteBatch);

        if (heldFood != null) {
            heldFood.draw(spriteBatch);
        }

        spriteBatch.end();
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        // Resize your screen here. The parameters represent the new window size.
        viewport.update(width, height, true); // true centers the camera
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        energyBar.dispose();
        happinessBar.dispose();
        hungerBar.dispose();
        dude.doToSprites(sprite -> sprite.getTexture().dispose());
    }
}
