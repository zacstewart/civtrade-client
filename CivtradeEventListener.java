package com.zacstewart.civtrade;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zacstewart.civtrade.client.ClientProxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;


public class CivtradeEventListener {
	
	
    @ForgeSubscribe
    public void chatReceived(ClientChatReceivedEvent event) {
    	System.out.println(event.message);
    }
    
    @ForgeSubscribe
    public void playerInteracted(PlayerInteractEvent event) {
    	if (!Civtrade.multiplayer) { return; }
    	
    	int x = event.x;
    	int y = event.y;
    	int z = event.z;
    	World world = event.entityPlayer.worldObj;
    	int dimension = world.getWorldInfo().getDimension();
    	int blockId = world.getBlockId(x, y, z);
    	System.out.println("Server:");
    	System.out.println(Civtrade.server);

    	if (blockId == Block.signPost.blockID || blockId == Block.signWall.blockID) {
    		String signText[] = ((TileEntitySign) world.getBlockTileEntity(x, y, z)).signText;
    		
    		ShopParser parser = ShopParser.parse(signText);
    		
    		if (parser.isShop()) {
				Shop shop = new Shop(
						x, y, z,
						dimension,
						parser.item,
						parser.buyAmount,
						parser.sellAmount,
						parser.buyPrice,
						parser.sellPrice,
						parser.buyCurrency,
						parser.sellCurrency,
						parser.seller);
    			
				shop.save();
    		}
    	}
    }
}
