package com.zacstewart.civtrade;

import java.util.ArrayList;
import java.util.logging.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

public class ShopListGui extends GuiScrollingList {
	private ShopSearchGui parent;
	private ArrayList<Shop> shops;
	private static final int ENTRY_HEIGHT = 30;

	public ShopListGui(ShopSearchGui parent, ArrayList<Shop> shops) {
		super(parent.mc, parent.width - 80, parent.height - 115,
				80, parent.height - 35, 40, ENTRY_HEIGHT, parent.width, parent.height);
		this.parent = parent;
		this.shops = shops;
	}

	@Override
	protected int getSize() {
		return shops.size();
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick) {
		parent.selectShop(index);
		
	}

	@Override
	protected boolean isSelected(int index) {
		return parent.shopIsSelected(index);
	}

	@Override
	protected void drawBackground() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
		FontRenderer font = this.parent.getFontRenderer();
		int playerX = (int) parent.mc.thePlayer.posX;
		int playerY = (int) parent.mc.thePlayer.posY;
		int playerZ = (int) parent.mc.thePlayer.posZ;
		Shop shop = shops.get(slotIdx);
		String input = "§r§eInput: §r" + shop.inputDescription();
		String output = "§r§eOutput: §r" + shop.outputDescription();
		String location = String.format("%d, %d, %d", shop.location.getX(), shop.location.getY(), shop.location.getZ());
		String distance = String.format("%.1fm away", shop.location.getDistance(playerX, playerY, playerZ));
		
		int locationWidth = font.getStringWidth(location);
		int distanceWidth = font.getStringWidth(distance);
		int rightCellWidth = Integer.max(locationWidth, distanceWidth);
		
		font.drawString(font.trimStringToWidth(input, listWidth - rightCellWidth - 15), this.left + 3, slotTop + 2, 0xFFFFFF);
		font.drawString(font.trimStringToWidth(output, listWidth - rightCellWidth - 15), this.left + 3, slotTop + 16, 0xFFFFFF);
		font.drawString(font.trimStringToWidth(location, rightCellWidth), this.right - locationWidth - 10, slotTop + 3, 0xFFFFFF);
		font.drawString(font.trimStringToWidth(distance, rightCellWidth), this.right - distanceWidth - 10, slotTop + 16, 0xFFFFFF);
	}

}
