package dev.shiro8613.stamprallyplugin.map;

import dev.shiro8613.stamprallyplugin.database.Database;
import dev.shiro8613.stamprallyplugin.memory.DataStore;
import dev.shiro8613.stamprallyplugin.utils.json.StampData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.util.List;

public class MapManager {

    public static ItemStack CreateItem(World world) {
        MapView mapView = Bukkit.createMap(world);
        String data = StampData.EncodeStamps(StampData.getEmptyMap());
        Database.getInstance().setMapStamp(mapView.getId(), data);
        DataStore.LoadMapStampData();

        ItemStack itemStack = new ItemStack(Material.FILLED_MAP);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("スタンプラリーカード", NamedTextColor.AQUA));
        meta.lore(List.of(Component.text("各地をまわってスタンプを集め、「", NamedTextColor.GRAY)
                .append(Component.text("景品", NamedTextColor.GOLD))
                .append(Component.text("」を手に入れよう", NamedTextColor.GRAY))));

        itemStack.setItemMeta(meta);
        MapMeta mapMeta = (MapMeta) itemStack.getItemMeta();

        CustomMapRenderer.ReloadRenderer(mapView.getId());

        mapMeta.setMapView(mapView);
        itemStack.setItemMeta(mapMeta);

        return itemStack;
    }

    public static ItemStack GetItem(int mapId) {
        MapView mapView = Bukkit.getMap(mapId);

        if (mapView != null) {
            ItemStack itemStack = new ItemStack(Material.FILLED_MAP);
            MapMeta mapMeta = (MapMeta) itemStack.getItemMeta();

            CustomMapRenderer.ReloadRenderer(mapView.getId());

            mapMeta.setMapView(mapView);
            itemStack.setItemMeta(mapMeta);

            return itemStack;
        } else {
            return null;
        }
    }
}
