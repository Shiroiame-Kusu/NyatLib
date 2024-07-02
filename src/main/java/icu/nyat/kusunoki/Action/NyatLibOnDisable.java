package icu.nyat.kusunoki.Action;

import icu.nyat.kusunoki.Utils.NyatLibLogger;

import static icu.nyat.kusunoki.NyatLib.brandUpdater;

public class NyatLibOnDisable {
    public static void DisableStep(){
        if(brandUpdater != null){
            brandUpdater.stop();
        }
        NyatLibLogger.logINFO("Goodbye!");
    }
}
