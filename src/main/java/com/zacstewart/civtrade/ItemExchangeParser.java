package com.zacstewart.civtrade;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.math.BlockPos;

public class ItemExchangeParser {
	private Optional<Shop> shop = Optional.empty();
	private final Pattern exchangesPresentPattern = Pattern.compile("\\A§r§e\\((\\d+)/(\\d+)\\) exchanges present.§r\\Z");
	private final Pattern inputPattern = Pattern.compile("\\A§r§eInput: §r§f(\\d+)\\s*(.+)§r\\Z");
	private final Pattern outputPattern = Pattern.compile("\\A§r§eOutput: §r§f(\\d+)\\s*(.+)§r\\Z");
	private final Pattern exchangesAvailablePattern = Pattern.compile("\\A§r§e(\\d+) exchanges available.§r\\Z");
	
	public void parse(String line) {
		Matcher matcher = exchangesPresentPattern.matcher(line);
		if (matcher.find()) {
			Integer which = Integer.parseInt(matcher.group(1), 10);
			Integer total = Integer.parseInt(matcher.group(2), 10);
			shop.get().setWhichExchange(which, total);
			return;
		}
		
		matcher = inputPattern.matcher(line);
		if (matcher.find()) {
			if (shop.isPresent()) {
				Integer amount = Integer.parseInt(matcher.group(1), 10);
				String item = matcher.group(2);
				shop.get().setInput(amount, item);
			}
			return;
		}
		
		matcher = outputPattern.matcher(line);
		if (matcher.find()) {
			if (shop.isPresent()) {
				Integer amount = Integer.parseInt(matcher.group(1), 10);
				String item = matcher.group(2);
				shop.get().setOutput(amount, item);
			}
			return;
		}
		
		matcher = exchangesAvailablePattern.matcher(line);
		if (matcher.find()) {
			if (shop.isPresent()) {
				Integer available = Integer.parseInt(matcher.group(1), 10);
				shop.get().setExchangesAvailable(available);
			}
			shop.get().save();
			return;
		}
	}
	
	public void newShop(String serverAddress, String worldId, BlockPos location) {
		shop = Optional.of(new Shop(serverAddress, worldId, location));
	}
}
