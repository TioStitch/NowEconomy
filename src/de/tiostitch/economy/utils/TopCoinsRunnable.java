package de.tiostitch.economy.utils;

import de.tiostitch.economy.Main;
import de.tiostitch.economy.data.economy.EcoManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;

public class TopCoinsRunnable implements Runnable {
    private final int delay = Main.plugin.getConfig().getInt("settings.money-top-runnable"); // Delay em ticks (30 segundos = 20 ticks por segundo * 30)

    @Override
    public void run() {
        Player magnata = null;
        double maxCoins = 0;

        Set<String> topPlayers = Main.playerData.getTopPlayers(10);

        for (String playerName : topPlayers) {
            Player player = Bukkit.getPlayerExact(playerName);
            if (player == null) {
                double coins = Main.playerData.getCoins(playerName);
                if (coins > maxCoins) {
                    maxCoins = coins;
                    magnata = null; // Limpar o jogador anterior (se houver)
                }
            } else {
                double coins = Main.playerData.getCoins(player);
                if (coins > maxCoins) {
                    maxCoins = coins;
                    magnata = player;
                }
            }
        }

        if (magnata != null) {
            EcoManager.setMagnata(magnata);

            if (!EcoManager.magnata.equals(magnata.getName())) {
                Bukkit.broadcastMessage(Main.plugin.getConfig().getString("messages.magnataSet")
                        .replace("%player%", magnata.getName())
                        .replace("%prefix%", Main.plugin.getConfig().getString("messages.prefix"))
                        .replace("%tag%", EcoManager.checkMagnata(magnata.getName())));
            }
        }
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimer(Main.plugin, this, delay, delay);
    }
}
