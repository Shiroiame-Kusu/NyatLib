package icu.nyat.kusunoki.Utils;

import icu.nyat.kusunoki.NyatLib;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public class ConfigReader {
    public static void Read(FileConfiguration config){
        NyatLib.BrandName = config.getString("default.ServerName", "§bNyatwork");
        NyatLib.BrandVersion = config.getString("default.ServerVersion", "");
        NyatLib.BrandProtocolVersion = config.getInt("default.ServerProtocolVersion", -1);
        NyatLib.ServerSupportedProtocolVersion = new ArrayList<>(config.getIntegerList("default.ServerSupportedProtocolVersion"));
        NyatLib.isBroadcastEnabled = config.getBoolean("default.EnableBroadcast", true);
    }
}
