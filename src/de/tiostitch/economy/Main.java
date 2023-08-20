package de.tiostitch.economy;

import de.tiostitch.economy.comandos.Money;
import de.tiostitch.economy.data.PlayerData;
import de.tiostitch.economy.events.PlayerEvents;
import de.tiostitch.economy.utils.TopCoinsRunnable;
import de.tiostitch.economy.utils.TopThirdRunnable;
import de.tiostitch.economy.utils.TopTwoRunnable;
import de.tiostitch.economy.utils.placeholders.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends JavaPlugin implements Listener {

    public static Main plugin;
    public static PlayerData playerData;

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();
        Bukkit.getConsoleSender().sendMessage("§6--------------------");
        Bukkit.getConsoleSender().sendMessage("   §6§lNowEconomy");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(" §7Versão: §a1.0");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("§6--------------------");
        Bukkit.getConsoleSender().sendMessage("§aPlugin iniciado com sucesso!");

            playerData = new PlayerData(
                    getConfig().getString("settings.MySQL.host"),
                    getConfig().getString("settings.MySQL.database"),
                    getConfig().getString("settings.MySQL.username"),
                    getConfig().getString("settings.MySQL.password"));

        new TopCoinsRunnable().start();
        new TopTwoRunnable().start();
        new TopThirdRunnable().start();
        Bukkit.getPluginCommand("money").setExecutor(new Money());
        Bukkit.getPluginManager().registerEvents(new PlayerEvents(), this);
        Bukkit.getPluginManager().registerEvents(this, this);
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            (new Placeholders(Main.plugin)).register();
        }
        playerData.createTable();
    }

    @Override
    public void onDisable() {
        try {
            playerData.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
