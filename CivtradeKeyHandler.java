package com.zacstewart.civtrade;

import java.util.EnumSet;
import java.util.logging.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class CivtradeKeyHandler extends KeyHandler {
	private final Minecraft minecraft;
	private static KeyBinding keyShopSearchGui = new KeyBinding("Open Shop Search GUI", Keyboard.KEY_I);
	private static KeyBinding[] keyBindings = new KeyBinding[] {keyShopSearchGui};
	private static boolean[] keyBooleans = new boolean[] {false};

	public CivtradeKeyHandler(Minecraft minecraft) {
		super(keyBindings, keyBooleans);
		this.minecraft = minecraft;
	}

	@Override
	public String getLabel() {
		return "Civtrade Key Bindings";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb,
			boolean tickEnd, boolean isRepeat) {

		Civtrade.logger.log(Level.INFO, "Key");
		if (kb == keyShopSearchGui && this.minecraft.currentScreen == null) {
			this.minecraft.displayGuiScreen(new ShopSearchGui(this.minecraft));
		}
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
		// TODO Auto-generated method stub

	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

}
