package icu.nyat.kusunoki.Utils;

import icu.nyat.kusunoki.NyatLib;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public class ConfigReader {
    public static void Read(FileConfiguration config){
        NyatLib.BrandName = config.getString("default.ServerName");
        NyatLib.BrandVersion = config.getString("default.ServerVersion");
        NyatLib.BrandProtocolVersion = (int)config.get("default.ServerProtocolVersion");
        NyatLib.ServerSupportedProtocolVersion = (ArrayList<Integer>) config.get("default.ServerSupportedProtocolVersion");
        NyatLib.isBroadcastEnabled = config.getBoolean("default.EnableBroadcast");
    }
}
