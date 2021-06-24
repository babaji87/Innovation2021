package com.example.Hackathon.controller;

import com.example.Hackathon.model.StockDetails;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.util.IOUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class StocksController {
	@Autowired
	@CrossOrigin(origins = "http://localhost:3001")
	@GetMapping("/stocks")
	@ApiOperation("Let the battle begin!!!! Go Go Go!!!")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
	public ResponseEntity<String> fetchStocks() throws IOException {
		List stockList = new ArrayList();
		List<StockDetails> allStocks= getAllStocks();
		int count =10;
		for(StockDetails stock: allStocks){
			count--;
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
			HttpPost httpPost = new HttpPost("http://localhost:8095/api/v1/");
			httpPost.setHeader("Content-type", "application/json");

			while ((line = rd.readLine()) != null) {
				result.append(line);

				try {
					StringEntity stringEntity = new StringEntity(line);
					httpPost.getRequestLine();
					httpPost.setEntity(stringEntity);
					client.execute(httpPost);
				} catch (Exception e) {

				}
			}
			stockList.add(result);
			System.out.println("FETCHED: " + stock.getSymbol()+ " "+ count);
			if (count==0)break;
		}
		return ResponseEntity.ok()
				.body(stockList.toString());
	}
	@CrossOrigin(origins = "http://localhost:3001")
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

	@CrossOrigin(origins = "http://localhost:3001")
	@GetMapping("/all-stocks-data")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
	public ResponseEntity<String> fetchAllStocksData() throws IOException {
		return ResponseEntity.ok()
				.body("");
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
