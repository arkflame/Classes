package dev._2lstudios.classes.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import dev._2lstudios.classes.managers.ClassPlayerManager;
import dev._2lstudios.classes.plugin.ClassPlayer;

public class PlayerQuitListener implements Listener {
  private final ClassPlayerManager classPlayerManager;
  
  public PlayerQuitListener(ClassPlayerManager classPlayerManager) {
    this.classPlayerManager = classPlayerManager;
  }
  
  @EventHandler(ignoreCancelled = true)
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    ClassPlayer classPlayer = this.classPlayerManager.get(player);
    classPlayer.clearClassEffects();
    this.classPlayerManager.remove(player);
  }
}
