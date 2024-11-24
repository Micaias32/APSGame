package ifpb.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import ifpb.game.utils.Movement;

public class Person implements Disposable {
    String name;
    Sprite sprite;
    float animationTime = 0;
    float position = 0;

    public Person(String name, Sprite sprite) {
        this.name = name;
        this.sprite = sprite;
    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
    }

    public void update(float delta) {
        animationTime += delta;
        this.position = Movement.jumpDisplacement(animationTime);
    }

    public void render(SpriteBatch spriteBatch) {
        sprite.draw(spriteBatch);
    }

    public void littleJump() {
    }
}
