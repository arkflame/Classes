package dev._2lstudios.classes.tasks;

import java.util.EnumMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import dev._2lstudios.classes.enums.ClassType;
import dev._2lstudios.classes.managers.ClassPlayerManager;
import dev._2lstudios.classes.plugin.ClassPlayer;
import dev._2lstudios.classes.utils.BukkitUtil;

public class TaskTimer {
  private final Map<Material, PotionEffect> effectMap = new EnumMap<>(Material.class);
  
  public TaskTimer(Plugin plugin, ClassPlayerManager classPlayerManager) {
    Server server = plugin.getServer();
    this.effectMap.put(Material.SUGAR, new PotionEffect(PotionEffectType.SPEED, 100, 1));
    this.effectMap.put(Material.GHAST_TEAR, new PotionEffect(PotionEffectType.REGENERATION, 100, 0));
    this.effectMap.put(Material.IRON_INGOT, new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0));
    this.effectMap.put(Material.BLAZE_POWDER, new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0));
    this.effectMap.put(Material.GOLDEN_CARROT, new PotionEffect(PotionEffectType.NIGHT_VISION, 100, 0));
    this.effectMap.put(Material.MAGMA_CREAM, new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 0));
    this.effectMap.put(Material.RABBIT_FOOT, new PotionEffect(PotionEffectType.JUMP, 100, 1));
    this.effectMap.put(Material.INK_SACK, new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0));
    server.getScheduler().runTaskTimer(plugin, () -> {
          for (Player player : server.getOnlinePlayers())
            runTask(classPlayerManager, player); 
        }, 20L, 20L);
  }
  
  private ClassType getArmor(Player player) {
    PlayerInventory inventory = player.getInventory();
    if ((inventory.getArmorContents()).length > 0) {
      ItemStack helmetItem = inventory.getHelmet();
      if (helmetItem != null) {
        ItemStack chestplateItem = inventory.getChestplate();
        if (chestplateItem != null) {
          ItemStack leggingsItem = inventory.getLeggings();
          if (leggingsItem != null) {
            ItemStack bootsItem = inventory.getBoots();
            if (bootsItem != null) {
              Material helmet = helmetItem.getType();
              Material chestplate = chestplateItem.getType();
              Material leggings = leggingsItem.getType();
              Material boots = bootsItem.getType();
              if (helmet == Material.GOLD_HELMET && chestplate == Material.GOLD_CHESTPLATE && 
                leggings == Material.GOLD_LEGGINGS && boots == Material.GOLD_BOOTS)
                return ClassType.BARD; 
              if (helmet == Material.LEATHER_HELMET && chestplate == Material.LEATHER_CHESTPLATE && 
                leggings == Material.LEATHER_LEGGINGS && boots == Material.LEATHER_BOOTS)
                return ClassType.ARCHER; 
              if (helmet == Material.CHAINMAIL_HELMET && chestplate == Material.CHAINMAIL_CHESTPLATE && 
                leggings == Material.CHAINMAIL_LEGGINGS && boots == Material.CHAINMAIL_BOOTS)
                return ClassType.ROGUE; 
              if (helmet == Material.IRON_HELMET && chestplate == Material.IRON_CHESTPLATE && 
                leggings == Material.IRON_LEGGINGS && boots == Material.IRON_BOOTS)
                return ClassType.MINER; 
              if (helmet == Material.DIAMOND_HELMET && chestplate == Material.DIAMOND_CHESTPLATE && 
                leggings == Material.DIAMOND_LEGGINGS && boots == Material.DIAMOND_BOOTS)
                return ClassType.DIAMOND; 
            } 
          } 
        } 
      } 
    } 
    return null;
  }
  
  public void runBardEffect(ClassPlayer classPlayer) {
    ItemStack heldItem = classPlayer.getHeldItem();
    if (heldItem != null) {
      PotionEffect potionEffect = getPotionEffect(heldItem.getType());
      if (potionEffect != null) {
        PotionEffectType potionEffectType = potionEffect.getType();
        if (potionEffect.getAmplifier() > 0)
          potionEffect = new PotionEffect(potionEffectType, potionEffect.getDuration(), 0); 
        classPlayer.giveNearPlayersEffect(potionEffect, 25);
      } 
    } 
  }
  
  private PotionEffect getPotionEffect(Material material) {
    return this.effectMap.getOrDefault(material, null);
  }
  
  private void runTask(ClassPlayerManager classPlayerManager, Player player) {
    ClassPlayer classPlayer = classPlayerManager.get(player);
    if (classPlayer != null) {
      ClassType newClassType = classPlayer.isEffectsAllowed() ? getArmor(player) : null;
      ClassType oldClassType = classPlayer.getClassType();
      if (newClassType != oldClassType) {
        classPlayer.clearPendingEffects();
        classPlayer.clearClassEffects();
        classPlayer.setClassType(newClassType);
        if (newClassType != null)
          player.sendMessage(ChatColor.GREEN + "Activaste la clase " + ChatColor.AQUA + 
              newClassType.toString() + ChatColor.GREEN + "!"); 
      } 
      if (newClassType != null) {
        byte b;
        int i;
        PotionEffect[] arrayOfPotionEffect;
        for (i = (arrayOfPotionEffect = newClassType.getPotionEffects()).length, b = 0; b < i; ) {
          PotionEffect potionEffect = arrayOfPotionEffect[b];
          classPlayer.givePotionEffect(potionEffect);
          b++;
        } 
        double energy = classPlayer.getEnergy();
        if (newClassType == ClassType.BARD) {
          if (energy < 100.0D)
            classPlayer.addEnergy(1.0D); 
          runBardEffect(classPlayer);
        } else {
          if (energy > 0.0D)
            classPlayer.addEnergy(-energy); 
          if (newClassType == ClassType.MINER)
            if (player.getLocation().getY() <= 50.0D) {
              classPlayer.givePotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1200, 0));
              classPlayer.setInvisible(true);
            } else if (classPlayer.isInvisible()) {
              PotionEffect potionEffect = BukkitUtil.getPotionEffect(player, 
                  PotionEffectType.INVISIBILITY);
              if (potionEffect != null) {
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                classPlayer.setInvisible(false);
              } 
            }  
        } 
      } 
    } 
  }
}
