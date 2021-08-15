package dev._2lstudios.classes.language;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import dev._2lstudios.teams.utils.ConfigurationUtil;

public class LanguageManager {
  private final Map<String, ClassesLanguage> locales = new HashMap<>();
  
  private final String defaultLocale = "en";
  
  public LanguageManager(String dataFolder, ConfigurationUtil configurationUtil) {
    String[] premadeLocales = { "en", "es" };
    String localeFolderRaw = String.valueOf(dataFolder) + "/locales/";
    byte b;
    int i;
    String[] arrayOfString1;
    for (i = (arrayOfString1 = premadeLocales).length, b = 0; b < i; ) {
      String locale = arrayOfString1[b];
      configurationUtil.createConfiguration(String.valueOf(localeFolderRaw) + locale + ".yml");
      b++;
    } 
    for (i = (arrayOfString1 = (new File(localeFolderRaw)).list()).length, b = 0; b < i; ) {
      String languageFile = arrayOfString1[b];
      YamlConfiguration yamlConfiguration = configurationUtil.getConfiguration(String.valueOf(localeFolderRaw) + languageFile);
      String locale = languageFile.split("[.]")[0];
      this.locales.put(locale, new ClassesLanguage((ConfigurationSection)yamlConfiguration));
      b++;
    } 
  }
  
  public ClassesLanguage getLanguage(String rawLocale) {
    String locale = formatLocale(rawLocale);
    if (this.locales.containsKey(locale))
      return this.locales.get(locale); 
    return this.locales.get("en");
  }
  
  private String formatLocale(String rawLocale) {
    if (rawLocale.length() > 1)
      return rawLocale.substring(0, 2); 
    return "en";
  }
}
