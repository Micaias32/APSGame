package ifpb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import org.jetbrains.annotations.NotNull;

public class Bar implements Disposable {
	Rectangle outer, inner;
	Color color, bgColor;

    BarIcon icon;

	float value;

	public Bar(float x, float y, float width, float height, Color color, Color bgColor, float value, Sprite icon) {
        // needs to be a square so height on both
        // noinspection SuspiciousNameCombination
        this.icon = new BarIcon(icon, x, y, height, height);

        x = x+height+1;
		outer = new Rectangle(x, y, width, height);
		float inX, inY, inWidth, inHeight;
		float border = height * .05f;
		inX = x + border;
		inY = y + border;
		inWidth = width - (2*border);
		inHeight = height - (2*border);
		inner = new Rectangle(inX, inY, inWidth, inHeight);
		this.color = color;
		this.value = value;
        this.bgColor = bgColor;

	}

	void setValue(float value) {
		if (value >= 0f && value <= 1f) {
			this.value = value;
		}
	}

	public void render(@NotNull ShapeRenderer shapeRenderer, @NotNull SpriteBatch spriteBatch) {
        icon.render(spriteBatch);
		shapeRenderer.rect(outer.x, outer.y, outer.width, outer.height,
				bgColor, bgColor, bgColor, bgColor);
		shapeRenderer.rect(inner.x, inner.y, inner.width, inner.height,
				Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
		float barSize = inner.width * value;
		shapeRenderer.rect(inner.x, inner.y, barSize, inner.height,
				Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
	}

    @Override
    public void dispose() {
        icon.dispose();
    }

    // Builder Class
    public static class Builder {
        private float x = 0f;
        private float y = 0f;
        private float width = 100f;
        private float height = 20f;
        private Color color = Color.WHITE;
        private float value = 1f;
        private Color bgColor = new Color(.02f, .45f, .49f,  1f);
        private Sprite icon = new Sprite();

        public Builder withX(float x) {
            this.x = x;
            return this;
        }

        public Builder withY(float y) {
            this.y = y;
            return this;
        }

        public Builder withWidth(float width) {
            this.width = width;
            return this;
        }

        public Builder withBgColor(Color color) {
            this.bgColor = color;
            return this;
        }

        public Builder withHeight(float height) {
            this.height = height;
            return this;
        }

        public Builder withColor(Color color) {
            this.color = color;
            return this;
        }

        public Builder withValue(float value) {
            if (value >= 0f && value <= 1f) {
                this.value = value;
            }
            return this;
        }

        public Builder withIcon(Sprite icon) {
            this.icon = icon;
            return this;
        }

        public Bar build() {
            return new Bar(x, y, width, height, color, bgColor, value, icon);
        }
    }
}

class BarIcon implements Disposable {
    Sprite bg;
    Sprite fg;

    public BarIcon(@NotNull Sprite fg, float x, float y, float width, float height) {
        this.bg = new Sprite(new Texture(Gdx.files.internal("mainScreen/bgButton2.png")));
        bg.setPosition(x, y);
        bg.setSize(width, height);
        this.fg = fg;
        fg.setPosition(x, y);
        fg.setSize(width, height);
    }

    public void render(SpriteBatch spriteBatch) {
        bg.draw(spriteBatch);
        fg.draw(spriteBatch);
    }

    @Override
    public void dispose() {
        bg.getTexture().dispose();
        fg.getTexture().dispose();
    }
}
