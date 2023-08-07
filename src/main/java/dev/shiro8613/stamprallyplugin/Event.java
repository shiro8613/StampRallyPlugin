package dev.shiro8613.stamprallyplugin;

import dev.shiro8613.stamprallyplugin.database.Database;
import dev.shiro8613.stamprallyplugin.database.entry.StampLocation;
import dev.shiro8613.stamprallyplugin.map.CustomMapRenderer;
import dev.shiro8613.stamprallyplugin.memory.DataStore;
import dev.shiro8613.stamprallyplugin.utils.DistanceLocation;
import dev.shiro8613.stamprallyplugin.utils.NearLocation;
import dev.shiro8613.stamprallyplugin.utils.json.StampData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.MapMeta;

import java.util.Map;

public class Event implements Listener {

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();

        int focusItemSlot = inventory.getHeldItemSlot();
        ItemStack focusItem = inventory.getItem(focusItemSlot);

        if (focusItem == null || !focusItem.getType().equals(Material.FILLED_MAP)) return;

        MapMeta meta = (MapMeta) focusItem.getItemMeta();

        if (meta == null) return;

        int mapId = meta.getMapId();

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

            Database.getInstance().setMapStamp(mapId, StampData.EncodeStamps(map));
            DataStore.LoadMapStampData();
            CustomMapRenderer.ReloadRenderer(mapId);
        });
    }

}
