package com.zacstewart.civtrade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import argo.jdom.JsonNode;
import argo.jdom.JsonNodeDoesNotMatchJsonNodeSelectorException;
import argo.jdom.JsonRootNode;

public class ShopApiParser {
	public static Shop parseShop(JsonNode json) {
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
		
		shopX = (int) Float.parseFloat(json.getNumberValue("location_x"));
		shopY = (int) Float.parseFloat(json.getNumberValue("location_y"));
		shopZ = (int) Float.parseFloat(json.getNumberValue("location_z"));
		String dimension = json.getStringValue("world");
		
		String item = json.getStringValue("item_name");
		try {
			buyAmount = (int) Float.parseFloat(json.getNumberValue("buy_amount"));
		} catch (JsonNodeDoesNotMatchJsonNodeSelectorException e) {
			e.printStackTrace();
		}
		try {
			sellAmount = (int) Float.parseFloat(json.getNumberValue("sell_amount"));
		} catch (JsonNodeDoesNotMatchJsonNodeSelectorException e) {
			e.printStackTrace();
		}
		
		try {
			buyPrice = (int) Float.parseFloat(json.getNumberValue("buy_price"));
		} catch (JsonNodeDoesNotMatchJsonNodeSelectorException e) {
			e.printStackTrace();
		}
		try {
			sellPrice = (int) Float.parseFloat(json.getNumberValue("sell_price"));
		} catch (JsonNodeDoesNotMatchJsonNodeSelectorException e) {
			e.printStackTrace();
		}
		try {
			buyCurrency = json.getStringValue("buy_currency");
		} catch (JsonNodeDoesNotMatchJsonNodeSelectorException e) {
			e.printStackTrace();
		}
		try {
			sellCurrency = json.getStringValue("sell_currency");
		} catch (JsonNodeDoesNotMatchJsonNodeSelectorException e) {
			e.printStackTrace();
		}
		
		try {
			seller = json.getStringValue("seller_username");
		} catch (JsonNodeDoesNotMatchJsonNodeSelectorException e) {
			e.printStackTrace();
		}

		return new Shop(shopX,
				shopY,
				shopZ,
				dimension, item, buyAmount, sellAmount, buyPrice, sellPrice, buyCurrency, sellCurrency, seller);
	}
	public static List<Shop> parseShops(JsonRootNode json) {
		ArrayList shops = new ArrayList();
		for (JsonNode shop : json.getElements()) {
			shops.add(parseShop(shop));
		}
		return shops;
	}
}
