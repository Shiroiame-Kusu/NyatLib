package icu.nyat.kusunoki;

import icu.nyat.kusunoki.utils.NyatLibLogger;
import icu.nyat.kusunoki.utils.ReloadCmd;
import icu.nyat.kusunoki.packet.*;
import icu.nyat.kusunoki.utils.HttpUtil;
import icu.nyat.kusunoki.utils.NyatLibYAMLPraser;
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

public final class NyatLib extends JavaPlugin {
    private static NyatLib instance;
    //Plugin plugin = NyatLib.getPlugin(NyatLib.class);
    String version = this.getDescription().getVersion().toString();
    String author = this.getDescription().getAuthors().toString();
    String website = this.getDescription().getWebsite().toString();
    //String serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    //public NyatLibLogger logger;


    private NyatLibCore brandUpdater;
//    public static final String BRAND = "minecraft:brand";
    public boolean isProtocolLibInstalled;

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
        NyatLibLogger.logINFO("§3Powered By " + author);
        NyatLibLogger.logINFO("§3Website: " + website);
        isProtocolLibInstalled = getServer().getPluginManager().isPluginEnabled("ProtocolLib");
        InputStream subversion = getResource("subversion.yml");
        String SubMCVersion = getStringByInputStream(subversion);
        //String SubMCVersion = NyatLibYAMLPraser.getYmlValue(subversionpath,"subversion");
        //Yaml yaml = new Yaml();
        //String SubMCVersion = yaml.load(getClass().getClassLoader().getResourceAsStream("subversion.yml"));
        NyatLibLogger.logINFO("Seems that you have installed ProtocolLib, continue...");
        NyatLibLogger.logINFO("Check for network access......");
        String Hitokoto = new HttpUtil().GetHttpResponseBody("https://v1.hitokoto.cn/?encode=text&charset=utf-8&max_length=20");
        NyatLibLogger.logINFO("Daily Saying from Hitokoto: " + Hitokoto);
        NyatLibLogger.logINFO("§3Current NyatWork Version is: " + SubMCVersion);
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();

        try{
            brandUpdater = new NyatLibCore(this, Collections.singletonList("§bNyatwork §d" + SubMCVersion + "§f"),100,manager);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            NyatLibLogger.logERROR(e.getMessage());
            onDisable();
            return;
        }

        manager.addPacketListener(new PacketListener(this, brandUpdater));

        this.getCommand("nlreload").setExecutor(new ReloadCmd(this));

        new PlayerListener(this, this.brandUpdater).register();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        brandUpdater.stop();
    }
}
