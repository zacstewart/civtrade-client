package com.zacstewart.civtrade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import net.minecraft.client.Minecraft;

public class Shop {
	Integer x, y, z;
	int dimension;
	String item;
	Integer buyAmount = 0, sellAmount = 0;
	Integer buyPrice = 0, sellPrice = 0;
	String buyCurrency = "", sellCurrency = "";
	String seller;
	
	public Shop(
			int x, int y, int z,
			int dimension,
			String item,
			int buyAmount,
			int sellAmount,
			int buyPrice,
			int sellPrice,
			String buyCurrency,
			String sellCurrency,
			String seller) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.dimension = dimension;
		this.item = item;
		this.buyAmount = buyAmount;
		this.sellAmount = sellAmount;
		this.buyPrice = buyPrice;
		this.sellPrice = sellPrice;
		this.buyCurrency = buyCurrency;
		this.sellCurrency = sellCurrency;
		this.seller = seller;
	}
	
	public void save() {
		new Thread(new Runnable () {
			public void run () {
				try {
					writeToServer();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
	private void writeToServer() throws IOException {
		URLConnection conn = civtradeShopsURL().openConnection();

		conn.setDoOutput(true);

		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		writer.write(requestParameters());
		writer.flush();

		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		int statusCode = ((HttpURLConnection) conn).getResponseCode();
		System.out.println("HTTP Status: " + statusCode);

		while ((line = reader.readLine()) != null) {
		    System.out.println(line);
		}
		writer.close();
		reader.close();
	}
	
	private URL civtradeShopsURL() throws MalformedURLException {
		if (Civtrade.debug ||  Civtrade.server.equals("127.0.0.1:25565")) {
			return new URL("http://localhost:5000/shops.json");
		} else {
			return new URL("http://civtrade.herokuapp.com/shops.json");
		}
	}
	
	private String requestParameters() {
		String[] params = new String[] {
				"item_name", 		this.item,
				"seller_username", 	this.seller,
				"buy_amount", 		this.buyAmount.toString(),
				"buy_price", 		this.buyPrice.toString(),
				"buy_currency", 	this.buyCurrency,
				"sell_amount", 		this.sellAmount.toString(),
				"sell_price", 		this.sellPrice.toString(),
				"sell_currency", 	this.sellCurrency,
				"server",			Civtrade.server,
				"world", 			this.getWorld(),
				"location_x", 		this.x.toString(),
				"location_y", 		this.y.toString(),
				"location_z", 		this.z.toString()};
		
		String paramString = "";
		for (int i = 0; i < params.length; i += 2) {
			if ((!params[i].equals("buy_amount") || this.buyAmount > 0)
					&& (!params[i].equals("sell_amount") || this.sellAmount > 0)) {
				paramString += ("shop[" + params[i] + "]=" + params[i + 1]);
				if (i < params.length - 1) { paramString += '&'; }
			}
		}
		return paramString;
	}
	
	private String getWorld() {
		String dimension;
		switch(this.dimension) {
		case -1:
			dimension = "nether";
			break;
		case 0: 
			dimension = "overworld";
			break;
		case 1:
			dimension = "end";
			break;
		default:
			dimension = "";
		}
		return dimension;
	}
}
