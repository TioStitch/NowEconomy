package de.tiostitch.economy.data.economy;

import de.tiostitch.economy.Main;
import de.tiostitch.economy.data.YamlData;
import de.tiostitch.economy.events.PlayerEvents;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.bukkit.Bukkit.getScheduler;
import static org.bukkit.Bukkit.getServer;

public class EcoManager {

    public static String magnata = "§cNinguém!";
    public static String topSeco = "§cNinguém!";
    public static String topThird = "§cNinguém!";

    public static void setCoins(Player player, double coins) throws IOException {
        switch (Main.plugin.getConfig().getString("settings.file-process.method")) {
            case "MYSQL":
                BukkitScheduler scheduler = getServer().getScheduler();
                scheduler.runTaskAsynchronously(Main.plugin, () -> Main.playerData.setCoins(player, coins));
                break;
            case "YAML":
                YamlData.setCoins(player.getUniqueId().toString(), coins);
                break;
        }
    }

    public static void addCoins(Player player, double coins) throws IOException {
        switch (Main.plugin.getConfig().getString("settings.file-process.method")) {
            case "MYSQL":
                BukkitScheduler scheduler = getServer().getScheduler();
                scheduler.runTaskAsynchronously(Main.plugin, () -> Main.playerData.setCoins(player, getCoins(player.getName(), player.getUniqueId().toString()) + coins));
                break;
            case "YAML":
                YamlData.setCoins(player.getUniqueId().toString(), YamlData.getCoins(player.getUniqueId().toString()) + coins);
                break;
        }
    }

    public static String checkMagnata(String player) {
        if (magnata.equals(player)) {
            return Main.plugin.getConfig().getString("settings.magnata");
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


    public static void removeCoins(Player player, double coins) throws IOException {
        switch (Main.plugin.getConfig().getString("settings.file-process.method")) {
            case "MYSQL":
                BukkitScheduler scheduler = getServer().getScheduler();
                scheduler.runTaskAsynchronously(Main.plugin, () -> Main.playerData.setCoins(player, getCoins(player.getName(), player.getUniqueId().toString()) - coins));
                break;
            case "YAML":
                YamlData.setCoins(player.getUniqueId().toString(), YamlData.getCoins(player.getUniqueId().toString()) - coins);
                break;
        }
    }

    private static HashMap<String, Double> coins = new HashMap<>();
    public static double getCoins(String player, String UUID) {
        if (PlayerEvents.type == "MYSQL") {
            BukkitScheduler scheduler = getServer().getScheduler();
            scheduler.runTaskAsynchronously(Main.plugin, () -> coins.put(player, Main.playerData.getCoins(player)));
            return coins.get(player);
        } else if (PlayerEvents.type == "YAML") {
            return YamlData.getCoins(UUID);
        }
        return 0.0;
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
