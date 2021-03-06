package io.github.caojohnny.schedtemporaltest;

import io.github.caojohnny.schedtemporaltest.data.CurrentTickData;
import io.github.caojohnny.schedtemporaltest.listener.PlayerInteractListener;
import io.github.caojohnny.schedtemporaltest.task.TickTrackingTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Plugin main class.
 */
public class SchedTemporalTest extends JavaPlugin {
    @Override
    public void onEnable() {
        CurrentTickData ctd = new CurrentTickData();

        Bukkit.getPluginManager().registerEvents(
                new PlayerInteractListener(this, ctd), this);
        Bukkit.getScheduler().runTaskTimer(
                this, new TickTrackingTask(this, ctd), 0L, 1L);
    }
}
