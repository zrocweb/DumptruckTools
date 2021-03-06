package com.dumptruckman.tools.config;

import com.dumptruckman.tools.plugin.PluginBase;
import com.dumptruckman.tools.util.Logging;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Commented Yaml implementation of ConfigBase.
 */
public abstract class AbstractYamlConfig implements ConfigBase {

    /**
     * Add a comment to the top of file.
     */
    protected static final ConfigEntry SETTINGS
            = new ConfigEntry("settings", null, "# ===[ DumptruckTools Config ]===");

    /**
     * Locale name config path, default and comments.
     */
    private static final ConfigEntry LANGUAGE_FILE_NAME
            = new ConfigEntry("settings.locale", "en", "# This is the locale you wish to use.");

    /**
     * Debug Mode config path, default and comments.
     */
    private static final ConfigEntry DEBUG_MODE
            = new ConfigEntry("settings.debug_level", 0, "# 0 = off, 1-3 display debug info with increasing "
            + "granularity.");

    /**
     * First Run flag config path, default and comments.
     */
    private static final ConfigEntry FIRST_RUN
            = new ConfigEntry("settings.first_run", true, "# Will make the plugin perform tasks only done on "
            + "a first run.");
    
    static {
        Entries.registerConfig(AbstractYamlConfig.class);
    }

    private CommentedYamlConfiguration config;
    private PluginBase plugin;

    public AbstractYamlConfig(PluginBase plugin) throws IOException {
        this.plugin = plugin;
        // Make the data folders
        if (this.plugin.getDataFolder().mkdirs()) {
            Logging.fine("Created data folder.");
        }

        // Check if the config file exists.  If not, create it.
        File configFile = new File(this.plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            if (configFile.createNewFile()) {
                Logging.fine("Created config file.");
            }
        }

        // Load the configuration file into memory
        config = new CommentedYamlConfiguration(configFile);
        config.load();

        // Sets defaults config values
        this.setDefaults();

        // Saves the configuration from memory to file
        config.save();
    }

    /**
     * Loads default settings for any missing config values.
     */
    private void setDefaults() {
        for (ConfigEntry path : Entries.entries) {
            config.addComment(path.getPath(), path.getComments());
            if (this.getConfig().get(path.getPath()) == null) {
                if (path.getDefault() != null) {
                    Logging.fine("Config: Defaulting '" + path.getPath() + "' to " + path.getDefault());
                    this.getConfig().set(path.getPath(), path.getDefault());
                }
            }
        }
    }

    private Boolean getBoolean(ConfigEntry path) {
        return this.getConfig().getBoolean(path.getPath(), (Boolean) path.getDefault());
    }

    private Integer getInt(ConfigEntry path) {
        return this.getConfig().getInt(path.getPath(), (Integer) path.getDefault());
    }

    private String getString(ConfigEntry path) {
        return this.getConfig().getString(path.getPath(), (String) path.getDefault());
    }

    protected FileConfiguration getConfig() {
        return this.config.getConfig();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save() {
        this.config.save();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDebug(int globalDebug) {
        this.getConfig().set(DEBUG_MODE.getPath(), globalDebug);
        Logging.setDebugMode(globalDebug);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDebug() {
        return this.getInt(DEBUG_MODE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLocale() {
        return this.getString(LANGUAGE_FILE_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFirstRun() {
        return this.getBoolean(FIRST_RUN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFirstRun(boolean firstRun) {
        this.getConfig().set(FIRST_RUN.getPath(), firstRun);
    }

    protected abstract ConfigEntry getSettingsHeader();
}
