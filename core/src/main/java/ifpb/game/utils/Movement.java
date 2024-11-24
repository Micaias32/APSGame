package ifpb.game.utils;

public class Movement {
    public static float jumpDisplacement(float f) {
        if (f <= 0 || f > 1) {
            return 0f;
        }

        if (f < .3f) {
            return -(float) Math.pow(((f - 3) * (10 / 3f)), 2) + 1;
        }

        return 0;
    }
}
