package fr.redsarow.phoenixCore.minecraft.config.jsonAdapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import fr.redsarow.phoenixCore.minecraft.config.configFiles.WorldGroup;
import net.minecraft.scoreboard.Team;

import java.io.IOException;
import java.util.Map;

/**
 * @author redsarow
 */
public class WorldGroupAdapter extends TypeAdapter<Map<String, WorldGroup.Group>> {

    /**
     * Writes one JSON value (an array, object, string, number, boolean or null)
     * for {@code value}.
     *
     * @param out
     * @param groupMap the Java object to write. May be null.
     */
    @Override
    public void write(JsonWriter out, Map<String, WorldGroup.Group> groupMap) throws IOException {
        if (groupMap == null) {
            return;
        }
        out.beginArray();
        for (String name : groupMap.keySet()) {
            WorldGroup.Group group = groupMap.get(name);

            out.beginObject().name(name).beginObject();

            out.name("worldsTeam")
                    .beginArray();
            for (String itWorldName : group.worldsTeam.keySet()) {
                Team itTeam = group.worldsTeam.get(itWorldName);
                out.beginObject()
                        .name(itWorldName)
                        .beginObject();
                if(itTeam != null){
                    out.name("color")
                            .value(itTeam.getColor().getName())
                            .name("prefix")
                            .value(itTeam.getPrefix().getString());
                }
                out.endObject()
                        .endObject();
            }
            out.endArray();

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

            out.endObject().endObject();
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
    public Map<String, WorldGroup.Group> read(JsonReader in) throws IOException {
       /* if (!in.hasNext()) {
            return null;
        }
        String color;
        String prefix;
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "color":
                    color = in.nextString();
                    break;
                case "prefix":
                    prefix = in.nextString();
                    break;
            }
        }
        PhoenixCore.getInstance().getServer().getScoreboard().getTeam();*/
        return null;
    }
}
