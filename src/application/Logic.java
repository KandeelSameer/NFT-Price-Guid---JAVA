package application;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import gui.MainPageController;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Logic {


	public static ArrayList<String> GetCollectionsSymbols() throws IOException {

		ArrayList<String> symbol_list = new ArrayList<String>();

		// using okhttp to collect 100 collections
		JSONObject json_elm = new JSONObject();

		OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				.writeTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
		Request request = new Request.Builder()
				.url("https://api-mainnet.magiceden.dev/v2/collections?offset=400&limit=100").method("GET", null)
				.build();

		Response response = client.newCall(request).execute();
		
		Object parsed = JSONValue.parse(response.body().string());
		
		response.close();
		JSONArray json_arr = (JSONArray) parsed;

		// inserting each symbol from the JSON array into the string list.
		for (int i = 0; i < json_arr.size(); i++) {
			json_elm = (JSONObject) json_arr.get(i);
			symbol_list.add((String) json_elm.get("symbol"));
		}
		
		return symbol_list;
	}

	public static String GetfloorPriceOpenSea(String name)  {
		// using okhttp to collect stats of collection
		
		try {
		System.out.println("before :"+name);
			name=name.replace('_', '-');
			System.out.println("after :"+name);
			OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
					.writeTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
			Request request = new Request.Builder().url("https://api.opensea.io/api/v1/collection/" + name + "/stats")
					.get().addHeader("Accept", "application/json").build();
			// executing the api command
			 Response response = client.newCall(request).execute();

			// parsing the string into json
			Object parsed = JSONValue.parse(response.body().string());
			response.close();
			JSONObject parsed_js = (JSONObject) parsed;

			// checking for data validation or availability
			if (!parsed_js.containsKey("stats") || parsed_js == null) {
				return "N/A";
			}

			// accessing to "stats" attribute
			parsed_js = (JSONObject) parsed_js.get("stats");

			// gathering floor_price
			Double floor_price = (Double) parsed_js.get("floor_price");
			System.out.println(floor_price);
			
			if (String.valueOf(floor_price) == "null"||String.valueOf(floor_price) == ""||String.valueOf(floor_price)==null||floor_price==null) {
				return "N/A";
			}
			
			return String.valueOf(floor_price);
		} catch (Exception e) {
			return "N/A";
		}
	}

	public static String GetfloorPriceMagicEden(String name) {
		// using okhttp to collect stats of collection
		
		try {
			OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
					.writeTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
			Request request = new Request.Builder()
					.url("https://api-mainnet.magiceden.dev/v2/collections/" + name + "/stats").method("GET", null)
					.build();
			// executing the api command
			Response response = client.newCall(request).execute();

			// parsing the string into json
			Object parsed = JSONValue.parse(response.body().string());
			response.close();
			JSONObject parsed_js = (JSONObject) parsed;
			
			// checking for data validation or availability
			if (!parsed_js.containsKey("floorPrice") || parsed_js == null) {
				System.out.println("not found in MagicEden");
				return "N/A";
			}
		
			// gathering floor_price
			Long floor_price = (Long) parsed_js.get("floorPrice");
			//double floor_price =  Double.parseDouble(parsed_js.get("floorPrice").toString()) ;
			
			if (String.valueOf(floor_price) == "null"||String.valueOf(floor_price) =="") {
				System.out.println("not found in MagicEden");
				return "N/A";
			}
			int count = countZeros(String.valueOf(floor_price));
		    String temp;
			String a;
			double fixedNumber=0;
			double fixedAfterDigit;
			switch(count) {
			case 7:
				
				 temp = String.valueOf(floor_price);
				 if(temp.length()!=8&&temp.length()!=9) {
					 //a=Character.toString(temp.charAt(0));
					// fixedNumber=Double.parseDouble(a);
					 fixedAfterDigit=Double.parseDouble(temp.substring(0, 3));
					fixedNumber+=fixedAfterDigit/100;}
				 else if(temp.length()==8){
					 fixedAfterDigit=Double.parseDouble(temp.substring(0, 1));
						fixedNumber+=fixedAfterDigit/100;
				 }
				 else if(temp.length()==9){
					 fixedAfterDigit=Double.parseDouble(temp.substring(0, 2));
						fixedNumber+=fixedAfterDigit/100;
				 }
					break;
			case 6:		
				 temp = String.valueOf(floor_price);
				 if(temp.length()==8) {
				 fixedNumber=0;
				 fixedAfterDigit=Double.parseDouble(temp.substring(0, 2));
				fixedNumber+=fixedAfterDigit/1000;
				 }
				 else {
					
					 fixedNumber=0;
					 fixedAfterDigit=Double.parseDouble(temp.substring(0, 2));
					fixedNumber+=fixedAfterDigit/100;
				 }
				break;
			case 9:
				 temp = String.valueOf(floor_price);
				 if(temp.length()==10)
				 {
					 fixedAfterDigit=Double.parseDouble(temp.substring(0, 1));
						fixedNumber+=fixedAfterDigit/1;//  1 it mean dont divide
				 }
				break;
			case 10:
				 temp = String.valueOf(floor_price);
				 if(temp.length()==11)
				 {
					 fixedAfterDigit=Double.parseDouble(temp.substring(0, 2));
						fixedNumber+=fixedAfterDigit/1;//  1 it mean dont divide
				 }
				break;
			case 4:
				 temp = String.valueOf(floor_price);
				 fixedNumber=0;
				 fixedAfterDigit=Double.parseDouble(temp.substring(0, 3));
				fixedNumber+=fixedAfterDigit/10000;
				break;
			case 8:
				 temp = String.valueOf(floor_price);
				 if(temp.length()==10)
				 {
					 fixedAfterDigit=Double.parseDouble(temp.substring(0, 3));
						fixedNumber+=fixedAfterDigit/100;//  1 it mean dont divide
				 }
				 else {
					 fixedAfterDigit=Double.parseDouble(temp.substring(0, 3));
						fixedNumber+=fixedAfterDigit/1000;
				 }
				break ;
			default :
				 temp = String.valueOf(floor_price);
				// a=Character.toString(temp.charAt(0));
				// fixedNumber=Double.parseDouble(a);
				 if(temp.length()==10)
				 {
					 fixedAfterDigit=Double.parseDouble(temp.substring(0, 3));
						fixedNumber+=fixedAfterDigit/100;
				 }
				 else {
				 fixedAfterDigit=Double.parseDouble(temp.substring(0, 3));
				fixedNumber+=fixedAfterDigit/1000;
				 }
			}
			//fix the number first remove 9
		//	String temp = String.valueOf(floor_price);
		//	String a=Character.toString(temp.charAt(0));
		//	double fixedNumber=Double.parseDouble(a);
			///double fixedAfterDigit=Double.parseDouble(temp.substring(1, 4));
			
			//fixedNumber+=fixedAfterDigit/1000;
			
			
			return String.valueOf((double)((double)floor_price/1000000000));
		} catch (Exception e) {
			System.out.println("not found in MagicEden error");
			e.printStackTrace();
			return "N/A";
		}
	}


	// convert an ETH to SOL
	public static String floorPriceETHtoSOL(String s) {
		if(s==null||s=="N/A")
			return "N/A";
		Double CurrenyNow,priceETH=Double.parseDouble(s),priceSOL;
			OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
			.writeTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
	Request request = new Request.Builder()
			.url("https://min-api.cryptocompare.com/data/price?fsym=ETH&tsyms=SOL,USD,EUR").method("GET", null)
			.build();
	// executing the api command
	try {
	Response response = client.newCall(request).execute();

	// parsing the string into json
	Object parsed = JSONValue.parse(response.body().string());
	response.close();
	JSONObject parsed_js = (JSONObject) parsed;
	CurrenyNow=Double.parseDouble(parsed_js.get("SOL").toString());

	//System.out.println(parsed_js.get("SOL"));
	priceSOL=CurrenyNow*priceETH;
	
	return String.format("%.2f",priceSOL);
	}catch(Exception e) {
		e.printStackTrace();
	}
		return "";
	}

	//get the differnce between 2 sites prices
	public static Double getDiffertns(String openSea,String magicEden) {
		if(openSea=="N/A"||magicEden=="N/A"||openSea==null||magicEden==null)
			return 0.0;
		double priceEden=Double.parseDouble( magicEden);
		double priceOpenSea=Double.parseDouble(openSea);
		
		if(priceOpenSea>priceEden)//negativc
		{
			String strDouble = String.format("%.2f", (double)((1-(priceOpenSea/priceEden))*(100)));
			return Double.parseDouble(strDouble);// it should be a negative
		}
		else {//postive
			String strDouble = String.format("%.2f", (double)((priceEden/priceOpenSea)-1)*(100));
			return Double.parseDouble(strDouble);// it shloud be postive (if eden > open sea then when we sub 1 from the result it will be negative multilbe it by -1 = postive
		}
		
	}
	private static int countZeros(String valueOf) {
		int count=0;
		for(Character c:valueOf.toCharArray())
		{
			if(c=='0')
				count++;
		}
		return count;
	}

	public static String SOLtoUSD() {
		OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
				.writeTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
		Request request = new Request.Builder()
				.url("https://min-api.cryptocompare.com/data/price?fsym=ETH&tsyms=SOL,USD,EUR").method("GET", null)
				.build();
		// executing the api command
		try {
		Response response = client.newCall(request).execute();

		// parsing the string into json
		Object parsed = JSONValue.parse(response.body().string());
		response.close();
		JSONObject parsed_js = (JSONObject) parsed;
		return parsed_js.get("USD").toString();
		}
		catch(Exception e){
			return "";
		}
		
	}
}
