package dev.shiro8613.stamprallyplugin.memory;

import dev.shiro8613.stamprallyplugin.database.Database;
import dev.shiro8613.stamprallyplugin.database.DatabaseConn;
import dev.shiro8613.stamprallyplugin.database.entry.Config;
import dev.shiro8613.stamprallyplugin.database.entry.StampLocation;
import dev.shiro8613.stamprallyplugin.utils.json.StampData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    private static List<StampLocation> locations;
    private static Map<Integer, String> MapStamp;
    private static Map<String, Image> imageMap;
    private static DatabaseConn databaseConn;
    private static Config config;

    public static void Init() {
        locations = new ArrayList<>();
        MapStamp = new HashMap<>();
        imageMap = new HashMap<>();
        databaseConn = Database.getInstance();

        LoadLocationsData();
        LoadMapStampData();
    }

    public static void LoadLocationsData() {
        List<StampLocation> list = databaseConn.getStampLocation();
        if (list != null) {
            locations.addAll(list);
        }
    }

    public static void LoadMapStampData() {
        Map<Integer, String> map = databaseConn.getMapStamp();
        if (map != null) {
            MapStamp.putAll(map);
        }
    }

    public static void LoadImages(String[] filenames, String path) {
        if (filenames.length != 0) {

            for (String filename : filenames) {
                if(!filename.endsWith(".png")) return;

                String stampName = filename.replace(".png", "");

                if(!StampData.getKeyNames().contains(stampName) && !stampName.equals("background")) return;

                File imageFile = new File(path + File.separator + filename);
                try {
                    Image image = ImageIO.read(imageFile);
                    imageMap.put(stampName, image);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void setConfig(Config conf) {
        config = conf;
    }

    public static Config getConfig() {
        return config;
    }

    public static List<StampLocation> getLocations() {
        return locations;
    }

    public static Map<Integer, String> getMapStamp() {
        return MapStamp;
    }

    public static Map<String,Image> getImageMap() {
        return imageMap;
    }
}
