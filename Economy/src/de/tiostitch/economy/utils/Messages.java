package de.tiostitch.economy.utils;

import de.tiostitch.economy.Main;

public enum Messages {

    NO_PERMISSION(Main.plugin.getConfig().getString("messages.no-permission")),
    OFFLINE_PLAYER(Main.plugin.getConfig().getString("messages.no-online")),
    MORE_THAN_ZERO(Main.plugin.getConfig().getString("messages.more-than-zero")),
    DONT_HAVE_COINS(Main.plugin.getConfig().getString("messages.dont-have-coins").replace("%rcoins%", String.valueOf(getNecessaryCoins()))),
    NUMBER_EXCEPTION(Main.plugin.getConfig().getString("messages.number-exception"));

    String msg;
    static String rCoins;

    Messages(String message) {
        msg = message;
    }

    public String getString() {
        return msg;
    }

    public static String setNecessaryCoins(double coins) {
        return rCoins = String.valueOf(coins);
    }

    public static String getNecessaryCoins() {
        return rCoins;
    }
}
