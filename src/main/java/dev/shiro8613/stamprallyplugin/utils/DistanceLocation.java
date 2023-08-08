package dev.shiro8613.stamprallyplugin.utils;

import dev.shiro8613.stamprallyplugin.database.entry.StampLocation;
import org.bukkit.Location;

public class DistanceLocation {
    public static void CalcRun(int radius, StampLocation centerLocation, Location location, Context ctx) {
        int PosX1 = centerLocation.PosX;
        int PosY1 = centerLocation.PosY;
        int PosZ1 = centerLocation.PosZ;

        int PosX2 = (int) Math.round(location.getX());
        int PosY2 = (int) Math.round(location.getY());
        int PosZ2 = (int) Math.round(location.getZ());

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if ((PosX1 + x) == PosX2 && (PosY1 + y) == PosY2 && (PosZ1 + z) == PosZ2) {
                        ctx.Run();
                    }
                }
            }
        }


    }

    public interface Context{
        void Run();
    }
}
