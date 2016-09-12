package com.zacstewart.civtrade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class ShopSearchGui extends GuiScreen {
	private static final int BUTTON_SEARCH = 0;
	private GuiTextField queryField;
	private String query = "";
	private ArrayList<Shop> shops = new ArrayList<Shop>();
	private ShopListGui shopListGui;
	private int selectedShopIndex = 0;
	
	@Override
	public void initGui() {
		queryField = new GuiTextField(0, fontRendererObj, 40, 60, this.width - 160, 15);
		queryField.setFocused(true);
		queryField.setText("");
		buttonList.clear();
		buttonList.add(new GuiButton(BUTTON_SEARCH, this.width - 100, 58, 60, 18, "Search"));
		shopListGui = new ShopListGui(this, shops);
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		
		queryField.textboxKeyTyped(typedChar, keyCode);
		query = queryField.getText();
		
		if (keyCode == Keyboard.KEY_RETURN) {
			performSearch();
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(this.fontRendererObj, "Shop Search", width / 2, 40, 0xFFFFFF);
		queryField.drawTextBox();
		shopListGui.drawScreen(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void updateScreen() {
		queryField.updateCursorCounter();
		super.updateScreen();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == BUTTON_SEARCH) {
			performSearch();
		}
	}
	
	void setShops(ArrayList<Shop> shops) {
		Civtrade.logger.log(Level.INFO, "Shops: " + shops.size());
		this.shops.clear();
		for (Shop shop : shops) {
			this.shops.add(shop);
		}
	}
	
	private void performSearch() {
		final double x = mc.thePlayer.posX;
		final double y = mc.thePlayer.posY;
		final double z = mc.thePlayer.posZ;
		final ShopSearchGui delegate = this;
		
		shops.clear();
		selectedShopIndex = 0;
		
		new Thread(new Runnable() {
			public void run() {
				ShopSearch.search(x, y, z, query, delegate);
			}
		}).start();
	}

	FontRenderer getFontRenderer() {
		return this.fontRendererObj;
	}

	public void selectShop(int index) {
		selectedShopIndex = index;
	}
	
	public boolean shopIsSelected(int index) {
		return selectedShopIndex == index;
	}
}
