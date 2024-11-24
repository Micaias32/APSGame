package ifpb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Dialog Screen. When something is being talked, it is here that it is talked.
 */
public class DialogScreen implements Screen {

    FitViewport viewport;
    SpriteBatch spriteBatch;
    ShapeRenderer shapeRenderer;
    BitmapFont textFont;
    DialogState dialogState;
    String name;
    String dialog;
    DialogContent dialogContent;

    Person person;
    Rectangle dialogBox;

    @Override
    public void show() { // Prepare your screen here.
        viewport = new FitViewport(160, 90);
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();

        dialogBox = new Rectangle(
            0f, 0f,
            viewport.getWorldWidth(),
            viewport.getWorldHeight() * .4f
        );
        System.out.println(viewport.getWorldWidth());
        System.out.println(viewport.getWorldHeight());

        FreeTypeFontGenerator ttf = new FreeTypeFontGenerator(Gdx.files.internal("main.ttf"));
        ttf.scaleForPixelHeight(10);
        FreeTypeFontGenerator.FreeTypeFontParameter ttfParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        ttfParam.size = 40;
        textFont = ttf.generateFont(ttfParam);
        textFont.setColor(Color.BLACK);

        dialogContent = new DialogContent(
            new String[]{
                "Actor 1",
                "Actor 2",
            }, new Speech[]{
            new Speech("Actor 1", "Hello"),
            new Speech("Actor 2", "Hello there"),
        });

        textFont.setUseIntegerPositions(false);
        textFont.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());

        Speech speech = dialogContent.next().orElseThrow();

        name = speech.getTalking();
        dialog = speech.getTalked();

        person = new Person(
            "Actor 1",
            new Sprite(
                new Texture(
                    Gdx.files.internal("alfred.png")
                )
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
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)
            && dialogState != DialogState.ON_TRANSITION) {
            dialogState = DialogState.SHOULD_BE_TRANSITIONED;
        }
    }

    private void logic(float delta) {

        if (dialogState == DialogState.SHOULD_BE_TRANSITIONED) {
            person.littleJump();
        }

        person.update(delta);
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); // Use ShapeType.Filled for a filled rectangle
        shapeRenderer.setColor(Color.WHITE); // Or any color you prefer
        shapeRenderer.rect(dialogBox.x, dialogBox.y, dialogBox.width, dialogBox.height);
        shapeRenderer.end();

        spriteBatch.begin();


        this.renderText(spriteBatch);

        person.render(spriteBatch);


        spriteBatch.end();
    }

    private void renderText(SpriteBatch spriteBatch) {
        // Text to render
        GlyphLayout layout = new GlyphLayout(textFont, name);
        float textWidth = layout.width;
        float textHeight = layout.height;

        // Center the text inside the rectangle
        float textX = dialogBox.x + dialogBox.width / 2 - textWidth / 2;
        float textY = dialogBox.y + dialogBox.height / 2 + textHeight / 2;

        textFont.draw(spriteBatch, name, textX, textY);

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
        spriteBatch.dispose();
        textFont.dispose();
        person.dispose();

    }
}
