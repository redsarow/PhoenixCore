package fr.redsarow.phoenixCore.minecraft.util;

import net.minecraft.server.world.ServerWorld;

/**
 * @author redsarow
 */
public class ModUtils {

    public static boolean isEmpty(Object obj) {
        return obj == null;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.equalsIgnoreCase("");
    }

    public static String getWorldName(ServerWorld world){
        return world.getRegistryKey().getValue().getPath();
    }
}
