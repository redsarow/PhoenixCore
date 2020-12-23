package fr.redsarow.phoenixCore.minecraft.config.jsonAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import fr.redsarow.phoenixCore.PhoenixCore;
import fr.redsarow.phoenixCore.minecraft.config.configFiles.WorldGroup;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.world.GameMode;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author redsarow
 */
public class WorldGroupAdapter extends TypeAdapter<WorldGroup> {

    private static final Logger LOGGER = PhoenixCore.getLogger(WorldGroupAdapter.class.getSimpleName());

    /**
     * Writes one JSON value (an array, object, string, number, boolean or null)
     * for {@code value}.
     *
     * @param out
     * @param worldGroup the Java object to write. May be null.
     */
    @Override
    public void write(JsonWriter out, WorldGroup worldGroup) throws IOException {
        if (worldGroup == null || worldGroup.groupMap == null) {
            return;
        }
        out.beginArray();
        for (String name : worldGroup.groupMap.keySet()) {
            WorldGroup.Group group = worldGroup.groupMap.get(name);

            out.beginObject().name(name).beginObject();

            out.name("worldsTeam")
                    .beginArray();
            for (String itWorldName : group.worldsTeam.keySet()) {
                Team itTeam = group.worldsTeam.get(itWorldName);
                out.beginObject()
                        .name(itWorldName)
                        .beginObject();
                if (itTeam != null) {
                    out.name("color")
                            .value(itTeam.getColor().getName())
                            .name("prefix")
                            .value(itTeam.getPrefix().getString());
                }
                out.endObject()
                        .endObject();
            }
            out.endArray(); // end array worldsTeam

            out.name("defaultTeam")
                    .beginObject()
                    .name("color")
                    .value(group.defaultTeam.getColor().getName())
                    .name("prefix")
                    .value(group.defaultTeam.getPrefix().getString())
                    .endObject();

            out.name("deadCount")
                    .value(group.deadCount);

            out.name("gameMode")
                    .value(group.gameMode.getName());

            out.endObject().endObject(); // end obj name
        }
        out.endArray();
    }

    /**
     * Reads one JSON value (an array, object, string, number, boolean or null)
     * and converts it to a Java object. Returns the converted object.
     *
     * @param in
     *
     * @return the converted Java object. May be null.
     */
    @Override
    public WorldGroup read(JsonReader in) throws IOException {
        WorldGroup out = new WorldGroup();

        in.beginArray();

        while (in.hasNext()) { // loop group names
            in.beginObject();
            String groupName = in.nextName();
            in.beginObject();

            WorldGroup.Group group = new WorldGroup.Group();
            group.name = groupName;

            while (in.hasNext()) { // loop group config
                switch (in.nextName()) {
                    case "worldsTeam":
                        group.worldsTeam = processWorlds(groupName, in);
                        break;
                    case "defaultTeam":
                        group.defaultTeam = initTeam(groupName, in);
                        break;
                    case "deadCount":
                        group.deadCount = in.nextBoolean();
                        break;
                    case "gameMode":
                        String gmName = in.nextString();
                        group.gameMode = GameMode.byName(gmName, GameMode.SPECTATOR);
                        break;
                }
            }
            out.groupMap.put(groupName, group);

            in.endObject();
            in.endObject();
        }

        in.endArray();

        return out;
    }

    private Map<String, Team> processWorlds(String groupName, JsonReader in) throws IOException {
        Map<String, Team> worldsTeam = new HashMap<>();
        in.beginArray();
        while (in.hasNext()) {
            in.beginObject();
            String nameWorld = in.nextName();
            Team team = initTeam(groupName + "." + nameWorld, in);
            worldsTeam.put(nameWorld, team);
            in.endObject();
        }
        in.endArray();
        return worldsTeam;
    }

    private Team initTeam(String name, JsonReader in) throws IOException {
        in.beginObject();
        Team team = null;
        if (in.hasNext()) {
            team = PhoenixCore.getInstance().getServer().getScoreboard().getTeam(name);
        }
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "color":
                    team.setColor(Formatting.byName(in.nextString()));
                    break;
                case "prefix":
                    team.setPrefix(new LiteralText(in.nextString()));
                    break;
            }
        }
        in.endObject();
        return team;
    }
}
