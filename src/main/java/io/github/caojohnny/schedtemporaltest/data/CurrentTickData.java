package io.github.caojohnny.schedtemporaltest.data;

/**
 * This is a wrapper class that contains the "current" tick
 * value relative to when this plugin was enabled; that is,
 * the tick value that starts from 0 when this plugin was
 * enabled.
 */
public class CurrentTickData {
    /**
     * The current tick value.
     */
    private long currentTick;

    /**
     * Obtains the current tick value, where 0 is the
     * time at which this plugin was enabled.
     *
     * @return the current tick number
     */
    public long getCurrentTick() {
        return this.currentTick;
    }

    /**
     * Adds 1 to the current tick value.
     *
     * <p>This should be called by a BukkitScheduler task
     * running at intervals of 1 tick.</p>
     */
    public void incrCurrentTick() {
        this.currentTick++;
    }
}
