package dev.shiro8613.stamprallyplugin.utils;

import dev.shiro8613.stamprallyplugin.database.entry.StampLocation;
import org.bukkit.Location;

import java.util.*;

public class NearLocation {
    public static StampLocation Calc(List<StampLocation> l1, Location l2) {
        List<StampLocation> list = l1.stream()
                .filter(l -> l.WorldName.equals(l2.getWorld().getName()))
                .toList();

        double bef = 0;
        StampLocation ret = null;

        for (int i = 0; i < list.size(); i++) {
            StampLocation l = list.get(i);
            double d = (
                    Math.pow((l2.getBlockX() - l.PosX), 2) +
                            Math.pow((l2.getBlockY() - l.PosY), 2) +
                            Math.pow((l2.getBlockZ() - l.PosZ), 2)
            );

            if (i == 0) {
                bef = d;
                ret = l;
            } else {
                if (bef >= d) {
                    bef = d;
                    ret = l;
                }
            }
        }

        return ret;
    }
}
