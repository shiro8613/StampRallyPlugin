package dev.shiro8613.stamprallyplugin.utils;

import dev.shiro8613.stamprallyplugin.database.entry.StampLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class DistanceLocation {
    public static void CalcRun(int radius, StampLocation centerLocation, Location location, int nearest, Context ctx1, Context ctx2) {
        Location centLocation = new Location(Bukkit.getWorld(
                centerLocation.WorldName),
                centerLocation.PosX,
                centerLocation.PosY,
                centerLocation.PosZ);

        double r = Math.pow(radius, 2);
        double distance = centLocation.distanceSquared(location);

        if (distance <= r) {
            ctx1.Run();
        } else if (distance <= Math.pow(nearest, 2)) {
            ctx2.Run();
        }
    }

    public interface Context{
        void Run();
    }
}
