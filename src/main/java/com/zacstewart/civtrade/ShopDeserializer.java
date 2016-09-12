package com.zacstewart.civtrade;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.util.math.BlockPos;

public class ShopDeserializer {
	private JsonElement json;

	public ShopDeserializer(JsonElement json) {
		this.json = json;
	}

	public static Shop deserialize(JsonElement json) {
		return new ShopDeserializer(json).deserialize();
	}
	
	Shop deserialize() {
		JsonObject obj = json.getAsJsonObject();
		String worldID = obj.get("world_uuid").getAsString();
		double x = obj.get("location_x").getAsDouble();
		double y = obj.get("location_y").getAsDouble();
		double z = obj.get("location_z").getAsDouble();
		BlockPos location = new BlockPos(x, y, z);
		int inputAmount = obj.get("input_amount").getAsInt();
		String inputItemName = obj.get("input_item_name").getAsString();
		int outputAmount = obj.get("output_amount").getAsInt();
		String outputItemName = obj.get("output_item_name").getAsString();
		return new Shop(worldID, location,
				inputAmount, inputItemName, outputAmount, outputItemName);
	}
}
