package dev._2lstudios.classes.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VersionUtil {
  public static String getLocale(CommandSender sender) {
    if (sender instanceof Player) {
      String locale;
      Player player = (Player)sender;
      try {
        player.getClass().getMethod("getLocale", new Class[0]);
        locale = player.getLocale();
      } catch (NoSuchMethodException exception) {
        try {
          player.spigot().getClass().getMethod("getLocale", new Class[0]);
          locale = player.spigot().getLocale();
        } catch (Exception exception1) {
          locale = "en";
        } 
      } 
      if (locale != null && locale.length() > 1)
        return locale.substring(0, 2); 
    } 
    return "en";
  }
}
