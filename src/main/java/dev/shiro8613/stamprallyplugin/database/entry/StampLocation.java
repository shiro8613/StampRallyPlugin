package dev.shiro8613.stamprallyplugin.database.entry;

public class StampLocation {
    public int StampId;
    public String WorldName;
    public int PosX;
    public int PosY;
    public int PosZ;

    public StampLocation(int StampId, String WorldName, int PosX, int PosY, int PosZ) {
        this.StampId = StampId;
        this.WorldName = WorldName;
        this.PosX = PosX;
        this.PosY = PosY;
        this.PosZ = PosZ;
    }
}
