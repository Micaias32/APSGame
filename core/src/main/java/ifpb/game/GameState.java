package ifpb.game;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;

public class GameState {

    public static String statePath;
    public static long timeSinceUpdate = 0;
    public static boolean isLightOn = true, sleeping = false;
    public static HoldingState holdingState;
    private static float
        health = 0f,
        hunger = 0f,
        energy = 0f,
        happiness = 0f;

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


    public static void save() {
        File file = new File(statePath);
        FileWriter writer;
        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HashMap<String, String> data = new HashMap<>();

        data.put("energy", String.valueOf(energy));
        data.put("health", String.valueOf(health));
        data.put("hunger", String.valueOf(hunger));
        data.put("happiness", String.valueOf(happiness));
        data.put("light", String.valueOf(isLightOn));
        data.put("sleeping", String.valueOf(sleeping));
        data.put("time", String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(-3))));

        data.forEach((key, value) -> {
            try {
                writer.write(String.format("%s,%s\n", key, value));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void incrementTime() {
        timeSinceUpdate += 1;
    }

    public static void updateTime() {
        while (timeSinceUpdate >= 60) {
            if (sleeping) {
                setEnergy(energy + .1f);
            } else {
                setEnergy(energy - .01f);
            }
            if (energy == 1) {
                sleeping = false;
            }
            if (energy == 0) {
                setHealth(health - .1f);
            }
            setHunger(hunger - .05f);
            setHappiness(happiness - .01f);

            timeSinceUpdate -= 60;
        }
    }
}
