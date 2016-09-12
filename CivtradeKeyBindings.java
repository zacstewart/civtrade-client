package com.zacstewart.civtrade;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import cpw.mods.fml.client.registry.ClientRegistry;

public class CivtradeKeyBindings {
    public static KeyBinding inventory;

    public static void init() {
        inventory = new KeyBinding("key.inventory", Keyboard.KEY_I, "key.categories.civtrade");
        ClientRegistry.registerKeyBinding(inventory);
    }
}

