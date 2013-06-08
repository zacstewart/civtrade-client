package com.zacstewart.civtrade;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid="Civtrade", name="Civtrade", version="0.0.0")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class Civtrade {
	@Instance("Civtrade")
	public static Civtrade instance;

	@SidedProxy(clientSide="com.zacstewart.civtrade.client.ClientProxy",
			serverSide="com.zacstewart.civtrade.CommonProxy") 
	public static CommonProxy proxy;
	public CivtradeConfiguration config;

	// TODO: this should be part of a config object
	public static String server = null;
	public static boolean multiplayer = false;
	public static boolean debug = false;
	public static Logger logger;

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		this.config = new CivtradeConfiguration(event.getSuggestedConfigurationFile());
		this.logger = Logger.getLogger("Civtrade");
		this.logger.setParent(FMLLog.getLogger());
	}

	@Init
	public void load(FMLInitializationEvent event) {
		proxy.registerRenderers();
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		proxy.init(this);
	}
}