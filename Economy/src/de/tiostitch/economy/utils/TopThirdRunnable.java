package de.tiostitch.economy.utils;

import de.tiostitch.economy.Main;
import de.tiostitch.economy.data.economy.EcoManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;

public class TopThirdRunnable implements Runnable {
    private final int delay = Main.plugin.getConfig().getInt("settings.third-top-runnable");; // Delay em ticks (30 segundos = 20 ticks por segundo * 30)

    @Override
    public void run() {
        Player magnata = null;
        double maxCoins = 0;
        Player secondRichest = null;
        double secondMaxCoins = 0;
        Player thirdRichest = null;
        double thirdMaxCoins = 0;

        Set<String> topPlayers = Main.playerData.getTopPlayers(10);

        for (String playerName : topPlayers) {
            Player player = Bukkit.getPlayerExact(playerName);
            if (player == null) {
                // Jogador offline, buscar informações do banco de dados
                double coins = EcoManager.getCoins(playerName, playerName);
                if (coins > maxCoins) {
                    thirdRichest = secondRichest; // O terceiro mais rico passa a ser o antigo segundo mais rico
                    thirdMaxCoins = secondMaxCoins;
                    secondRichest = magnata; // O segundo mais rico passa a ser o antigo mais rico
                    secondMaxCoins = maxCoins;
                    maxCoins = coins;
                    magnata = null; // Limpar o jogador anterior (se houver)
                } else if (coins > secondMaxCoins) {
                    thirdRichest = secondRichest; // O terceiro mais rico passa a ser o antigo segundo mais rico
                    thirdMaxCoins = secondMaxCoins;
                    secondRichest = null; // Limpar o jogador anterior (se houver)
                    secondMaxCoins = coins;
                } else if (coins > thirdMaxCoins) {
                    thirdRichest = null; // Limpar o jogador anterior (se houver)
                    thirdMaxCoins = coins;
                }
            } else {
                // Jogador online
                double coins = EcoManager.getCoins(player.getName(), player.getUniqueId().toString());
                if (coins > maxCoins) {
                    thirdRichest = secondRichest; // O terceiro mais rico passa a ser o antigo segundo mais rico
                    thirdMaxCoins = secondMaxCoins;
                    secondRichest = magnata; // O segundo mais rico passa a ser o antigo mais rico
                    secondMaxCoins = maxCoins;
                    maxCoins = coins;
                    magnata = player;
                } else if (coins > secondMaxCoins) {
                    thirdRichest = secondRichest; // O terceiro mais rico passa a ser o antigo segundo mais rico
                    thirdMaxCoins = secondMaxCoins;
                    secondRichest = player;
                    secondMaxCoins = coins;
                } else if (coins > thirdMaxCoins) {
                    thirdRichest = player;
                    thirdMaxCoins = coins;
                }
            }
        }

        if (thirdRichest != null) {
            EcoManager.setTopThird(thirdRichest.getName());
        }
    }

    public void start() {
        Bukkit.getScheduler().runTaskTimer(Main.plugin, this, delay, delay);
    }
}
