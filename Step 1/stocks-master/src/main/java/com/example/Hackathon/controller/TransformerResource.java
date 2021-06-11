package com.example.Hackathon.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.util.IOUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/transformers")
public class TransformerResource {

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/stocks")
	@ApiOperation("Let the battle begin!!!! Go Go Go!!!")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
	public ResponseEntity<String> fetchStocks(@RequestParam("symbols") String symbols) throws IOException {
		List stockList = new ArrayList();
		//int count =100;
		List<StockDetails> allStocks= getAllStocks();
		for(StockDetails stock: allStocks){
			//count--;
			HttpClient client = HttpClientBuilder.create().build();
			String url = "https://cloud.iexapis.com/stable/stock/"+stock.getSymbol()+"/quote?token=pk_53f96e249be3442d803886bb59504119";
			HttpGet request = new HttpGet(url);
			// add request header
			org.apache.http.HttpResponse response = null;
			response = client.execute(request);

			BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));

			StringBuilder result = new StringBuilder();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			stockList.add(result);
			System.out.println("FETCHED: " + stock.getSymbol());
//			if (count==0)
//				break;
		}
		return ResponseEntity.ok()
				.body(stockList.toString());
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/stocksbak")
	@ApiOperation("Let the battle begin!!!! Go Go Go!!!")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
	public ResponseEntity<String> fetchStocksBak(@RequestParam("symbols") String symbols) throws IOException {
		getAllStocks();
		List stockList = new ArrayList();
		for(String symbol: symbols.split(",")){
			HttpClient client = HttpClientBuilder.create().build();
			String url = "https://cloud.iexapis.com/stable/stock/"+symbol+"/quote?token=pk_53f96e249be3442d803886bb59504119";
			HttpGet request = new HttpGet(url);
			// add request header
			org.apache.http.HttpResponse response = null;
			response = client.execute(request);

			BufferedReader rd = new BufferedReader(
			new InputStreamReader(response.getEntity().getContent()));

			StringBuilder result = new StringBuilder();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			stockList.add(result);
			System.out.println("FETCHED: " + symbol);
		}
		return ResponseEntity.ok()
		.body(stockList.toString());
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/all-stocks")
	@ApiOperation("Let the battle begin!!!! Go Go Go!!!")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
	public ResponseEntity<String> fetchAllStocks() throws IOException {
		HttpClient client = HttpClientBuilder.create().build();
		String url = "https://cloud.iexapis.com/stable/tops?token=pk_53f96e249be3442d803886bb59504119";
		HttpGet request = new HttpGet(url);
		// add request header
		org.apache.http.HttpResponse response = null;
		response = client.execute(request);

		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));

		StringBuilder result = new StringBuilder();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		return ResponseEntity.ok()
				.body(result.toString());
	}
	public List<StockDetails> getAllStocks() throws IOException {
		HttpClient client = HttpClientBuilder.create().build();
		String url = "https://cloud.iexapis.com/stable/tops?token=pk_53f96e249be3442d803886bb59504119";
		HttpGet request = new HttpGet(url);
		// add request header
		org.apache.http.HttpResponse response = null;
		response = client.execute(request);

		String json = IOUtils.toString(response.getEntity().getContent());

		List<StockDetails> listStock = new ObjectMapper().readValue(json, new TypeReference<List<StockDetails>>(){});

		return listStock;
	}
}