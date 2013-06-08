package com.zacstewart.civtrade;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShopSignParser {
	final Pattern SHOP_ITEM_PATTERN = Pattern.compile("^\\[(.+)\\]$");
	final Pattern SHOP_BUY_PATTERN = Pattern.compile("^Buy (\\d+) for (\\d+)([a-z])$");
	final Pattern SHOP_SELL_PATTERN = Pattern.compile("^Sell (\\d+) for (\\d+)([a-z])$");

	String signText[];

	String item = "";
	int buyAmount = 0;
	int sellAmount = 0;
	int buyPrice = 0;
	int sellPrice = 0;
	String buyCurrency = "";
	String sellCurrency = "";
	String seller = "";

	public ShopSignParser(String signText[]) {
		this.signText = signText;
	}

	public static ShopSignParser parse(String signText[]) {
		ShopSignParser parser = new ShopSignParser(signText);
		parser.parse();
		return parser;
	}

	public ShopSignParser parse() {
		Matcher itemMatcher = SHOP_ITEM_PATTERN.matcher(this.signText[0]);
		if (itemMatcher.find()) {
			this.item = itemMatcher.group(1);
		} else {
			System.out.println("No item");
			return this;
		}

		Matcher buySellMatcher;
		for (int i = 1; i < 3; i++) {
			buySellMatcher = SHOP_BUY_PATTERN.matcher(signText[i]);
			if (buySellMatcher.find()) {
				this.buyAmount = Integer.parseInt(buySellMatcher.group(1), 10);
				this.buyPrice = Integer.parseInt(buySellMatcher.group(2), 10);
				this.buyCurrency = buySellMatcher.group(3);
				continue;
			}

			buySellMatcher = SHOP_SELL_PATTERN.matcher(signText[i]);
			if (buySellMatcher.find()) {
				this.sellAmount = Integer.parseInt(buySellMatcher.group(1), 10);
				this.sellPrice = Integer.parseInt(buySellMatcher.group(2), 10);
				this.sellCurrency = buySellMatcher.group(3);
				continue;
			}
		}

		if (this.buyAmount == 0 && this.sellAmount == 0) {
			System.out.println("Not buying or selling");
			return this;
		}

		if (signText[3].isEmpty()) {
			System.out.println("No seller");
			return this;
		} else {
			this.seller = signText[3];
		}
		return this;
	}

	public boolean isShop() {
		if (this.item.isEmpty()) { return false; }
		if (this.buyAmount == 0 && this.sellAmount == 0) { return false; }
		if (this.seller.isEmpty()) { return false; }
		return true;
	}
}
