package io.github.caojohnny.schedtemporaltest.listener;

import io.github.caojohnny.schedtemporaltest.SchedTemporalTest;
import io.github.caojohnny.schedtemporaltest.data.CurrentTickData;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import static java.lang.String.format;

/**
 * Listens for any sort of player interaction event and
 * records the tick at which it occurs.
 */
public class PlayerInteractListener implements Listener {
    /**
     * The instance of the plugin main class.
     */
    private final SchedTemporalTest plugin;
    /**
     * The data wrapper containing the current tick value.
     */
    private final CurrentTickData ctd;

    /**
     * Constructs a new listener instance with the given
     * required parameters.
     *
     * @param plugin the instance of the plugin main class
     * @param ctd    the current tick value wrapper
     */
    public PlayerInteractListener(SchedTemporalTest plugin,
                                  CurrentTickData ctd) {
        this.plugin = plugin;
        this.ctd = ctd;
    }

    /**
     * This is the event handler method for
     * {@link PlayerInteractEvent}. Whenever the event is
     * called by the server, this method is invoked and
     * schedules a task that runs on the next tick that
     * prints both the tick counter at the time this method
     * was invoked and the tick counter at the time this
     * scheduled task runs.
     *
     * @param event the injected event instance
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        long curTick = this.ctd.getCurrentTick();
        Bukkit.getScheduler().runTask(this.plugin, () ->
                this.plugin.getLogger().info(format(
                        "Listener: %d (now = %d)",
                        curTick,
                        this.ctd.getCurrentTick())));
    }
}
