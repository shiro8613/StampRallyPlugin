package dev.shiro8613.stamprallyplugin;

import dev.shiro8613.stamprallyplugin.database.Database;
import dev.shiro8613.stamprallyplugin.memory.DataStore;
import dev.shiro8613.stamprallyplugin.utils.DetectItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.*;

public class BackGround {
    private static List<HasPlayer> playerMap;

    public static void Init(JavaPlugin plugin) {
        playerMap = new ArrayList<>();
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
                playerMap.clear();
                for(Player player : plugin.getServer().getOnlinePlayers()) {
                    PlayerInventory inventory = player.getInventory();
                    DetectItem.StackId stackId = DetectItem.search(inventory);
                    if(stackId == null) break;
                    if (DataStore.getMapStamp().containsKey(stackId.mapId)) {
                        playerMap.add(new HasPlayer(player, stackId.mapId));
                    }
                }
        }, 0, 20);

        plugin.getServer().getScheduler().runTaskTimer(plugin, () ->{
            try {
                Database database = StampRallyPlugin.getDatabase();
                if (database.getConn() == null || !database.getConn().isClosed()) {
                    database.openConnection();
                    DataStore.ReloadDataBase(database.getConn());
                    DataStore.LoadMapStampData();
                    DataStore.LoadLocationsData();
                    plugin.getLogger().info("Database is reconnected");
                }
            } catch (SQLException | ClassNotFoundException e) {
                plugin.getLogger().warning("");
            }
        }, 0, 20 * 30);
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
