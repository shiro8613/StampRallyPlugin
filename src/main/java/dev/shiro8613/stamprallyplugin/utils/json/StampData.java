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

    public static Map<Integer, Boolean> DecodeStamps(String str) {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<HashMap<Integer, Boolean>> reference = new TypeReference<>() {};
        try {
            return objectMapper.readValue(str, reference);
        } catch (JsonProcessingException e) {
            StampRallyPlugin.getInstance().getLogger().warning(e.getMessage());
            return null;
        }
    }

    public static String EncodeStamps(Map<Integer, Boolean> map) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            StampRallyPlugin.getInstance().getLogger().warning(e.getMessage());
            return null;
        }
    }

    public static Map<Integer, Boolean> getEmptyMap() {
        Map<Integer, Boolean> map = new HashMap<>();
        getKeyNames().forEach(k -> map.put(k, false));
        return map;
    }

    public static List<Integer> getKeyNames() {
        return Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
    }
}
