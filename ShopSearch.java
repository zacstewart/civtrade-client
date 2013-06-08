package com.zacstewart.civtrade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;

public class ShopSearch {
	private static final JdomParser JDOM_PARSER = new JdomParser();
	private final String CIVTRADE_SHOPS_URL;
	private Integer x, y, z;
	private String query;
	private List<Shop> results;

	public ShopSearch(int x, int y, int z, String query) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.query = query;
		if (Civtrade.debug ||  Civtrade.server.equals("127.0.0.1:25565")) {
			this.CIVTRADE_SHOPS_URL ="http://localhost:5000/search.json";
		} else {
			this.CIVTRADE_SHOPS_URL = "http://civtrade.herokuapp.com/search.json";
		}
	}

	public void execute() {
		JsonRootNode json = null;
		try {
			json = this.queryServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.results = ShopApiParser.parseShops(json);
	}

	public List<Shop> getShops() {
		return this.results;
	}

	public static void search(int x, int y, int z, String query, ShopSearchGui delegate) {
		ShopSearch search = null;
		search = new ShopSearch(x, y ,z , query);
		search.execute();
		for(Shop shop: search.getShops()) {
			delegate.addShop(shop);
		}

		delegate.selectShopAtIndex(0);
	}
	
	private JsonRootNode queryServer() throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(CIVTRADE_SHOPS_URL + "?" + this.requestParameters()).openConnection();
		conn.setRequestMethod("GET");
		conn.setDoOutput(true);

		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		int statusCode = conn.getResponseCode();
		System.out.println("HTTP Status: " + statusCode);

		String body = "";
		String line;
		while ((line = reader.readLine()) != null) {
			body += line;
			System.out.println(line);
		}

		reader.close();
		JsonRootNode json = null;
		try {
			json = JDOM_PARSER.parse(body);
		} catch (InvalidSyntaxException e) {
			e.printStackTrace();
		}
		return json;
	}

	private String requestParameters() {
		String[] params = new String[] {
				"search",	this.query,
				"near_x",	this.x.toString(),
				"near_y",	this.y.toString(),
				"near_z",	this.z.toString()};

		String paramString = "";
		for (int i = 0; i < params.length; i += 2) {
			paramString += params[i] + "=" + params[i + 1];
			if (i < params.length - 1) { paramString += '&'; }
		}
		return paramString;
	}
}
