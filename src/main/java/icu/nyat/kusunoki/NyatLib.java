package icu.nyat.kusunoki;

import icu.nyat.kusunoki.utils.NyatLibLogger;
import icu.nyat.kusunoki.utils.ReloadCmd;
import icu.nyat.kusunoki.packet.*;
import icu.nyat.kusunoki.packet.PlayerListener;
import icu.nyat.kusunoki.motd.PingEventPaper;
import icu.nyat.kusunoki.motd.PingEventSpigot;
import io.papermc.lib.PaperLib;
import org.bukkit.plugin.java.JavaPlugin;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import java.util.Collections;

public final class NyatLib extends JavaPlugin {
    private static NyatLib Main;
    private String version = this.getDescription().getVersion().toString();
    private NyatLibCore brandUpdater;

    @Override
    public void onLoad(){
        NyatLibLogger Logger = new NyatLibLogger();
        NyatLibInit LibInit = new NyatLibInit();
        try {
            LibInit.initial();
            Logger.logLoader("NyatLib Version:" + version);
        }catch (Exception ex){
            NyatLibLogger.logERROR(ex.toString());
        }


   }

   @Override
    public void onEnable(){
       NyatLibLogger Logger = new NyatLibLogger();
       Main = this;
       NyatLibOnEnable ResourceFetch = new NyatLibOnEnable();
       try {
           ResourceFetch.Fetch();
       }catch(Exception e){
           Logger.logERROR(e.toString());
           onDisable();
       }
       ProtocolManager manager = ProtocolLibrary.getProtocolManager();

       try{
           brandUpdater = new NyatLibCore(this, Collections.singletonList("§bNyatwork §d" + ResourceFetch.SubMCVersion + "§f"),100,manager);
       } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
           NyatLibLogger.logERROR(e.getMessage());
           onDisable();
           return;
       }

       manager.addPacketListener(new PacketListener(this, brandUpdater));
       this.getCommand("nlreload").setExecutor(new ReloadCmd(this));

       new PlayerListener(this, this.brandUpdater).register();
       NyatLib plugin = NyatLib.getPlugin(NyatLib.class);
       if (PaperLib.isPaper()) {
           getServer().getPluginManager().registerEvents(new PingEventPaper(plugin), this);
       } else {
           PaperLib.suggestPaper(this);
           getServer().getPluginManager().registerEvents(new PingEventSpigot(plugin), this);
       }


   }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        brandUpdater.stop();
        NyatLibOnDisable.DisableStep();
    }
}
