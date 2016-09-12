package com.zacstewart.civtrade;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ShopSearch {
	private double x, y, z;
	private String query;
	private ShopSearchGui delegate;
	private static final JsonParser parser = new JsonParser();
	
	public ShopSearch(double x, double y, double z, String query, ShopSearchGui delegate) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.query = query;
		this.delegate = delegate;
	}
	
	public static void search(double x, double y, double z, String query, ShopSearchGui delegate) {
		new ShopSearch(x, y, z, query, delegate).execute();
	}
	
	public void execute() {
		try {
			delegate.setShops(ShopsDeserializer.deserialize(fetchResults()));
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}
	}
	
	private JsonElement fetchResults() throws ParseException, IOException, URISyntaxException {
		HttpClient httpClient = HttpClients.createDefault();
		HttpGet get = new HttpGet(searchURL());
		get.addHeader("Accept", "application/json");
		HttpResponse response = httpClient.execute(get);
		Civtrade.logger.log(Level.INFO, "Shop search response: " + response.getStatusLine());
		String json = EntityUtils.toString(response.getEntity(), "UTF-8");
		return parser.parse(json);
	}
	
	private URI searchURL() throws URISyntaxException {
		URIBuilder builder = new URIBuilder(Civtrade.searchURL());
		builder.setParameter("world_uuid", Civtrade.currentWorldID);
		builder.setParameter("near_x", Double.toString(x));
		builder.setParameter("near_y", Double.toString(y));
		builder.setParameter("near_z", Double.toString(z));
		builder.setParameter("search", query);
		return builder.build();
	}
}
