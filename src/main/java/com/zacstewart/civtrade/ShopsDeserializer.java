package com.zacstewart.civtrade;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ShopsDeserializer {
	private JsonElement json;

	public ShopsDeserializer(JsonElement json) {
		this.json = json;
	}

	public static ArrayList<Shop> deserialize(JsonElement json) {
		return new ShopsDeserializer(json).deserialize();
	}
	
	ArrayList<Shop> deserialize() {
		ArrayList<Shop> shops = new ArrayList<Shop>();
		JsonObject obj = json.getAsJsonObject();
		if (obj.has("shops")) {
			JsonArray elements = obj.get("shops").getAsJsonArray();
			for (JsonElement element : elements) {
				shops.add(ShopDeserializer.deserialize(element));
			}
		}
		return shops;
	}
}
