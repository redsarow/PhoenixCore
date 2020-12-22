package fr.redsarow.phoenixCore.minecraft.config.configFiles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;
import fr.redsarow.phoenixCore.minecraft.config.AConfigFile;

import java.io.File;
import java.util.ArrayList;

/**
 * @author redsarow
 */
public class MainConf extends AConfigFile {

    @Since(1.0)
    @Expose
    public Boolean discord = false;

    @Since(1.0)
    @Expose
    public String token = "";

    @Since(1.0)
    @Expose
    public String prefix = "!";

    @Since(1.0)
    @Expose
    public ArrayList<String> roles = new ArrayList<>();

    @Since(1.0)
    @Expose
    @SerializedName("channel_in")
    public ArrayList<String> channelIn = new ArrayList<>();

    @Since(1.0)
    @Expose
    @SerializedName("channel_out")
    public String channelOut = "bot";

    public MainConf(File file) {
        super(file, 1.0);
    }

}
