package dev._2lstudios.classes;

import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import dev._2lstudios.classes.commandexecutors.ClassesCommandExecutor;
import dev._2lstudios.classes.language.LanguageManager;
import dev._2lstudios.classes.listeners.EntityDamageByEntityListener;
import dev._2lstudios.classes.listeners.EntityShootBowListener;
import dev._2lstudios.classes.listeners.PlayerInteractListener;
import dev._2lstudios.classes.listeners.PlayerItemHeldListener;
import dev._2lstudios.classes.listeners.PlayerJoinListener;
import dev._2lstudios.classes.listeners.PlayerQuitListener;
import dev._2lstudios.classes.managers.ClassPlayerManager;
import dev._2lstudios.classes.placeholders.ClassesPlaceholders;
import dev._2lstudios.classes.tasks.TaskTimer;
import dev._2lstudios.teams.utils.ConfigurationUtil;

public class Classes extends JavaPlugin {
  private static ClassPlayerManager classPlayerManager;
  
  private ClassesPlaceholders classesPlaceholders;
  
  private static void setClassPlayerManager(ClassPlayerManager classPlayerManager) {
    Classes.classPlayerManager = classPlayerManager;
  }
  
  public static ClassPlayerManager getClassPlayerManager() {
    return classPlayerManager;
  }
  
  public void onEnable() {
    setClassPlayerManager(new ClassPlayerManager());
    Server server = getServer();
    PluginManager pluginManager = server.getPluginManager();
    ConfigurationUtil configurationUtil = new ConfigurationUtil((Plugin)this);
    String dataFolder = getDataFolder().toString();
    LanguageManager languageManager = new LanguageManager(dataFolder, configurationUtil);
    TaskTimer taskTimer = new TaskTimer((Plugin)this, classPlayerManager);
    pluginManager.registerEvents((Listener)new EntityDamageByEntityListener(classPlayerManager), (Plugin)this);
    pluginManager.registerEvents((Listener)new EntityShootBowListener(classPlayerManager), (Plugin)this);
    pluginManager.registerEvents((Listener)new PlayerInteractListener(classPlayerManager), (Plugin)this);
    pluginManager.registerEvents((Listener)new PlayerItemHeldListener(classPlayerManager, taskTimer), (Plugin)this);
    pluginManager.registerEvents((Listener)new PlayerJoinListener(classPlayerManager), (Plugin)this);
    pluginManager.registerEvents((Listener)new PlayerQuitListener(classPlayerManager), (Plugin)this);
    getCommand("classes")
      .setExecutor((CommandExecutor)new ClassesCommandExecutor(languageManager, getDescription().getVersion()));
    if (pluginManager.isPluginEnabled("PlaceholderAPI")) {
      this.classesPlaceholders = new ClassesPlaceholders((Plugin)this, classPlayerManager);
      this.classesPlaceholders.register();
    } 
  }
  
  public void onDisable() {
    if (this.classesPlaceholders != null)
      this.classesPlaceholders.unregister(); 
  }
}
