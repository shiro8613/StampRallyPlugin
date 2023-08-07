package dev.shiro8613.stamprallyplugin.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.shiro8613.stamprallyplugin.StampRallyPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StampData {

    public static Map<String, Boolean> DecodeStamps(String str) {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<HashMap<String, Boolean>> reference = new TypeReference<HashMap<String, Boolean>>() {};
        try {
            return objectMapper.readValue(str, reference);
        } catch (JsonProcessingException e) {
            StampRallyPlugin.getInstance().getLogger().warning(e.getMessage());
            return null;
        }
    }

    public static String EncodeStamps(Map<String, Boolean> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            StampRallyPlugin.getInstance().getLogger().warning(e.getMessage());
            return null;
        }
    }

    public static Map<String, Boolean> getEmptyMap() {
        Map<String, Boolean> map = new HashMap<>();
        getKeyNames().forEach(k -> map.put(k, false));
        return map;
    }

    public static List<String> getKeyNames() {
        return Arrays.asList("stamp1", "stamp2", "stamp3", "stamp4",
                "stamp5", "stamp6", "stamp7", "stamp8", "stamp9");
    }
}
