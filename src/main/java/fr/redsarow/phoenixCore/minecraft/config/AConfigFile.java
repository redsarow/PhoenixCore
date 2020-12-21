package fr.redsarow.phoenixCore.minecraft.config;

import com.google.gson.annotations.Expose;

import java.io.File;

/**
 * Classes which extend {@link AConfigFile} must have a constructor with a single parameter {@link File}. <br/>
 * To initialize the configuration use {@link ConfigManager#iniConfig(String, Class)}
 *
 * @author redsarow
 */
public abstract class AConfigFile {

    @Expose(serialize = false, deserialize = false)
    private final double actualVersion;

    public double version;

    @Expose(serialize = false, deserialize = false)
    public File file;

    AConfigFile(File file, double actualVersion) {
        this.file = file;
        this.actualVersion = actualVersion;
        this.version = this.actualVersion;
    }

    public void save() {
        ConfigManager.getInstance().writeConfig(this);
    }
}
