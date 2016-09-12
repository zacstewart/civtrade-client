package com.zacstewart.civtrade;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class ShopSearchGui extends GuiScreen {
	private static final int BUTTON_SEARCH = 0;
	private static final int BUTTON_COPY_COORDS = 1;
	
	private GuiTextField queryField;
	private String query;
	private ArrayList<Shop> shops = new ArrayList<Shop>();
	private ShopListGui shopListGui;
	private int selectedShopIndex;
	private boolean isFirstInit;
	
	ShopSearchGui() {
		super();
		query = "";
		selectedShopIndex = 0;
		isFirstInit = true;
	}
	
	@Override
	public void initGui() {
		queryField = new GuiTextField(0, fontRendererObj, 40, 60, this.width - 160, 15);
		queryField.setFocused(true);
		queryField.setText(query);
		buttonList.clear();
		buttonList.add(new GuiButton(BUTTON_SEARCH, width - 100, 58, 60, 18, "Search"));
		buttonList.add(new GuiButton(BUTTON_COPY_COORDS, 40, height - 25, 100, 18, "Copy Coordinates"));
		shopListGui = new ShopListGui(this, shops);
		if (isFirstInit) {
			performSearch();
			isFirstInit = false;
		}
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
		switch(button.id) {
			case BUTTON_SEARCH:
				performSearch();
				break;
			case BUTTON_COPY_COORDS:
				copySelectedShopCoords();
			default:
				break;
		}
	}
	
	void setShops(ArrayList<Shop> shops) {
		Civtrade.logger.log(Level.INFO, "Shops: " + shops.size());
		this.shops.clear();
		for (Shop shop : shops) {
			this.shops.add(shop);
		}
	}
	
	private void copySelectedShopCoords() {
		Optional<Shop> maybeShop = selectedShop();
		if (maybeShop.isPresent()) {
			Shop shop = maybeShop.get();
			String location = String.format("%d, %d, %d", shop.location.getX(), shop.location.getY(), shop.location.getZ());
			StringSelection selection = new StringSelection(location);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, selection);
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

	void selectShop(int index) {
		selectedShopIndex = index;
	}
	
	Optional<Shop> selectedShop() {
		if (selectedShopIndex < shops.size()) {
			return Optional.of(shops.get(selectedShopIndex));
		} else {
			return Optional.empty();
		}
	}
	
	boolean shopIsSelected(int index) {
		return selectedShopIndex == index;
	}
}
