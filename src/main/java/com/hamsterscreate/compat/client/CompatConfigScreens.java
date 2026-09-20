package com.hamsterscreate.compat.client;

import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModContainer;

public final class CompatConfigScreens {
    private CompatConfigScreens() {
    }

    public static void register(ModContainer container) {
        container.registerExtensionPoint(
            ConfigScreenHandler.ConfigScreenFactory.class,
            () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, parent) -> new CompatConfigScreen(parent))
        );
    }
}
