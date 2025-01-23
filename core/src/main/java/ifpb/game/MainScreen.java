package ifpb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static ifpb.game.FoodBar.FOOD_BOUNDS;
import static ifpb.game.GameState.holdingState;
import static ifpb.game.GameState.updateTime;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class MainScreen implements Screen {
    public static final float WORLD_WIDTH = 160;
    public static final float WORLD_HEIGHT = 90;

    FitViewport viewport;
    SpriteBatch spriteBatch;
    ShapeRenderer shapeRenderer;

    float timeSincePlaying;
    float looped1SecTime;
    Sprite background = new Sprite(new Texture("mainScreen/bg.png"));
    Dude dude;
    Bar energyBar;
    Bar hungerBar;
    Bar happinessBar;
    Bar healthBar;
    FoodBar foodBar;
    Sprite heldFood;
    Lamp lamp;
    Vector2 dragPos, endPos;

    @Override
    public void show() {
        // Prepare your screen here.

        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        background.setCenter(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f);
        float scale = WORLD_WIDTH / background.getWidth();
        background.setScale(scale);
        background.translateY(5);

        dude = new Dude();
        dude.setScale(0.5f);
        dude.holiSprite.doToChildren(sprite -> sprite.setCenter(WORLD_WIDTH / 2, WORLD_HEIGHT / 2));

        Vector2 barPos = new Vector2(1, 80);
        Vector2 barSize = new Vector2(20, 5);
        float gap = 6;

        Bar.Builder builder = new Bar.Builder()
            .withColor(Color.WHITE)
            .withX(barPos.x)
            .withWidth(barSize.x)
            .withHeight(barSize.y);

        energyBar = builder.
            withValue(GameState.getEnergy())
            .withY(barPos.y)
            .withIcon(new Sprite(new Texture("mainScreen/energy.png")))
            .build();
        happinessBar = builder.
            withValue(GameState.getEnergy())
            .withY(barPos.y - gap * 1)
            .withIcon(new Sprite(new Texture("mainScreen/happiness.png")))
            .build();
        hungerBar = builder.
            withValue(GameState.getHunger())
            .withY(barPos.y - gap * 2)
            .withIcon(new Sprite(new Texture("mainScreen/hunger.png")))
            .build();
        healthBar = builder.
            withValue(GameState.getHealth())
            .withY(barPos.y - gap * 3)
            .withIcon(new Sprite(new Texture("mainScreen/health.png")))
            .build();

        foodBar = new FoodBar();
        holdingState = HoldingState.NOT_HOLDING;

        dragPos = new Vector2();
        endPos = new Vector2();

        this.lamp = new Lamp(WORLD_WIDTH / 4, 0, .2f);
        lamp.getLamp().doToAll(sprite -> sprite.setCenterX(WORLD_WIDTH / 2));
        lamp.getLamp().doToAll(
            sprite -> sprite.setY(
                WORLD_HEIGHT / 2.81f
            )
        );
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        input();
        logic(delta);
        draw();

    }

    private void input() {

        handleFullscreen();

        Vector2 cursorPos = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        handleLamp(cursorPos);

        handleHolding(cursorPos);


        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            dude.changeColor(Color.valueOf("8E00FF"));
        }

    }

    public void handleFullscreen() {
        if (Gdx.input.isKeyPressed(Input.Keys.F))
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        if (Gdx.input.isKeyPressed(Input.Keys.G))
            Gdx.graphics.setWindowedMode(1280, 720);
    }

    public void handleLamp(Vector2 cursorPos) {
        if (Gdx.input.justTouched()) {
            if (lamp.getLamp().getSprite().getBoundingRectangle().contains(cursorPos)) {
                lamp.switchState();
            }
        }
    }

    public void handleHolding(Vector2 cursorPos) {
        if (GameState.sleeping)
            return;

        if (Gdx.input.justTouched()) {
            dragPos = cursorPos;
            if (FOOD_BOUNDS.contains(dragPos)) {
                holdingState = HoldingState.IS_HOLDING_FOOD;
            } else {
                holdingState = HoldingState.IS_HOLDING;
            }

            if (foodBar.buttonPrevious.getBoundingRectangle().contains(cursorPos)) {
                foodBar.previous();
            } else if (foodBar.buttonNext.getBoundingRectangle().contains(cursorPos)) {
                foodBar.next();
            }
            return;
        }
        if (Gdx.input.isTouched()) {
            endPos = cursorPos;
        } else if (holdingState != HoldingState.STOPPED_HOLDING && holdingState != HoldingState.NOT_HOLDING && holdingState != HoldingState.STOPPED_HOLDING_FOOD) {
            if (holdingState == HoldingState.IS_HOLDING_FOOD) {
                holdingState = HoldingState.STOPPED_HOLDING_FOOD;
            } else {
                holdingState = HoldingState.STOPPED_HOLDING;
            }

            return;
        }
        if (holdingState == HoldingState.STOPPED_HOLDING || holdingState == HoldingState.STOPPED_HOLDING_FOOD) {

            holdingState = HoldingState.NOT_HOLDING;
        }
    }

    private void logic(float delta) {
        timeSincePlaying += delta;
        looped1SecTime += delta;
        dude.doPhysics(timeSincePlaying, delta);
        handleFood();

        dude.doFaceExpressionCalculations();

        energyBar.setValue(GameState.getEnergy());
        happinessBar.setValue(GameState.getHappiness());
        hungerBar.setValue(GameState.getHunger());
        healthBar.setValue(GameState.getHealth());

        if (looped1SecTime >= 1) {
            looped1SecTime -= 1;
            GameState.incrementTime();
        }

        updateTime();

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
        ScreenUtils.clear(new Color(.153f, .153f, .153f, 1f));
//        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        background.draw(spriteBatch);

        dude.render(spriteBatch);

        lamp.render(spriteBatch);

        energyBar.render(shapeRenderer, spriteBatch);
        happinessBar.render(shapeRenderer, spriteBatch);
        hungerBar.render(shapeRenderer, spriteBatch);
        healthBar.render(shapeRenderer, spriteBatch);
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
        GameState.save();
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
        energyBar.dispose();
        happinessBar.dispose();
        hungerBar.dispose();
        dude.holiSprite.doToChildren(sprite -> sprite.getTexture().dispose());
    }
}
