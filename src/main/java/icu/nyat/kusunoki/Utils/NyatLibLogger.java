package icu.nyat.kusunoki.Utils;

import icu.nyat.kusunoki.NyatLib;
import org.bukkit.plugin.Plugin;
import  java.util.logging.Level;
public class NyatLibLogger {
    private static Plugin plugin() {
        return NyatLib.getPlugin(NyatLib.class);
    }
    public static void logINFO(String log) {
        plugin().getLogger().log(Level.INFO, log);
    }

    public static void logWARN(String warn) {
        plugin().getLogger().log(Level.WARNING, warn);
    }

    public static void logLoader(String msg) {
        logINFO("[Loader] " + msg);
    }

    public static void logERROR(String error) { plugin().getLogger().log(Level.SEVERE, error); }


}
