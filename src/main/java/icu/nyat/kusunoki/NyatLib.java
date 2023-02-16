package icu.nyat.kusunoki;

import icu.nyat.kusunoki.utils.NyatLibLogger;
import icu.nyat.kusunoki.utils.ReloadCmd;
import icu.nyat.kusunoki.packet.*;
import icu.nyat.kusunoki.utils.NyatLibYAMLPraser;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import java.util.Collections;

public final class NyatLib extends JavaPlugin {
    private static NyatLib instance;
    //Plugin plugin = NyatLib.getPlugin(NyatLib.class);
    String version = this.getDescription().getVersion().toString();
    String author = this.getDescription().getAuthors().toString();
    String website = this.getDescription().getWebsite().toString();
    //String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    //public NyatLibLogger logger;

    private NyatLibCore brandUpdater;
    public static final String BRAND = "minecraft:brand";
    public boolean isProtocolLibInstalled;
    public String SubMCVersion;
    //private NyatLibCore brandUpdater;
    @Override
    public void onLoad(){
        //NyatLibStringInit = new NyatLibStringInit();
        NyatLibLogger.logLoader("NyatLib Version:" + version);

    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        NyatLibInit.init();
        NyatLibLogger.logINFO("&3Powered By " + author);
        NyatLibLogger.logINFO("Website: " + website);
        isProtocolLibInstalled = getServer().getPluginManager().getPlugin("ProtocolLib") == null;
        if(isProtocolLibInstalled){
            NyatLibLogger.logWARN("Seems that you did not install ProtocolLib.");
            NyatLibLogger.logWARN("NyatLib will still try to start but may causes some issues.");
        }else{
            NyatLibLogger.logINFO("Seems that you have installed ProtocolLib, continue...");
        }
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        try{
            brandUpdater = new NyatLibCore(Collections.singletonList("Nyatwork" + SubMCVersion),100,manager);
        } catch (ClassNotFoundException e) {
        NyatLibLogger.logERROR(e.getMessage());
        onDisable();
        return;
        }


        manager.addPacketListener(new PacketListener(this, brandUpdater));

        this.getCommand("nlreload").setExecutor(new ReloadCmd(this));

        if (brandUpdater.size() > 0) brandUpdater.broadcast();
        if (brandUpdater.size() > 1) brandUpdater.start();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        brandUpdater.stop();
    }
}
