package com.zacstewart.civtrade;

import java.util.logging.Level;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class GuiKeyHandler {
	static private KeyBinding keyOpenGui = new KeyBinding("Open shop search", Keyboard.KEY_I, "Civtrade");
	
	public GuiKeyHandler() {
		ClientRegistry.registerKeyBinding(keyOpenGui);
	}
    
    @SubscribeEvent
    public void keyInput(KeyInputEvent event) {
    	Minecraft minecraft = Minecraft.getMinecraft();
    	
    	if (keyOpenGui.isPressed() && minecraft.currentScreen == null) {
    		minecraft.displayGuiScreen(new ShopSearchGui());
    	}
    }
}
