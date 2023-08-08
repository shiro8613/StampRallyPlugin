package dev.shiro8613.stamprallyplugin;

import dev.shiro8613.stamprallyplugin.database.Database;
import dev.shiro8613.stamprallyplugin.database.entry.StampLocation;
import dev.shiro8613.stamprallyplugin.map.CustomMapRenderer;
import dev.shiro8613.stamprallyplugin.memory.DataStore;
import dev.shiro8613.stamprallyplugin.utils.DistanceLocation;
import dev.shiro8613.stamprallyplugin.utils.HandItem;
import dev.shiro8613.stamprallyplugin.utils.NearLocation;
import dev.shiro8613.stamprallyplugin.utils.json.StampData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.PlayerInventory;


import java.util.Map;

public class Event implements Listener {

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();

        int mapId = HandItem.getMapId(inventory);
        if(!DataStore.getMapStamp().containsKey(mapId)) return;

        StampLocation nearStampLocation = NearLocation.Calc(DataStore.getLocations(), player.getLocation());
        if(nearStampLocation == null) return;

        DistanceLocation.CalcRun(DataStore.getConfig().radius, nearStampLocation, player.getLocation(), () -> {
            String data = Database.getInstance().getMapStamp().get(mapId);
            if (data == null) return;

            Map<Integer, Boolean> map = StampData.DecodeStamps(data);
            if (map != null && map.containsKey(nearStampLocation.StampId)) {
                map.put(nearStampLocation.StampId, true);
            }

            if (Database.getInstance().setMapStamp(mapId, StampData.EncodeStamps(map))) {
                DataStore.LoadMapStampData();
                CustomMapRenderer.ReloadRenderer(mapId);
            }
        });
    }

}
