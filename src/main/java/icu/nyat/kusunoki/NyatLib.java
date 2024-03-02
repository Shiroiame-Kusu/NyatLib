package icu.nyat.kusunoki;

import icu.nyat.kusunoki.utils.NyatLibLogger;
import icu.nyat.kusunoki.utils.ReloadCmd;
import icu.nyat.kusunoki.packet.*;
import icu.nyat.kusunoki.packet.PlayerListener;
import icu.nyat.kusunoki.motd.PingEventPaper;
import icu.nyat.kusunoki.motd.PingEventSpigot;
import io.papermc.lib.PaperLib;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public final class NyatLib extends JavaPlugin {


    private String PluginVersion = this.getDescription().getVersion();
    private NyatLibCore brandUpdater;

    public static String BrandName;
    public static String BrandVersion;
    public static int BrandProtocolVersion;
    public static ArrayList<Integer> ServerSupportedProtocolVersion;
    public static boolean EnableBoardcast;

    @Override
    public void onLoad(){
        NyatLibLogger Logger = new NyatLibLogger();
        NyatLibInit LibInit = new NyatLibInit();
        try {
            LibInit.initial();
            Logger.logLoader("NyatLib Version:" + PluginVersion);
        }catch (Exception ex){
            NyatLibLogger.logERROR(ex.toString());
        }
   }

   @Override
    public void onEnable(){
       NyatLibLogger Logger = new NyatLibLogger();
       NyatLibOnEnable ResourceFetch = new NyatLibOnEnable();
       FileConfiguration config = getConfig();
       NyatLib.BrandName = config.getString("default.ServerName");
       NyatLib.BrandVersion = config.getString("default.ServerVersion");
       NyatLib.BrandProtocolVersion = (int)config.get("default.ServerProtocolVersion");
       NyatLib.ServerSupportedProtocolVersion = (ArrayList<Integer>) config.get("default.ServerSupportedProtocolVersion");
       try {
           ResourceFetch.Fetch();
       }catch(Exception e){
           NyatLibLogger.logERROR(e.toString());
           onDisable();
       }
       ProtocolManager manager = ProtocolLibrary.getProtocolManager();

       try{
           if(EnableBoardcast){
               brandUpdater = new NyatLibCore(this, Collections.singletonList(BrandName + " " + BrandVersion + "§f"),100,manager);
           }
       } catch (Exception e) {
           NyatLibLogger.logERROR(e.getMessage());
           NyatLibLogger.logERROR(e.toString());
           e.printStackTrace();
           onDisable();
       }

       manager.addPacketListener(new PacketListener(this, brandUpdater));
       try {
           this.getCommand("nlreload").setExecutor(new ReloadCmd(this));
       }catch (Exception ignored){}

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
        if(brandUpdater != null){
            brandUpdater.stop();
        }
        NyatLibOnDisable.DisableStep();
    }
}
