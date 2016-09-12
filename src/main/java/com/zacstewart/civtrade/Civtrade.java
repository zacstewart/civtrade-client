package com.zacstewart.civtrade;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import wafflestomper.wafflecore.WorldInfoEvent;

@Mod(modid = Civtrade.MODID, version = Civtrade.VERSION, name = Civtrade.NAME,
		dependencies = "required-after:WaffleCore")
public class Civtrade
{
    public static final String NAME = "Civtrade";
    public static final String MODID = "civtrade";
    public static final String VERSION = "0.0.1";
    public static Logger logger = Logger.getLogger("Civtrade");
	private static final boolean devEnv = (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
	private final ItemExchangeParser parser = new ItemExchangeParser();
	static String currentServerAddress;
	static String currentWorldID;
	
	private static String rootURL() {
		if (devEnv) {
			return "http://localhost:4000";
		} else {
			return "http://civtrade.herokuapp.com";
		}
	}
	
	static String itemExchangesURL() {
		return rootURL() + "/shops";
	}

	public static String searchURL() {
		return rootURL() + "/search";
	}
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new GuiKeyHandler());
    }
    
	@SubscribeEvent
	public void playerInteracted(LeftClickBlock event) {
		BlockPos location = new BlockPos(event.getHitVec());
		World world = event.getEntityPlayer().worldObj;
		Block block = world.getBlockState(location).getBlock();;
		if (block == Block.getBlockFromName("chest") ||
				block == Block.getBlockFromName("trapped_chest") ||
				block == Block.getBlockFromName("dispenser")) {
			parser.newShop(currentServerAddress, currentWorldID, location);
		}
	}
	
	@SubscribeEvent
	public void receivedChat(ClientChatReceivedEvent event) {
		parser.parse(event.getMessage().getFormattedText());
	}
	
    @SubscribeEvent
    public void worldInfoReceived(WorldInfoEvent event){
    	currentServerAddress = event.cleanServerAddress;
    	currentWorldID = event.worldID;
    }
}
