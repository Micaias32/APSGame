package ifpb.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bar {
	static final Color bgColor = new Color(.02f, .45f, .49f,  1f);
	Rectangle outer, inner;
	Color color;
	
	float value;
	
	public Bar(float x, float y, float width, float height, Color color, int value) {
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
		Rectangle rect = new Rectangle(x, y, width, height);
	}
	
	void setValue(float value) {
		if (value >= 0f && value <= 1f) {
			this.value = value;
		}
	}
	
	public void render(ShapeRenderer shapeRenderer) {
		shapeRenderer.rect(outer.x, outer.y, outer.width, outer.height,
				bgColor, bgColor, bgColor, bgColor);
		shapeRenderer.rect(inner.x, inner.y, inner.width, inner.height,
				Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
		float barSize = inner.width * value;
		shapeRenderer.rect(inner.x, inner.y, barSize, inner.height,
				Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
	}
}
