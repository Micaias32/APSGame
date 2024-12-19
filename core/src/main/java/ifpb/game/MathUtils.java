package ifpb.game;

public class MathUtils {

    public static float blinkingFunction(float timePassed) {
        if (timePassed < 0 | timePassed > .5f) {
            return 0;
        }
        return (float) Math.pow(-4 * timePassed, 2)+ 1;
    }
}
