package com.zacstewart.civtrade;

import java.util.ArrayList;

import com.zacstewart.civtrade.Shop;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.GuiTextField;

public class ShopSearchGui extends GuiScreen {
	private final int BUTTON_SEARCH = 0;
	private final Minecraft minecraft;
	
	private GuiTextField queryField;
	private boolean acceptingText = false;
	private String query = "";
	private ArrayList<Shop> shops;
	private ShopListGui shopListGui;
	private int selectedShopIndex;
	private Shop selectedShop = null;
	private boolean closed = false;

	public ShopSearchGui(Minecraft minecraft) {
		this.minecraft = minecraft;
		this.shops = new ArrayList();
	}

	public void initGui() {
		queryField = new GuiTextField(fontRenderer, 40, 60, this.width - 180, 20);
		queryField.setFocused(true);
		queryField.setText("");

		this.buttonList.clear();
		this.buttonList.add(new GuiButton(BUTTON_SEARCH, this.width - 100, 60, 60, 20, "Search"));
		
		this.shopListGui = new ShopListGui(this, shops);
	}

	public void onGuiClosed() {
		this.closed = true;
	}

	public void keyTyped(char c, int i) {
		super.keyTyped(c, i);

		if (!acceptingText) {
			acceptingText = true;
			return;
		}

		queryField.textboxKeyTyped(c, i);
		this.query = queryField.getText();
	}

	protected void actionPerformed(GuiButton button) {
		if (button.id == BUTTON_SEARCH) {
			final int x = (int) this.minecraft.thePlayer.posX;
			final int y = (int) this.minecraft.thePlayer.posY;
			final int z = (int) this.minecraft.thePlayer.posZ;
			final ShopSearchGui delegate = this;
			this.shops.clear();
			new Thread(new Runnable() {
				public void run() {
					ShopSearch.search(x, y, z, query, delegate);
				}
			}).start();
			
		}
	}
	
	public void addShop(Shop shop) {
		if (this.closed) { return; }
		shops.add(shop);
	}

	public void updateScreen() {
		queryField.updateCursorCounter();
	}

	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, "Shop Search", this.width / 2, 40, 16777215);

		this.queryField.drawTextBox();
		
		this.shopListGui.drawScreen(par1, par2, par3);
		
		if (this.selectedShop != null) {
	    	String distance = String.format("%.2f meters", this.selectedShop.distanceFrom(this.minecraft.thePlayer));
	    	String location = String.format("%d, %d, %d", this.selectedShop.x, this.selectedShop.y, this.selectedShop.z);
			this.fontRenderer.drawString(this.fontRenderer.trimStringToWidth(this.selectedShop.item, this.width - 260), 220 , 100, 0xFFFFFF);
			this.fontRenderer.drawString(this.fontRenderer.trimStringToWidth(location, this.width - 260), 220 , 110, 0xFFFFFF);
			this.fontRenderer.drawString(this.fontRenderer.trimStringToWidth(distance, this.width - 260), 220 , 120, 0xCCCCCC);
		}
		
		super.drawScreen(par1, par2, par3);
	}
	
	public Minecraft getMinecraftInstance() {
		return this.minecraft;
	}
	
	public FontRenderer getFontRenderer() {
		return this.fontRenderer;
	}

	public void selectShopAtIndex(int index) {
		if (this.closed) { return; }
		this.selectedShopIndex = index;
		if (index >= 0 && index < shops.size()) {
			this.selectedShop = shops.get(index);
		}
		
	}

	public boolean shopIsSelected(int index) {
		return this.selectedShopIndex == index;
	}
}
