package de.tiostitch.economy.comandos;

import de.tiostitch.economy.Main;
import de.tiostitch.economy.data.economy.EcoManager;
import de.tiostitch.economy.utils.ChequeBuilder;
import de.tiostitch.economy.utils.Messages;
import de.tiostitch.economy.utils.NumberFormat;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.sacredlabyrinth.phaed.simpleclans.managers.ClanManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static de.tiostitch.economy.utils.ChequeBuilder.chequeUpdate;

public class Money implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender.hasPermission("Economy.Manager")) {
            if (sender instanceof Player) {

                if (args.length == 0) {
                    Player player = (Player) sender;
                    long coins = (long) EcoManager.getCoins(player);
                    sender.sendMessage(Main.plugin.getConfig().getString("messages.yourMoney")
                            .replace("%coins%", String.valueOf(coins))
                            .replace("%CoinMsg%", coinPluralSingular(coins))
                            .replace("%formattedcoins%", NumberFormat.formatNumber(coins)));
                    return false;
                }

                if (args.length == 1) {
                    Player player = (Player) sender;
                    Player player2 = Bukkit.getPlayer(args[0]);

                    if (player2 != null) {
                        long coins = (long) EcoManager.getCoins(player2);
                        sender.sendMessage(Main.plugin.getConfig().getString("messages.otherMoney")
                                .replace("%coins%", String.valueOf(coins))
                                .replace("%CoinMsg%", coinPluralSingular(coins))
                                .replace("%formattedcoins%", NumberFormat.formatNumber(coins)));
                        return false;

                    }
                }

                String input = args[0];
                List<String> enviarList = Main.plugin.getConfig().getStringList("aliases-list.enviar");
                List<String> adicionarList = Main.plugin.getConfig().getStringList("aliases-list.adicionar");
                List<String> removerList = Main.plugin.getConfig().getStringList("aliases-list.remover");
                List<String> setarList = Main.plugin.getConfig().getStringList("aliases-list.setar");
                List<String> toggleList = Main.plugin.getConfig().getStringList("aliases-list.toggle");
                List<String> topList = Main.plugin.getConfig().getStringList("aliases-list.top");

                if (enviarList.contains(input)) {
                    if (sender.hasPermission("Economy.Enviar")) {
                        if (args.length < 3) {
                            sender.sendMessage(Main.plugin.getConfig().getString("messages.incorrectUsage"));
                            return false;
                        }

                        try {
                            double coins = Double.parseDouble(args[2]);
                            Player player = Bukkit.getPlayer(args[1]);

                            if (player == null || !player.isOnline()) {
                                sender.sendMessage(Main.plugin.getConfig().getString("messages.playerOffline"));
                                return false;
                            }

                            if (Main.playerData.getToggle(player) == 1) {
                                sender.sendMessage(Main.plugin.getConfig().getString("messages.playerHasUntoggle"));
                                return false;
                            }


                            if (player == sender) {
                                sender.sendMessage(Main.plugin.getConfig().getString("messages.dontSendForMe"));
                                return false;
                            }

                            long senderCoins = (long) EcoManager.getCoins((Player) sender);
                            if (senderCoins < coins) {
                                sender.sendMessage(Main.plugin.getConfig().getString("messages.notEnoughCoins")
                                        .replace("%rcoins%", String.valueOf((int) (senderCoins - coins)))
                                        .replace("%CoinMsg%", coinPluralSingular(coins))
                                        .replace("%coins%", String.valueOf(coins)));
                                return false;
                            }

                            if (coins <= 0) {
                                sender.sendMessage(Main.plugin.getConfig().getString("messages.moreThanZero"));
                                return false;
                            }

                            EcoManager.removeCoins((Player) sender, coins);
                            EcoManager.addCoins(player, coins);


                            String senderMessage = Main.plugin.getConfig().getString("messages.sendMoney")
                                    .replace("%coins%", String.valueOf(coins))
                                    .replace("%CoinMsg%", coinPluralSingular(coins))
                                    .replace("%formattedcoins%", NumberFormat.formatNumber((long) coins))
                                    .replace("%player_send%", player.getName())
                                    .replace("%prefix%", Main.plugin.getConfig().getString("messages.prefix"));

                            String receiverMessage = Main.plugin.getConfig().getString("messages.receiveMoney")
                                    .replace("%coins%", String.valueOf(coins))
                                    .replace("%CoinMsg%", coinPluralSingular(coins))
                                    .replace("%formattedcoins%", NumberFormat.formatNumber((long) coins))
                                    .replace("%player_send%", sender.getName())
                                    .replace("%prefix%", Main.plugin.getConfig().getString("messages.prefix"));

                            sender.sendMessage(senderMessage);
                            player.sendMessage(receiverMessage);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(Main.plugin.getConfig().getString("messages.numberException"));
                            return false;
                        }
                    } else {
                        sender.sendMessage(Main.plugin.getConfig().getString("messages.noPermission"));
                    }

                } else if (adicionarList.contains(input)) {
                        if (sender.hasPermission("Economy.Adicionar")) {
                            if (args.length < 3) {
                                sender.sendMessage(Main.plugin.getConfig().getString("messages.incorrectUsage"));
                                return false;
                            }

                            try {
                                double coins = Double.parseDouble(args[2]);
                                Player player = Bukkit.getPlayer(args[1]);

                                if (player == null || !player.isOnline()) {
                                    sender.sendMessage(Main.plugin.getConfig().getString("messages.playerOffline"));
                                    return false;
                                }


                                if (coins <= 0) {
                                    sender.sendMessage(Main.plugin.getConfig().getString("messages.moreThanZero"));
                                    return false;
                                }

                                EcoManager.addCoins(player, coins);

                                String senderMessage = Main.plugin.getConfig().getString("messages.sendMoney")
                                        .replace("%coins%", String.valueOf(coins))
                                        .replace("%CoinMsg%", coinPluralSingular(coins))
                                        .replace("%formattedcoins%", NumberFormat.formatNumber((long) coins))
                                        .replace("%player_send%", player.getName())
                                        .replace("%prefix%", Main.plugin.getConfig().getString("messages.prefix"));

                                if (Main.plugin.getConfig().getBoolean("settings.coinsAddMessage")) {
                                    String receiverMessage = Main.plugin.getConfig().getString("messages.receiveMoney")
                                            .replace("%coins%", String.valueOf(coins))
                                            .replace("%CoinMsg%", coinPluralSingular(coins))
                                            .replace("%formattedcoins%", NumberFormat.formatNumber((long) coins))
                                            .replace("%player_send%", sender.getName())
                                            .replace("%prefix%", Main.plugin.getConfig().getString("messages.prefix"));
                                    player.sendMessage(receiverMessage);
                                }

                                sender.sendMessage(senderMessage);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(Main.plugin.getConfig().getString("messages.numberException"));
                                return false;
                            }
                        } else {
                            sender.sendMessage(Main.plugin.getConfig().getString("messages.noPermission"));
                        }
                } else if (removerList.contains(input)) {
                    if (sender.hasPermission("Economy.Remover")) {
                        if (args.length < 3) {
                            sender.sendMessage(Main.plugin.getConfig().getString("messages.incorrectUsage"));
                            return false;
                        }

                        try {
                            double coins = Double.parseDouble(args[2]);
                            Player player = Bukkit.getPlayer(args[1]);

                            if (player == null || !player.isOnline()) {
                                sender.sendMessage(Main.plugin.getConfig().getString("messages.playerOffline"));
                                return false;
                            }

                            if (coins <= 0) {
                                sender.sendMessage(Main.plugin.getConfig().getString("messages.moreThanZero"));
                                return false;
                            }

                            if (coins > Main.playerData.getCoins(player)) {
                                sender.sendMessage(Main.plugin.getConfig().getString("messages.moreThanValue"));
                                return false;
                            }

                            EcoManager.removeCoins(player, coins);

                            String senderMessage = Main.plugin.getConfig().getString("messages.removeMoney")
                                    .replace("%coins%", String.valueOf(coins))
                                    .replace("%CoinMsg%", coinPluralSingular(coins))
                                    .replace("%formattedcoins%", NumberFormat.formatNumber((long) coins))
                                    .replace("%player_send%", player.getName())
                                    .replace("%prefix%", Main.plugin.getConfig().getString("messages.prefix"));

                            if (Main.plugin.getConfig().getBoolean("settings.coinsRemoveMessage")) {
                                String receiverMessage = Main.plugin.getConfig().getString("messages.removedMoney")
                                        .replace("%coins%", String.valueOf(coins))
                                        .replace("%CoinMsg%", coinPluralSingular(coins))
                                        .replace("%formattedcoins%", NumberFormat.formatNumber((long) coins))
                                        .replace("%player_send%", sender.getName())
                                        .replace("%prefix%", Main.plugin.getConfig().getString("messages.prefix"));
                                player.sendMessage(receiverMessage);
                            }

                            sender.sendMessage(senderMessage);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(Main.plugin.getConfig().getString("messages.numberException"));
                            return false;
                        }
                    } else {
                        sender.sendMessage(Main.plugin.getConfig().getString("messages.noPermission"));
                    }

                } else if (setarList.contains(input)) {
                    if (sender.hasPermission("Economy.Setar")) {
                        if (args.length < 3) {
                            sender.sendMessage(Main.plugin.getConfig().getString("messages.incorrectUsage"));
                            return false;
                        }

                        try {
                            double coins = Double.parseDouble(args[2]);
                            Player player = Bukkit.getPlayer(args[1]);

                            if (player == null || !player.isOnline()) {
                                sender.sendMessage(Main.plugin.getConfig().getString("messages.playerOffline"));
                                return false;
                            }

                            if (coins <= 0) {
                                sender.sendMessage(Main.plugin.getConfig().getString("messages.moreThanZero"));
                                return false;
                            }

                            EcoManager.setCoins(player, coins);

                            String senderMessage = Main.plugin.getConfig().getString("messages.setMoney")
                                    .replace("%coins%", String.valueOf(coins))
                                    .replace("%formattedcoins%", NumberFormat.formatNumber((long) coins))
                                    .replace("%CoinMsg%", coinPluralSingular(coins))
                                    .replace("%player_send%", player.getName())
                                    .replace("%prefix%", Main.plugin.getConfig().getString("messages.prefix"));

                            if (Main.plugin.getConfig().getBoolean("settings.coinsSetMessage")) {
                                String receiverMessage = Main.plugin.getConfig().getString("messages.setedMoney")
                                        .replace("%coins%", String.valueOf(coins))
                                        .replace("%CoinMsg%", coinPluralSingular(coins))
                                        .replace("%formattedcoins%", NumberFormat.formatNumber((long) coins))
                                        .replace("%player_send%", sender.getName())
                                        .replace("%prefix%", Main.plugin.getConfig().getString("messages.prefix"));
                                player.sendMessage(receiverMessage);
                            }

                            sender.sendMessage(senderMessage);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(Main.plugin.getConfig().getString("messages.numberException"));
                            return false;
                        }
                    } else {
                        sender.sendMessage(Main.plugin.getConfig().getString("messages.noPermission"));
                    }
                } else if (toggleList.contains(input)) {
                    if (sender.hasPermission("Economy.toggle")) {

                        if (args.length >= 2) {
                            sender.sendMessage(Main.plugin.getConfig().getString("messages.incorrectUsage"));
                            return false;
                        }

                        Player player = (Player) sender;

                        if (Main.playerData.getToggle(player) == 0) {
                            player.sendMessage(Main.plugin.getConfig().getString("messages.toggleDesactive"));
                            Main.playerData.setToggle(player, 1);
                        } else if (Main.playerData.getToggle(player) == 1) {
                            player.sendMessage(Main.plugin.getConfig().getString("messages.toggleActive"));
                            Main.playerData.setToggle(player, 0);
                        }

                    } else {
                        sender.sendMessage(Main.plugin.getConfig().getString("messages.noPermission"));
                    }
                } else if (topList.contains(input)) {
                    if (!sender.hasPermission("Economy.Top")) {
                        sender.sendMessage(Main.plugin.getConfig().getString("messages.noPermission"));
                        return false;
                    }

                    if (args.length < 1) {
                        sender.sendMessage(Main.plugin.getConfig().getString("messages.incorrectUsage"));
                        return false;
                    }

                    Set<String> topPlayers = Main.playerData.getTopPlayers(10);

                    int startPosition = 1;
                    for (String playerName : topPlayers) {
                        double coins = Main.playerData.getCoins(playerName); // Obtém a quantidade de coins do jogador

                        String message = Main.plugin.getConfig().getString("messages.moneyTop")
                                .replace("%position%", String.valueOf(startPosition))
                                .replace("%clan%", estaInClan(playerName))
                                .replace("%player%", playerName)
                                .replace("%CoinMsg%", coinPluralSingular(coins))
                                .replace("%tag%", getTagTop(playerName))
                                .replace("%formattedcoins%", NumberFormat.formatNumber((long) coins))
                                .replace("%coins%", String.valueOf(coins));

                        sender.sendMessage(message);

                        if (startPosition >= 7) {
                            break;
                        }

                        startPosition++;
                    }
                }
            } else {
                // Código para execução quando o remetente não for um jogador
            }
        }
        return false;
    }

    public String getTagTop(String player) {
        if (Objects.equals(EcoManager.magnata, player)) {
            return EcoManager.checkMagnata(player);
        } else if (Objects.equals(EcoManager.topSeco, player)) {
            return EcoManager.checkTopSeco(player);
        } else if (Objects.equals(EcoManager.topOne, player)) {
            return EcoManager.checkTopThird(player);
        } else {
            return "";
        }
    }

    public String coinPluralSingular(double coins) {
        if (coins >= 2) {
            return Main.plugin.getConfig().getString("settings.coins-singular");
        } else {
            return Main.plugin.getConfig().getString("settings.coins-plural");
        }
    }

    public String estaInClan(String player) {
        ClanManager clanManager = SimpleClans.getInstance().getClanManager();
        ClanPlayer clanPlayer = clanManager.getClanPlayer(player);

        if (clanPlayer != null) {
            return "§7[" + clanPlayer.getClan().getColorTag() + "§7]";
        } else {
            return "§cSem Clan";
        }
    }
}