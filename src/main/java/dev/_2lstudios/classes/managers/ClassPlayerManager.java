package dev._2lstudios.classes.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;
import dev._2lstudios.classes.plugin.ClassPlayer;

public class ClassPlayerManager {
  private final Map<UUID, ClassPlayer> classPlayers = new HashMap<>();
  
  public void remove(Player player) {
    this.classPlayers.remove(player.getUniqueId());
  }
  
  public ClassPlayer get(Player player) {
    UUID uuid = player.getUniqueId();
    if (this.classPlayers.containsKey(uuid))
      return this.classPlayers.get(uuid); 
    ClassPlayer classPlayer = new ClassPlayer(player);
    this.classPlayers.put(uuid, classPlayer);
    return classPlayer;
  }
}
