package de.tiostitch.economy.events;

import de.tiostitch.economy.Main;
import de.tiostitch.economy.data.PlayerData;
import de.tiostitch.economy.data.YamlData;
import de.tiostitch.economy.data.economy.EcoManager;
import de.tiostitch.economy.utils.ChequeBuilder;
import de.tiostitch.economy.utils.NumberFormat;
import de.tiostitch.economy.utils.TopCoinsRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerEvents implements Listener {

    public static String type = "";

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws IOException {
        switch (Main.plugin.getConfig().getString("settings.file-process.method")) {
            case "MYSQL":
                Main.playerData.createPlayer(e.getPlayer());
                break;
            case "YAML":
                YamlData.createPlayer(e.getPlayer());
                break;
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (EcoManager.magnata == e.getPlayer().getName()) {
            if (Bukkit.getOnlinePlayers().length > 0) {
                new TopCoinsRunnable().run();
            }
        }
    }
}