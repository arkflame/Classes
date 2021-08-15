package dev._2lstudios.classes.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import dev._2lstudios.classes.enums.ClassType;
import dev._2lstudios.classes.managers.ClassPlayerManager;
import dev._2lstudios.classes.plugin.ClassPlayer;
import dev._2lstudios.classes.tasks.TaskTimer;

public class PlayerItemHeldListener implements Listener {
  private final ClassPlayerManager classPlayerManager;
  
  private final TaskTimer taskTimer;
  
  public PlayerItemHeldListener(ClassPlayerManager classPlayerManager, TaskTimer taskTimer) {
    this.classPlayerManager = classPlayerManager;
    this.taskTimer = taskTimer;
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  public void onItemSwitch(PlayerItemHeldEvent event) {
    Player player = event.getPlayer();
    ClassPlayer classPlayer = this.classPlayerManager.get(player);
    PlayerInventory playerInventory = player.getInventory();
    ItemStack heldItem = playerInventory.getItem(event.getNewSlot());
    classPlayer.setHeldItem(heldItem);
    if (classPlayer.getClassType() == ClassType.BARD)
      this.taskTimer.runBardEffect(classPlayer); 
  }
}
