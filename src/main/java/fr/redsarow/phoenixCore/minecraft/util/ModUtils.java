package fr.redsarow.phoenixCore.minecraft.util;

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
}
