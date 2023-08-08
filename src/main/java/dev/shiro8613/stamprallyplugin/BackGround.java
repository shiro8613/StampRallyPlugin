package dev.shiro8613.stamprallyplugin;

import dev.shiro8613.stamprallyplugin.memory.DataStore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class BackGround {
    private static List<HasPlayer> playerMap;

    public static void Init(JavaPlugin plugin) {
        playerMap = new ArrayList<>();
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
                playerMap.clear();
                for(Player player : plugin.getServer().getOnlinePlayers()) {
                    PlayerInventory inventory = player.getInventory();
                    if (!inventory.contains(Material.FILLED_MAP)) break;
                    Arrays.stream(inventory.getStorageContents())
                            .filter(Objects::nonNull)
                            .filter(itemStack -> itemStack.getType().equals(Material.FILLED_MAP))
                            .forEach(itemStack -> {
                                MapMeta mapMeta = (MapMeta) itemStack.getItemMeta();
                                if (mapMeta == null) return;
                                int mapId = mapMeta.getMapId();
                                if (DataStore.getMapStamp().containsKey(mapId)) {
                                    playerMap.add(new HasPlayer(player, mapId));
                                }
                            });
                }
        }, 0, 20);
    }

    public static List<HasPlayer> getHasPlayerMap() {
        return playerMap;
    }

    public static class HasPlayer {
        public Player player;
        public int mapId;

        public HasPlayer(Player player, int mapId) {
            this.player = player;
            this.mapId = mapId;
        }
    }
}
