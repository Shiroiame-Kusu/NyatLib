package icu.nyat.kusunoki.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
public class NyatLibYAMLPraser {
    public static String getStringByInputStream(InputStream inputStream){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            NyatLibLogger.logERROR(e.getMessage());
            try {
                inputStream.close();
                bufferedReader.close();
            } catch (Exception e1) {
                NyatLibLogger.logERROR(e1.getMessage());
            }
        }
        return null;
    }

    public static String getYmlValue(String ymlPath,String cronName){
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        InputStream input = null;
        try {
            input = new FileInputStream(ymlPath);
        } catch (FileNotFoundException e) {
            NyatLibLogger.logERROR(e.getMessage());
            return null;
        }
        Map<?, ?> map;
        try {
            map = objectMapper.readValue(input, Map.class);
        } catch (IOException e) {
            return null;
        }
        String[] split = cronName.split("\\.");
        Map info = new HashMap();
        String cron = "";
        for (int i = 0; i < split.length; i++) {
            if (i ==0) {
                info = (Map) map.get(split[i]);
            }else  if(i==split.length-1){
                cron = (String) info.get(split[i]);
            }else{
                info = (Map) info.get(split[i]);
            }
            if (info==null) {
                return null;
            }

        }
        return cron;
    }
    public static String getPluginYmlValue(FileInputStream ymlPath,String cronName){
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        /*try {
            input = new FileInputStream(ymlPath);
        } catch (FileNotFoundException e) {
            NyatLibLogger.logERROR(e.getMessage());
            return null;
        }*/
        Map<?, ?> map;
        try {
            map = objectMapper.readValue(ymlPath, Map.class);
        } catch (IOException e) {
            return null;
        }
        String[] split = cronName.split("\\.");
        Map info = new HashMap();
        String cron = "";
        for (int i = 0; i < split.length; i++) {
            if (i ==0) {
                info = (Map) map.get(split[i]);
            }else  if(i==split.length-1){
                cron = (String) info.get(split[i]);
            }else{
                info = (Map) info.get(split[i]);
            }
            if (info==null) {
                return null;
            }

        }
        return cron;
    }
}
