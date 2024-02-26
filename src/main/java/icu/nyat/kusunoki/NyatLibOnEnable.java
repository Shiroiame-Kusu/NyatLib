package icu.nyat.kusunoki;

import icu.nyat.kusunoki.utils.HttpUtil;
import icu.nyat.kusunoki.utils.NyatLibLogger;

import java.io.InputStream;

import static icu.nyat.kusunoki.utils.NyatLibYAMLPraser.getStringByInputStream;
import static org.bukkit.Bukkit.getServer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class NyatLibOnEnable {
    public static final Plugin plugin = NyatLib.getPlugin(NyatLib.class);
    private final String author = plugin.getDescription().getVersion();
    private final String website = plugin.getDescription().getWebsite();
    public static String SubMCVersion;

    public boolean isProtocolLibInstalled;
    NyatLibLogger Logger = new NyatLibLogger();
    public void Fetch(){
        Logger.logINFO("§3Powered By " + author);
        Logger.logINFO("§3Website: " + website);
        isProtocolLibInstalled = getServer().getPluginManager().isPluginEnabled("ProtocolLib");
        if(isProtocolLibInstalled){
            String ProtocolLibVersionDescription = getServer().getPluginManager().getPlugin("ProtocolLib").toString();
            String ProtocolLibVersion = ProtocolLibVersionDescription.substring(ProtocolLibVersionDescription.length() - 5);
            if(ProtocolLibVersion.equals("5.2.0")){
                Logger.logINFO("Seems that you have installed correct ProtocolLib version, continue...");
            }else{
                Logger.logWARN("You are using an untested ProtocolLib version!");
            }
            //InputStream subversion = plugin.getResource("subversion.yml");
            //String SubMCVersion = getStringByInputStream(subversion);

            Logger.logINFO("Check for network access......");
            Thread FetchHitokoto = new Thread(() -> {
                String Hitokoto = new HttpUtil().get("https://v1.hitokoto.cn/?encode=text&charset=utf-8&max_length=20");
                if(Hitokoto == null){
                    Logger.logWARN("Cannot connect to Internet, Please check your network connection!");
                }else{
                    Logger.logINFO("Daily Saying from Hitokoto: " + Hitokoto);
                }
            });

            Logger.logINFO("§3Current NyatWork Version is: " + NyatLib.BrandSubVersion);

        }else{
            plugin.onDisable();
        }

    }
}
