package com.zacstewart.civtrade.client;
import com.zacstewart.civtrade.CivtradeConnectionHandler;
import com.zacstewart.civtrade.CivtradeEventListener;
import com.zacstewart.civtrade.CommonProxy;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.network.NetworkRegistry;

public class ClientProxy extends CommonProxy {
        
        @Override
        public void registerRenderers() {
                MinecraftForgeClient.preloadTexture(ITEMS_PNG);
                MinecraftForgeClient.preloadTexture(BLOCK_PNG);
        }
        
        public void init() {
        	NetworkRegistry.instance().registerConnectionHandler(new CivtradeConnectionHandler());
            MinecraftForge.EVENT_BUS.register(new CivtradeEventListener());
        }
        
}