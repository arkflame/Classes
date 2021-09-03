package dev._2lstudios.classes.plugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import dev._2lstudios.classes.Classes;
import dev._2lstudios.classes.enums.ClassType;
import dev._2lstudios.classes.managers.ClassPlayerManager;
import dev._2lstudios.classes.utils.BukkitUtil;
import dev._2lstudios.teams.Teams;
import dev._2lstudios.teams.managers.TeamPlayerManager;

public class ClassPlayer {
  private final Player player;
  
  private Collection<PotionEffect> pendingEffects = new HashSet<>();
  
  private ItemStack heldItem = null;
  
  private ClassType classType = null;
  
  private double energy = 0.0D;
  
  private long lastSpellTime = 0L;
  
  private long lastArcherTagTime = 0L;
  
  private boolean effectsAllowed = true;
  
  private boolean invisible = false;
  
  public ClassPlayer(Player player) {
    this.player = player;
  }
  
  public void setEffectsAllowed(boolean b) {
    this.effectsAllowed = b;
  }
  
  public boolean isEffectsAllowed() {
    return this.effectsAllowed;
  }
  
  public ClassType getClassType() {
    return this.classType;
  }
  
  public void setClassType(ClassType classType) {
    this.classType = classType;
  }
  
  public void addEnergy(double power) {
    this.energy += power;
  }
  
  public double getEnergy() {
    return this.energy;
  }
  
  public void setLastArcherTagTime() {
    this.lastArcherTagTime = System.currentTimeMillis();
  }
  
  public boolean hasArcherTag() {
    return (System.currentTimeMillis() - this.lastArcherTagTime <= 10000L);
  }
  
  public void setLastSpellTime() {
    this.lastSpellTime = System.currentTimeMillis();
  }
  
  public boolean hasSpellCooldown(long cooldown) {
    return (System.currentTimeMillis() - this.lastSpellTime <= cooldown);
  }
  
  public long getCooldown() {
    int cooldown;
    if (this.classType == ClassType.BARD) {
      cooldown = 5000;
    } else {
      cooldown = 40000;
    } 
    return cooldown - System.currentTimeMillis() - this.lastSpellTime;
  }
  
  public void givePotionEffect(PotionEffect effect) {
    int effectDuration = effect.getDuration();
    if (effectDuration > 0) {
      PotionEffectType effectType = effect.getType();
      PotionEffect effect1 = BukkitUtil.getPotionEffect(this.player, effectType);
      if (effect1 != null) {
        int effectAmplifier = effect.getAmplifier();
        int effectAmplifier1 = effect1.getAmplifier();
        if (effectAmplifier1 < effectAmplifier) {
          this.player.removePotionEffect(effectType);
          addPendingEffect(effect1);
        } else if (effectAmplifier1 == effectAmplifier && effect1.getDuration() < effectDuration) {
          this.player.removePotionEffect(effectType);
        } 
      } 
      this.player.addPotionEffect(effect);
    } 
  }
  
  private void addPendingEffect(PotionEffect effect) {
    this.pendingEffects.add(effect);
  }
  
  public void givePendingEffects() {
    if (!this.pendingEffects.isEmpty()) {
      Iterator<PotionEffect> pendingEffectsIterator = this.pendingEffects.iterator();
      while (pendingEffectsIterator.hasNext()) {
        PotionEffect effect = pendingEffectsIterator.next();
        if (this.player.addPotionEffect(effect))
          pendingEffectsIterator.remove(); 
      } 
    } 
  }
  
  public void clearPendingEffects() {
    this.pendingEffects.clear();
  }
  
  public void giveNearPlayersEffect(PotionEffect potionEffect, int radius) {
    Location location = this.player.getLocation();
    PotionEffectType potionEffectType = potionEffect.getType();
    ClassPlayerManager classPlayerManager = Classes.getClassPlayerManager();
    TeamPlayerManager tPlayerManager = Teams.getTeamsManager().getTeamPlayerManager();
    String team = tPlayerManager.getPlayer(this.player.getName()).getTeam();
    boolean isPositive = (potionEffectType != PotionEffectType.WITHER && 
      potionEffectType != PotionEffectType.POISON && potionEffectType != PotionEffectType.WEAKNESS);
    for (Player player1 : location.getWorld().getPlayers()) {
      Location location1 = player1.getLocation();
      if (this.player != player1 && location.distance(location1) <= radius) {
        String team1 = tPlayerManager.getPlayer(player1.getName()).getTeam();
        boolean sameTeam = !(this.player != player1 && (team == null || team1 == null || !team.equals(team1)));
        if ((isPositive && sameTeam) || (!isPositive && !sameTeam)) {
          ClassPlayer classPlayer1 = classPlayerManager.get(player1);
          if (classPlayer1 != null)
            classPlayer1.givePotionEffect(potionEffect); 
        } 
      } 
    } 
  }
  
  public ItemStack getHeldItem() {
    return this.heldItem;
  }
  
  public void setHeldItem(ItemStack heldItem) {
    this.heldItem = heldItem;
  }
  
  public void clearClassEffects() {
    if (this.classType != null) {
      byte b;
      int i;
      PotionEffect[] arrayOfPotionEffect;
      for (i = (arrayOfPotionEffect = this.classType.getPotionEffects()).length, b = 0; b < i; ) {
        PotionEffect potionEffect = arrayOfPotionEffect[b];
        this.player.removePotionEffect(potionEffect.getType());
        b++;
      } 
      if (this.classType == ClassType.MINER)
        this.player.removePotionEffect(PotionEffectType.INVISIBILITY); 
    } 
  }
  
  public void setInvisible(boolean invisible) {
    this.invisible = invisible;
  }
  
  public boolean isInvisible() {
    return this.invisible;
  }
}
