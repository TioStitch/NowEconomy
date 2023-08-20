package de.tiostitch.economy.utils;

import de.tiostitch.economy.Main;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ChequeBuilder {

    public static ItemStack chequeItem(Player player, Long coins) {
        Material material = Material.valueOf(Main.plugin.getConfig().getString("cheque-item.material"));
        String displayName = Main.plugin.getConfig().getString("cheque-item.displayName");
        List<String> lore = Main.plugin.getConfig().getStringList("cheque-item.lore");

        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            if (Main.plugin.getConfig().getBoolean("settings.formatter-toggle")) {
                line = line.replace("%coins%", NumberFormat.formatNumber((long) coins));
            } else {
                line = line.replace("%coins%", String.valueOf((long) coins));
            }
            line = line.replace("%player%", player.getName());
            lore.set(i, line);
        }

        ItemStack cheque = new ItemStack(material);
        ItemMeta chequeMeta = cheque.getItemMeta();
        chequeMeta.setDisplayName(displayName);
        chequeMeta.setLore(lore);
        cheque.setItemMeta(chequeMeta);
        return cheque;
    }

    public static ItemStack chequeUpdate(ItemStack cheque, double coins) {
        NBTItem nbtItem = new NBTItem(cheque);
        nbtItem.setDouble("coins", coins);
        // Atualize outras tags NBT conforme necessário

        return nbtItem.getItem();
    }

    public static Double getChequeValue(ItemStack cheque) {
        NBTItem nbtItem = new NBTItem(cheque);
        return nbtItem.getDouble("coins");
    }

    public static Long getLongChequeValue(ItemStack cheque) {
        NBTItem nbtItem = new NBTItem(cheque);
        return nbtItem.getLong("coins");
    }
}
