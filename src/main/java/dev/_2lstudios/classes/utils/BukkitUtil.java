package dev._2lstudios.classes.utils;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BukkitUtil {
  public static PotionEffect getPotionEffect(Player player, PotionEffectType potionEffectType) {
    for (PotionEffect potionEffect : player.getActivePotionEffects()) {
      if (potionEffect.getType().getName().equalsIgnoreCase(potionEffectType.getName()))
        return potionEffect; 
    } 
    return null;
  }
}
