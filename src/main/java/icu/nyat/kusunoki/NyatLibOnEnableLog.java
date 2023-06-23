package icu.nyat.kusunoki;

import icu.nyat.kusunoki.NyatLib;
import icu.nyat.kusunoki.utils.HttpUtil;
import icu.nyat.kusunoki.utils.NyatLibLogger;

import java.io.InputStream;

import static icu.nyat.kusunoki.utils.NyatLibYAMLPraser.getStringByInputStream;
import static org.bukkit.Bukkit.getServer;
import org.bukkit.plugin.java.JavaPlugin;

public class NyatLibOnEnableLog {
    NyatLib nyatlib = new NyatLib();
    private String author = nyatlib.getDescription().getVersion();
    private String website = nyatlib.getDescription().getWebsite();
    public String SubMCVersion;

    public boolean isProtocolLibInstalled;
    NyatLibLogger Logger = new NyatLibLogger();
    public void Fetch(){
        Logger.logINFO("§3Powered By " + author);
        Logger.logINFO("§3Website: " + website);
        isProtocolLibInstalled = getServer().getPluginManager().isPluginEnabled("ProtocolLib");
        InputStream subversion = nyatlib.getResource("subversion.yml");
        String SubMCVersion = getStringByInputStream(subversion);
        //String SubMCVersion = NyatLibYAMLPraser.getYmlValue(subversionpath,"subversion");
        //Yaml yaml = new Yaml();
        //String SubMCVersion = yaml.load(getClass().getClassLoader().getResourceAsStream("subversion.yml"));
        Logger.logINFO("Seems that you have installed ProtocolLib, continue...");
        Logger.logINFO("Check for network access......");
        String Hitokoto = new HttpUtil().GetHttpResponseBody("https://v1.hitokoto.cn/?encode=text&charset=utf-8&max_length=20");
        Logger.logINFO("Daily Saying from Hitokoto: " + Hitokoto);
        Logger.logINFO("§3Current NyatWork Version is: " + SubMCVersion);
    }
}
