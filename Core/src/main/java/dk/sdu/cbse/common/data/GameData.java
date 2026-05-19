package dk.sdu.cbse.common.data;

public class GameData {
    private static final int ASTEROIDS_PER_LEVEL = 10;
    private static final int MAX_LEVEL = 3;
    private static final int MAX_PLAYER_HEALTH = 3;
    private static final double HEALTH_REGEN_INTERVAL = 1.5;

    // Screen size.
    private int displayWidth;
    private int displayHeight;
    // Time between frames (seconds).
    private double delta;
    // Keyboard state.
    private final GameKeys keys = new GameKeys();
    // Progression state.
    private int score;
    private int destroyedAsteroids;
    private double healthRegenTimer;


    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public void setDisplayHeight(int displayHeight) {
        this.displayHeight = displayHeight;
    }

    public double getDelta() {
        return delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public GameKeys getKeys() {
        return keys;
    }

    public int getScore() {
        return score;
    }

    public int getDestroyedAsteroids() {
        return destroyedAsteroids;
    }

    public int getLevel() {
        return Math.min(MAX_LEVEL, (destroyedAsteroids / ASTEROIDS_PER_LEVEL) + 1);
    }

    public int getMaxLevel() {
        return MAX_LEVEL;
    }

    public boolean isGameWon() {
        return destroyedAsteroids >= ASTEROIDS_PER_LEVEL * MAX_LEVEL;
    }

    public void registerAsteroidDestroyed() {
        destroyedAsteroids++;
        score += 100;
    }

    public int getMaxPlayerHealth() {
        return MAX_PLAYER_HEALTH;
    }

    public double getHealthRegenInterval() {
        return HEALTH_REGEN_INTERVAL;
    }

    public double getHealthRegenTimer() {
        return healthRegenTimer;
    }

    public void setHealthRegenTimer(double healthRegenTimer) {
        this.healthRegenTimer = healthRegenTimer;
    }

    public void resetRunState() {
        delta = 0;
        score = 0;
        destroyedAsteroids = 0;
        healthRegenTimer = 0;
        keys.reset();
    }
}
