package fr.redsarow.phoenixCore.minecraft;

import fr.redsarow.phoenixCore.minecraft.config.ConfigManager;
import fr.redsarow.phoenixCore.minecraft.config.configFiles.WorldGroup;

import java.util.Optional;
import java.util.Set;

/**
 * @author redsarow
 */
public class WorldGroupManager {

    private static WorldGroupManager INSTANCE;
    private WorldGroup conf;

    private WorldGroupManager() {
        conf = ConfigManager.getInstance().iniConfig("worldGroup.json", WorldGroup.class);
    }

    public static WorldGroupManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WorldGroupManager();
        }
        return INSTANCE;
    }

    /**
     * @return group or null
     */
    public Optional<WorldGroup.Group> findGroupByWorldName(String worldName) {
        for (WorldGroup.Group group : conf.groupMap.values()) {
            if (group.worldsTeam.containsKey(worldName)) {
                return Optional.of(group);
            }
        }
        return Optional.empty();
    }

    /**
     * @return group or null
     */
    public WorldGroup.Group findGroupByName(String worldGroupName) {
        return conf.groupMap.get(worldGroupName);
    }

    /**
     * @return list of group name
     */
    public Set<String> getListNameGroups() {
        return conf.groupMap.keySet();
    }
}
