package icu.nyat.kusunoki;

import icu.nyat.kusunoki.utils.NyatLibLogger;
import icu.nyat.kusunoki.utils.ReloadCmd;
import icu.nyat.kusunoki.packet.*;
import icu.nyat.kusunoki.utils.HttpUtil;
import icu.nyat.kusunoki.packet.PlayerListener;
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
import static org.bukkit.Bukkit.getServer;

public final class NyatLib extends JavaPlugin {

    private String version = this.getDescription().getVersion().toString();
    //public String author = this.getDescription().getAuthors().toString();
    //public String website = this.getDescription().getWebsite().toString();
    private NyatLibCore brandUpdater;
    //public boolean isProtocolLibInstalled;
    NyatLibLogger Logger = new NyatLibLogger();
    NyatLibInit init = new NyatLibInit();
    NyatLib Main = new NyatLib();
    @Override
    public void onLoad(){
        init.init();
        Logger.logLoader("NyatLib Version:" + version);


   }

   @Override
    public void onEnable(){
       Main = this;
       NyatLibOnEnableLog ResourceFetch = new NyatLibOnEnableLog();
       try {
           ResourceFetch.Fetch();
       }catch(Exception e){
           Logger.logERROR(e.toString());
           onDisable();
       }
       /*Logger.logINFO("§3Powered By " + author);
       Logger.logINFO("§3Website: " + website);
       isProtocolLibInstalled = getServer().getPluginManager().isPluginEnabled("ProtocolLib");
       InputStream subversion = getResource("subversion.yml");
       String SubMCVersion = getStringByInputStream(subversion);
       //String SubMCVersion = NyatLibYAMLPraser.getYmlValue(subversionpath,"subversion");
       //Yaml yaml = new Yaml();
       //String SubMCVersion = yaml.load(getClass().getClassLoader().getResourceAsStream("subversion.yml"));
       Logger.logINFO("Seems that you have installed ProtocolLib, continue...");
       Logger.logINFO("Check for network access......");
       String Hitokoto = new HttpUtil().GetHttpResponseBody("https://v1.hitokoto.cn/?encode=text&charset=utf-8&max_length=20");
       Logger.logINFO("Daily Saying from Hitokoto: " + Hitokoto);
       Logger.logINFO("§3Current NyatWork Version is: " + SubMCVersion);*/
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



   }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        brandUpdater.stop();
    }
}
