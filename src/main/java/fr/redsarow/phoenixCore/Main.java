package fr.redsarow.phoenixCore;

import net.fabricmc.api.DedicatedServerModInitializer;

/**
 * @author redsarow
 */
public class Main implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        System.out.println("Hello Fabric world!");
    }
}
