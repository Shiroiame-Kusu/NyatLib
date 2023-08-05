package icu.nyat.kusunoki;

import icu.nyat.kusunoki.utils.NyatLibLogger;

public class NyatLibOnDisable {
    public static void DisableStep(){
        NyatLibLogger Logger = new NyatLibLogger();
        Logger.logINFO("Goodbye!");
    }
}
