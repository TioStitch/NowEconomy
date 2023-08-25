package de.tiostitch.economy.data;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class YamlData {
    public static void setCoins(String playerUUID, double i) throws IOException {
        File file = new File("plugins/NowEconomy/data/" + playerUUID + "/data.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        yamlConfiguration.set("principal.coins", Double.valueOf(i));
        yamlConfiguration.save(file);
    }

    public static double getCoins(String playerUUID) {
        File file = new File("plugins/NowEconomy/data/" + playerUUID + "/data.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        return yamlConfiguration.getDouble("principal.coins");
    }

    public static void setCoinsNoBanco(Player p, int i) throws IOException {
        File file = new File("plugins/NowEconomy/data/" + p.getUniqueId() + "/data.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        yamlConfiguration.set("principal.banco", Integer.valueOf(i));
        yamlConfiguration.save(file);
    }

    public static int getCoinsDoBanco(Player p) {
        File file = new File("plugins/NowEconomy/data/" + p.getUniqueId() + "/data.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        return yamlConfiguration.getInt("principal.banco");
    }

    public static void createPlayer(Player p) throws IOException {
        File folder = new File("plugins/NowEconomy/data/" + p.getUniqueId());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File playerFile = new File("plugins/NowEconomy/data/" + p.getUniqueId() + "/data.yml");
        if (!playerFile.exists()) {
            playerFile.createNewFile();
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(playerFile);

            yamlConfiguration.set("principal.coins", Integer.valueOf(0));
            yamlConfiguration.set("principal.banco", Integer.valueOf(0));

            yamlConfiguration.save(playerFile);
        }
    }
}

