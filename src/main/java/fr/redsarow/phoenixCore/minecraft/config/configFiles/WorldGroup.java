package fr.redsarow.phoenixCore.minecraft.config.configFiles;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.Since;
import fr.redsarow.phoenixCore.PhoenixCore;
import fr.redsarow.phoenixCore.minecraft.config.AConfigFile;
import fr.redsarow.phoenixCore.minecraft.config.jsonAdapter.WorldGroupAdapter;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author redsarow
 */
@JsonAdapter(WorldGroupAdapter.class)
public class WorldGroup extends AConfigFile {

    @Since(1.0)
    @Expose
    public Map<String, Group> groupMap = new HashMap<>();

    public WorldGroup(File file) {
        super(file, 1.0);

        //default Conf
        Group defGroup = new Group();
        defGroup.worldsTeam = new HashMap<>();
        defGroup.worldsTeam.put("world", null);
        defGroup.worldsTeam.put("world_nether", null);
        defGroup.worldsTeam.put("world_the_end", null);
        defGroup.defaultTeam = PhoenixCore.getInstance().getServer().getScoreboard().getTeam("Survie");
        if (defGroup.defaultTeam == null) {
            defGroup.defaultTeam = PhoenixCore.getInstance().getServer().getScoreboard().addTeam("Survie");
        }
        defGroup.defaultTeam.setColor(Formatting.GREEN);

        groupMap.put("Survie", defGroup);
    }

    public WorldGroup() {
        this(null);
    }

    public static class Group {

        /**
         * Lis of worlds and potential Team
         */
        public Map<String, Team> worldsTeam = new HashMap<>();

        public Team defaultTeam = null;

        public boolean deadCount = true;

        public GameMode gameMode = GameMode.SURVIVAL;
    }
}
