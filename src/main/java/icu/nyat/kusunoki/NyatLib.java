package icu.nyat.kusunoki;

import icu.nyat.kusunoki.utils.NyatLibLogger;
import icu.nyat.kusunoki.utils.ReloadCmd;
import icu.nyat.kusunoki.packet.*;
import icu.nyat.kusunoki.utils.HttpUtil;
import icu.nyat.kusunoki.packet.PlayerListener;
import icu.nyat.kusunoki.utils.NyatLibYAMLPraser;
import icu.nyat.kusunoki.motd.PingEventPaper;
import icu.nyat.kusunoki.motd.PingEventSpigot;
import io.papermc.lib.PaperLib;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Collections;
import java.util.Objects;

import static icu.nyat.kusunoki.utils.NyatLibYAMLPraser.*;
import static org.bukkit.Bukkit.getServer;

public final class NyatLib extends JavaPlugin {
    //public static final NyatLib plugin = NyatLib.getPlugin(NyatLib.class);
    private static NyatLib Main;
    private String version = this.getDescription().getVersion().toString();
    //public String author = this.getDescription().getAuthors().toString();
    //public String website = this.getDescription().getWebsite().toString();
    private NyatLibCore brandUpdater;
    //public boolean isProtocolLibInstalled;


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
       NyatLibOnEnableLog ResourceFetch = new NyatLibOnEnableLog();
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
    }
}
