package com.zacstewart.civtrade;

import net.minecraft.block.Block;
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
import wafflestomper.wafflecore.WorldInfoEvent;

@Mod(modid = Civtrade.MODID, version = Civtrade.VERSION, name = Civtrade.NAME,
		dependencies = "required-after:WaffleCore")
public class Civtrade
{
    public static final String NAME = "Civtrade";
    public static final String MODID = "civtrade";
    public static final String VERSION = "1.0";
	private static final boolean devEnv = (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
	private final ItemExchangeParser parser = new ItemExchangeParser();
	private static String currentServerAddress;
	private static String currentWorldID;
	
	static String itemExchangesURL() {
		if (devEnv) {
			return "http://localhost:4000/shops";
		} else {
			return "http://civtrade.herokuapp.com/shops";
		}
	}
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
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
