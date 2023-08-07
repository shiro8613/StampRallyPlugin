package dev.shiro8613.stamprallyplugin.utils;

import dev.shiro8613.stamprallyplugin.database.entry.StampLocation;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearLocation {
    public static StampLocation Calc(List<StampLocation> l1, Location l2) {
        List<StampLocation> list = l1.stream()
                .filter(l -> l.WorldName.equals(l2.getWorld().getName()))
                .toList();

        Map<Double, StampLocation> map = new HashMap<>();
        list.forEach(l -> {
            double d = Math.sqrt(
                            ((Math.abs(l2.getBlockX()) - Math.abs(l.PosX)) * 2) +
                            ((Math.abs(l2.getBlockY()) - Math.abs(l.PosY)) * 2) +
                            ((Math.abs(l2.getBlockZ()) - Math.abs(l.PosZ)) * 2)
            );
            map.put(d, l);
        });

        double min = map.keySet().stream().sorted().toList().get(0);

        return map.get(min);
    }
}
