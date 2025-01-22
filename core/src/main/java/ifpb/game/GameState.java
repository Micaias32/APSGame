package ifpb.game;


public class GameState {

    private static float
        health = 0f,
        hunger = 0f,
        energy = 0f,
        happiness = 0f;

    public static boolean isLightOn = true, sleeping = false;

    public static HoldingState holdingState;

    public static float getHealth() {
        return health;
    }

    public static void setHealth(float health) {
        if (health < 0) {
            GameState.health = 0;
            return;
        }
        if (health > 1) {
            GameState.health = 1;
            return;
        }
        GameState.health = health;
    }

    public static float getEnergy() {
        return energy;
    }

    public static void setEnergy(float energy) {
        if (energy < 0) {
            GameState.energy = 0;
            return;
        }
        if (energy > 1) {
            GameState.energy = 1;
            return;
        }
        GameState.energy = energy;
    }

    public static float getHappiness() {
        return happiness;
    }

    public static void setHappiness(float happiness) {
        if (happiness < 0) {
            GameState.happiness = 0;
            return;
        }
        if (happiness > 1) {
            GameState.happiness = 1;
            return;
        }
        GameState.happiness = happiness;
    }

    public static float getHunger() {
        return hunger;
    }

    public static void setHunger(float hunger) {
        if (hunger < 0) {
            GameState.hunger = 0;
            return;
        }
        if (hunger > 1) {
            GameState.hunger = 1;
            return;
        }
        GameState.hunger = hunger;
    }


}
