package fr.redsarow.phoenixCore.minecraft.config;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import java.io.File;
import java.util.ArrayList;

/**
 * @author redsarow
 */
public class MainConf extends AConfigFile {

    @Since(1.0)
    public double version = 1.0;

    @Since(1.0)
    public Boolean discord = false;

    @Since(1.0)
    public String token = "";

    @Since(1.0)
    public String prefix = "!";

    @Since(1.0)
    public ArrayList<String> roles = new ArrayList<>();

    @Since(1.0)
    @SerializedName("channel_in")
    public ArrayList<String> channelIn = new ArrayList<>();

    @Since(1.0)
    @SerializedName("channel_out")
    public String channelOut = "bot";

    public MainConf(File file) {
        super(file);
    }
}