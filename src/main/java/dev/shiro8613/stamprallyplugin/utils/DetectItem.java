package dev.shiro8613.stamprallyplugin.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.MapMeta;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class DetectItem {
    public static StackId search(PlayerInventory inventory) {
        if (!inventory.contains(Material.FILLED_MAP)) return null;
        AtomicReference<StackId> stackId = new AtomicReference<>();
        Arrays.stream(inventory.getStorageContents())
                .filter(Objects::nonNull)
                .filter(itemStack -> itemStack.getType().equals(Material.FILLED_MAP))
                .forEach(itemStack -> {
                    MapMeta mapMeta = (MapMeta) itemStack.getItemMeta();
                    if (mapMeta == null) return;
                    int mapId = mapMeta.getMapId();
                    stackId.set(new StackId(itemStack, mapId));
                });
        return stackId.get();
    }

    public static class StackId {
        public int mapId;
        public ItemStack item;

        public StackId(ItemStack itemStack, int mapId) {
            this.item = itemStack;
            this.mapId = mapId;
        }

    }
}
