 package de.tiostitch.economy.utils.placeholders.VisualizadoresAPI;

 import java.util.Arrays;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 import java.util.stream.Collectors;
 import me.clip.placeholderapi.PlaceholderAPI;
 import org.bukkit.ChatColor;
 import org.bukkit.entity.Player;











 public class PlaceholderAPISupportYes
   implements PlaceholderAPISupport {
     public String chat(Player p, String msg) {
         if (msg == null)
             return ChatColor.translateAlternateColorCodes('&', "&cConfig Missing Text");
         msg = PlaceholderAPI.setPlaceholders(p, msg);
         if (!Pattern.compile("\\{#[0-9A-Fa-f]{6}}").matcher(msg).find()) {
             return ChatColor.translateAlternateColorCodes('&', msg);
         }
         Matcher m = Pattern.compile("\\{#[0-9A-Fa-f]{6}}").matcher(msg);


         while (m.find()) {
             String s = m.group();
             String sNew = "§x" + ((String) Arrays.<String>stream(s.split("")).map(s2 -> "§" + s2).collect(Collectors.joining())).replace("§#", "");
             msg = msg.replace(s, sNew.replace("§{", "").replace("§}", ""));
         }
         return ChatColor.translateAlternateColorCodes('&', msg);
     }

     public String chatApiOnly(Player p, String s) {
         /*    */
         return PlaceholderAPI.setPlaceholders(p, s);
         /*    */
     }
 }