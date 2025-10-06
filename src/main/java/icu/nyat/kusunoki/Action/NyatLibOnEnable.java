package icu.nyat.kusunoki.Action;

import icu.nyat.kusunoki.NyatLib;
import icu.nyat.kusunoki.Utils.HttpUtil;
import icu.nyat.kusunoki.Utils.NyatLibLogger;
import static org.bukkit.Bukkit.getServer;
import org.bukkit.plugin.Plugin;

public class NyatLibOnEnable {
    public static final Plugin plugin = NyatLib.getPlugin(NyatLib.class);
    private final String author = plugin.getDescription().getAuthors().get(0);
    private final String website = plugin.getDescription().getWebsite();
    public static String SubMCVersion;
    public boolean isProtocolLibInstalled;
    NyatLibLogger Logger = new  NyatLibLogger();
    public void Check(){
        Logger.logINFO("§3Powered By " + author);
        Logger.logINFO("§3Website: " + website);
        isProtocolLibInstalled = getServer().getPluginManager().isPluginEnabled("ProtocolLib");
        if(isProtocolLibInstalled){
            Plugin pl = getServer().getPluginManager().getPlugin("ProtocolLib");
            String protocolLibVersion = pl != null ? pl.getDescription().getVersion() : "unknown";
            if(protocolLibVersion.startsWith("5.3.") || protocolLibVersion.startsWith("5.4.")){
                Logger.logINFO("ProtocolLib version " + protocolLibVersion + " detected; continuing...");
            }else{
                Logger.logWARN("You are using an untested ProtocolLib version: " + protocolLibVersion);
            }
            Logger.logINFO("Check for network access......");
            try{
                String Hitokoto = new HttpUtil().Fetch("https://v1.hitokoto.cn/?encode=text&charset=utf-8&max_length=20");
                Logger.logINFO("Daily Saying from Hitokoto: " + Hitokoto);
            }catch (Exception ignored){
                Logger.logWARN("Cannot connect to Internet, Please check your network connection!");
            }

        }else{
            Logger.logWARN("ProtocolLib is not installed; certain features will be disabled.");
        }

    }
}
