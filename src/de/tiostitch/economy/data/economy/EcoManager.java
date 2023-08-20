package de.tiostitch.economy.data.economy;

import de.tiostitch.economy.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class EcoManager {

    public static String magnata = "§cNinguém!";
    public static String topOne = "§cNinguém!";
    public static String topSeco = "§cNinguém!";
    public static String topThird = "§cNinguém!";

    public static void setCoins(Player player, double coins) {
        Main.playerData.setCoins(player, coins);
    }

    public static void addCoins(Player player, double coins) {
        Main.playerData.setCoins(player, getCoins(player) + coins);
    }

    public static String checkMagnata(String player) {
        if (magnata.equals(player)) {
            return Main.plugin.getConfig().getString("settings.magnata");
        } else {
            return "";
        }
    }

    public static String checkTopOne(String player) {
        if (topOne.equals(player)) {
            return Main.plugin.getConfig().getString("chat-tag-settings.top-1.tag-prefix");
        } else {
            return "";
        }
    }
    public static String checkTopSeco(String player) {
        if (topSeco.equals(player)) {
            return Main.plugin.getConfig().getString("chat-tag-settings.top-2.tag-prefix");
        } else {
            return "";
        }
    }
    public static String checkTopThird(String player) {
        if (topThird.equals(player)) {
            return Main.plugin.getConfig().getString("chat-tag-settings.top-3.tag-prefix");
        } else {
            return "";
        }
    }


    public static void removeCoins(Player player, double coins) {
        Main.playerData.setCoins(player, getCoins(player) - coins);
    }

    public static double getCoins(Player player) {
        return Main.playerData.getCoins(player);
    }

    public static String getMagnata() {
        return magnata;
    }

    public static String setMagnata(Player player) {
        if (magnata == player.getName()) {
            return magnata;
        }
        return magnata = player.getName();
    }

    public static String setTopOne(String player) {
        if (topOne == player) {
            return topOne;
        }
        return topOne = player;
    }

    public static String setTopSeco(String player) {
        if (topSeco == player) {
            return topSeco;
        }
        return topSeco = player;
    }

    public static String setTopThird(String player) {
        if (topThird == player) {
            return topThird;
        }
        return topThird = player;
    }

    public static double getDoubleCoins(Player player) {
        return 2 * Main.playerData.getCoins(player);
    }

    public static double multipliedCoins(Player player, double multiply) {
        return multiply * Main.playerData.getCoins(player);
    }
}
