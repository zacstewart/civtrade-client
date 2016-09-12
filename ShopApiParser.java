package com.zacstewart.civtrade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;

public class ShopApiParser {
	public static Shop parseShop(JsonObject json) {
		int shopX = 0;
		int shopY = 0;
		int shopZ = 0;
		int buyAmount = 0;
		int sellAmount = 0;
		int buyPrice = 0;
		int sellPrice = 0;
		String buyCurrency = "";
		String sellCurrency = "";
		String seller = "";
		
		shopX = json.getAsInt("location_x");
		shopY = json.getAsInt("location_y");
		shopZ = json.getAsInt("location_z");
		String dimension = json.getAsString("world");
		
		String item = json.getAsString("item_name");
        buyAmount = json.getAsInt("buy_amount");
        sellAmount = json.getAsInt("sell_amount");
        buyPrice = json.getAsInt("buy_price");
        sellPrice = json.getAsInt("sell_price");
        buyCurrency = json.getAsString("buy_currency");
        sellCurrency = json.getAsString("sell_currency");
        seller = json.getAsString("seller_username");
		return new Shop(shopX,
				shopY,
				shopZ,
				dimension, item, buyAmount, sellAmount, buyPrice, sellPrice, buyCurrency, sellCurrency, seller);
	}
	public static List<Shop> parseShops(JsonArray json) {
		ArrayList shops = new ArrayList();
		for (JsonObject shop : json) {
			shops.add(parseShop(shop));
		}
		return shops;
	}
}
