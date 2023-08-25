package de.tiostitch.economy.utils.placeholders;

import de.tiostitch.economy.Main;
import de.tiostitch.economy.data.economy.EcoManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class Placeholders
   extends PlaceholderExpansion {

    Player p = null;
    public Main plugin;

    public Placeholders(Main plugin) {
        this.plugin = plugin;
    }

    public String getIdentifier() {
        return "MI";
    }

    public String getName() {
        return "MinasEconomy";
    }

    public String getAuthor() {
        return "Minas";
    }


    public String getVersion() {
        return "1.0";
    }


    public boolean persist() {
        return true;
    }

    public String onPlaceholderRequest(Player p, String id) {
        if (id.equals("coins"))
            return "" + EcoManager.getCoins(p.getName(), p.getUniqueId().toString());
        if (id.equals("Magnata"))
            return "" + EcoManager.checkMagnata(p.getName());
        if (id.equals("TopSeco"))
            return "" + EcoManager.checkTopSeco(p.getName());
        if (id.equals("TopThird"))
            return "" + EcoManager.checkTopThird(p.getName());
        return id;
    }
}