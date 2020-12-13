package fr.redsarow.phoenixCore.minecraft.config;

import com.google.gson.annotations.Expose;

import java.io.File;

/**
 * @author redsarow
 */
public abstract class AConfigFile {

    @Expose(serialize = false, deserialize = false)
    public File file;

    public AConfigFile(File file) {
        this.file = file;
    }

    public void save(){
        ConfigManager.getInstance().writeConfig(this);
    }
}
