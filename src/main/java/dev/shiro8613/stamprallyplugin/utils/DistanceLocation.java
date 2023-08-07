package dev.shiro8613.stamprallyplugin.utils;

import org.bukkit.Location;

public class DistanceLocation {
    public static void CalcRun(int radius, Location centerLocation, Location location, Context ctx) {
        int PosX1 = centerLocation.getBlockX();
        int PosY1 = centerLocation.getBlockY();
        int PosZ1 = centerLocation.getBlockZ();

        int PosX2 = location.getBlockX();
        int PosY2 = location.getBlockY();
        int PosZ2 = location.getBlockZ();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (PosX2 == PosX1 + x && PosY2 == PosY1 + y && PosZ2 == PosZ1 + z) {
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
