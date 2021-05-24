package io.github.caojohnny.schedtemporaltest.task;

import io.github.caojohnny.schedtemporaltest.SchedTemporalTest;
import io.github.caojohnny.schedtemporaltest.data.CurrentTickData;

import static java.lang.String.format;

/**
 * This is a task meant to be scheduled using the
 * BukkitScheduler at a 1 tick intervals. The purpose of
 * this class is to update the {@link CurrentTickData}
 * instance given through the constructor with the most
 * up-to-date tick as received by the scheduler.
 */
public class TickTrackingTask implements Runnable {
    /**
     * The default server tick rate.
     */
    private static final long TICKS_PER_SECOND = 20L;

    /**
     * The instance of the plugin main class.
     */
    private final SchedTemporalTest plugin;
    /**
     * The instance of the current tick data to be updated
     * by this task.
     */
    private final CurrentTickData ctd;

    /**
     * Constructs a new instance of this class with the
     * given parameters.
     *
     * @param plugin the instance of the plugin main class.
     * @param ctd    the tick counter data to be updated by
     *               this task
     */
    public TickTrackingTask(SchedTemporalTest plugin,
                            CurrentTickData ctd) {
        this.plugin = plugin;
        this.ctd = ctd;
    }

    /**
     * This method is called by the scheduler once per
     * tick. Each tick, it will first check if 20 ticks
     * have elapsed (roughly 1 second). If this is true,
     * a message is printed displaying the current tick
     * value. In any case, this method will increment the
     * current tick value stored in {@link #ctd} to update
     * the new tick number.
     */
    @Override
    public void run() {
        long curTick = this.ctd.getCurrentTick();

        // Logged at 1 Hz to avoid spamming the console
        if (curTick % TICKS_PER_SECOND == 0) {
            this.plugin.getLogger().info(format(
                    "Scheduler current tick = %d",
                    curTick));
        }

        this.ctd.incrCurrentTick();
    }
}
