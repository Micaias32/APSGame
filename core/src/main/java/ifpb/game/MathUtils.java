package ifpb.game;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class MathUtils {

    public static float blinkingFunction(float timePassed) {
        if (timePassed < 0 | timePassed > .5f) {
            return 0;
        }
        return (float) pow(-4 * timePassed, 2) + 1;
    }

    public static float colorFunction(float timePassed) {
        double res = -pow(timePassed / sqrt(2) - sqrt(2), 2) + 2;
        if (res < 1.0) {
            return (float) res;
        }
        return 1.0f;
    }
}
