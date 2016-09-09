package com.zacstewart.civtrade;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import net.minecraft.util.math.BlockPos;

public class Shop {

	private String serverAddress;
	private String worldID;
	private BlockPos location;

	private Optional<Integer> which;
	private Optional<Integer> total;
	
	private Optional<String> inputItemName;
	private Optional<Integer> inputAmount;
	
	private Optional<String> outputItemName;
	private Optional<Integer> outputAmount;
	
	private Optional<Integer> exchangesAvailable;
	Shop(String serverAddress, String worldID, BlockPos location) {
		this.serverAddress = serverAddress;
		this.worldID = worldID;
		this.location = location;
	}
	
	void setInput(Integer amount, String itemName) {
		inputAmount = Optional.of(amount);
		inputItemName = Optional.of(itemName);
	}
	
	void setOutput(Integer amount, String itemName) {
		outputAmount = Optional.of(amount);
		outputItemName = Optional.of(itemName);
	}
	
	void setExchangesAvailable(Integer available) {
		exchangesAvailable = Optional.of(available);
	}
	
	void setWhichExchange(Integer which, Integer total) {
		this.which = Optional.of(which);
		this.total = Optional.of(total);
	}
	
	void save() {
		new Thread(new Runnable () {
			public void run () {
				writeToServer();
			}
		}).start();
	}
	
	private List<BasicNameValuePair> serialize() {
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("shop[server_address]", serverAddress));
		params.add(new BasicNameValuePair("shop[world_uuid", worldID));
		params.add(new BasicNameValuePair("shop[location_x]", Integer.toString(location.getX())));
		params.add(new BasicNameValuePair("shop[location_y]", Integer.toString(location.getY())));
		params.add(new BasicNameValuePair("shop[location_z]", Integer.toString(location.getZ())));
		params.add(new BasicNameValuePair("shop[which]", Integer.toString(which.get())));
		params.add(new BasicNameValuePair("shop[total]", Integer.toString(total.get())));
		params.add(new BasicNameValuePair("shop[input_item_name]", inputItemName.get()));
		params.add(new BasicNameValuePair("shop[output_item_name]", outputItemName.get()));
		params.add(new BasicNameValuePair("shop[input_amount]", Integer.toString(inputAmount.get())));
		params.add(new BasicNameValuePair("shop[output_amount]", Integer.toString(outputAmount.get())));
		params.add(new BasicNameValuePair("shop[exchanges_available]", Integer.toString(exchangesAvailable.get())));
		return params;
	}
	
	private void writeToServer() {
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(Civtrade.itemExchangesURL());
		try {
			post.setEntity(new UrlEncodedFormEntity(serialize(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		}
		HttpResponse response;
		try {
			response = httpClient.execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		HttpEntity entity = response.getEntity();
		System.out.println("Result: " + response);
	}
}
