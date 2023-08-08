package dev.shiro8613.stamprallyplugin.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.MapMeta;

public class HandItem {
    public static int getMapId(PlayerInventory inventory) {
        int focusItemSlot = inventory.getHeldItemSlot();
        ItemStack focusItem = inventory.getItem(focusItemSlot);

        if (focusItem == null || !focusItem.getType().equals(Material.FILLED_MAP)) return 0;

        MapMeta meta = (MapMeta) focusItem.getItemMeta();

        if (meta == null) return 0;

        return meta.getMapId();
    }
}
