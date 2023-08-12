package dev.shiro8613.stamprallyplugin;

import dev.shiro8613.stamprallyplugin.database.Database;
import dev.shiro8613.stamprallyplugin.database.entry.StampLocation;
import dev.shiro8613.stamprallyplugin.map.CustomMapRenderer;
import dev.shiro8613.stamprallyplugin.memory.DataStore;
import dev.shiro8613.stamprallyplugin.utils.DistanceLocation;
import dev.shiro8613.stamprallyplugin.utils.HandItem;
import dev.shiro8613.stamprallyplugin.utils.NearLocation;
import dev.shiro8613.stamprallyplugin.utils.json.StampData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class Event implements Listener {

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (event.isSneaking()) return;
        if (player.isFlying()) return;
        PlayerInventory inventory = player.getInventory();

        int mapId = HandItem.getMapId(inventory);
        if(!DataStore.getMapStamp().containsKey(mapId)) return;

        JavaPlugin plugin = StampRallyPlugin.getInstance();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            StampLocation nearStampLocation = NearLocation.Calc(DataStore.getLocations(), player.getLocation());
            if (nearStampLocation == null) return;

            int radius = DataStore.getConfig().radius;
            DistanceLocation.CalcRun(radius, nearStampLocation, player.getLocation(), (radius + 7), () -> {
                String data = DataStore.getMapStamp().get(mapId);
                if (data == null) return;

                Map<Integer, Boolean> map = StampData.DecodeStamps(data);
                if (map != null && map.containsKey(nearStampLocation.StampId) && !map.get(nearStampLocation.StampId)) {
                    map.put(nearStampLocation.StampId, true);

                    if (Database.getInstance().setMapStamp(mapId, StampData.EncodeStamps(map))) {
                        DataStore.LoadMapStampData();
                        CustomMapRenderer.ReloadRenderer(mapId);
                    }
                }
            }, () -> player.sendMessage(Component.text("取得可能範囲中心より", NamedTextColor.WHITE)
                    .append(Component.text("「" + (radius + 7) + "ブロック」", NamedTextColor.YELLOW))
                    .append(Component.text("の範囲に入っています。もう少し近づいて再度お試しください。", NamedTextColor.WHITE))));

        });
    }
}
