package icu.nyat.kusunoki;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    private final NyatLib plugin;
    private final NyatLibCore core;

    public PlayerListener(NyatLib plugin, NyatLibCore core) {
        this.plugin = plugin;
        this.core = core;
    }

    public void register() {
        plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        core.setPlayerBrand(event.getPlayer(),"Nyatwork");
    }
}
