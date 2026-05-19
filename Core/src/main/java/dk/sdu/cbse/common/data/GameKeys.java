package dk.sdu.cbse.common.data;

public class GameKeys {
    // Number of keys we track.
    private static final int NUM_KEYS = 4;
    // Key indexes.
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int SPACE = 3;

    // keys = current, pkeys = previous (last frame).
    private final boolean[] keys = new boolean[NUM_KEYS];
    private final boolean[] pkeys = new boolean[NUM_KEYS];

    public void update() {
        // Move current state into previous state.
        for (int i = 0; i < NUM_KEYS; i++) {
            pkeys[i] = keys[i];
        }
    }

    public void setKey(int k, boolean b) {
        // Turn a key on or off.
        keys[k] = b;
    }

    public boolean isDown(int k) {
        // Is the key held down?
        return keys[k];
    }

    public boolean isPressed(int k) {
        // Was the key pressed this frame?
        return keys[k] && !pkeys[k];
    }

    public void reset() {
        for (int i = 0; i < NUM_KEYS; i++) {
            keys[i] = false;
            pkeys[i] = false;
        }
    }
}
