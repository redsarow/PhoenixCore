package fr.redsarow.phoenixCore.minecraft.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.redsarow.phoenixCore.PhoenixCore;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// TODO versioning

/**
 * @author redsarow
 */
public class ConfigManager {

    private static final Path MOD_CONFIG_DIR = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), PhoenixCore.MOD_ID);
    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().setPrettyPrinting().create();
    private static final Logger LOGGER = PhoenixCore.getLogger(ConfigManager.class.getSimpleName());
    private static ConfigManager instance = null;

    private ConfigManager() {
        if (!Files.exists(MOD_CONFIG_DIR)) {
            try {
                Files.createDirectory(MOD_CONFIG_DIR);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public <T extends AConfigFile> T iniConfig(String fileName, Class<T> clazz) {
        File file = new File(MOD_CONFIG_DIR.toString(), fileName);
        AConfigFile configFile = null;
        if (file.exists()) {
            configFile = this.readConfig(file, clazz);
        }else {
            // set default config
            LOGGER.info("Init def conf for : " + fileName);
            try {
                configFile = clazz.getDeclaredConstructor(File.class).newInstance(file);
                writeConfig(configFile);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                LOGGER.error("error init def conf " + fileName, e);
            }
        }
        return clazz.cast(configFile);
    }

    private <T extends AConfigFile> T readConfig(File file, Class<T> clazz) {
        AConfigFile configFile = null;
        try (FileReader reader = new FileReader(file)) {
            configFile = GSON.fromJson(reader, clazz);
            configFile.file = file;
        } catch (IOException e) {
            LOGGER.error("error read conf " + file.getName(), e);
        }
        return clazz.cast(configFile);
    }

    public AConfigFile readConfig(AConfigFile configFile) {
        return readConfig(configFile.file, configFile.getClass());
    }

    public void writeConfig(AConfigFile configFile) {
        try (FileWriter writer = new FileWriter(configFile.file)) {
            GSON.toJson(configFile, writer);
        } catch (IOException e) {
            LOGGER.error("error write conf " + configFile, e);
        }
    }
}
