package icu.nyat.kusunoki.utils;

import icu.nyat.kusunoki.NyatLib;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import  java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.event.server.*;
public class NyatLibLogger {
    //public Level.ERROR = new Level("");
    public static final Plugin plugin = NyatLib.getPlugin(NyatLib.class);
    public static void logINFO(String log) {
        plugin.getLogger().log(Level.INFO, log);
    }

    public static void logWARN(String warn) {
        plugin.getLogger().log(Level.WARNING, warn);
    }

    public static void logLoader(String msg) {
        logINFO("[Loader] " + msg);
    }

    public static void logERROR(String error) { plugin.getLogger().log(Level.SEVERE, error); }


}
