package dev.shiro8613.stamprallyplugin.item;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;



public class Gift {
    private static ItemStack item1;
    private static ItemStack item2;
    private static ItemStack item3;
    private static ItemStack item4;

    public static void Init() {
        item1 = tiisarakarada();
        item2 = tiisaraashi();
        item3 = tiisarahead();
        item4 = osaisen();
    }

    private static ItemStack osaisen() {
        ItemStack itemStack = new ItemStack(Material.RAW_COPPER);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("お賽銭 "));
        itemStack.setItemMeta(meta);
        itemStack.setAmount(64 * 2);
        return itemStack;
    }

    private static ItemStack tiisaraashi() {
        ItemStack itemStack = new ItemStack(Material.NETHERITE_LEGGINGS);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("ちーさらなりきりセット（足）", NamedTextColor.LIGHT_PURPLE));
        itemStack.setItemMeta(meta);

        return itemStack;
    }

    private static ItemStack tiisarakarada() {
        ItemStack itemStack = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("ちーさらなりきりセット（胴体）", NamedTextColor.LIGHT_PURPLE));
        itemStack.setItemMeta(meta);

        return itemStack;
    }

    private static ItemStack tiisarahead() {
        ItemStack itemStack = new ItemStack(Material.NETHERITE_HELMET);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("ちーさらなりきりセット（頭）", NamedTextColor.LIGHT_PURPLE));

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public static void Give(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.setHelmet(item3);
        inventory.setLeggings(item2);
        inventory.setChestplate(item1);
        inventory.addItem(item4);
    }
}
