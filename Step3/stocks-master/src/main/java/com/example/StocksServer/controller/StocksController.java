package com.example.StocksServer.controller;


import com.example.StocksServer.Repository.StocksDataRepository;
import com.example.StocksServer.model.StockDataDetail;
import com.example.StocksServer.model.StockDetails;
import com.example.StocksServer.model.StocksData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.util.IOUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class StocksController {
	@Autowired
	private StocksDataRepository stocksDataRepository;
	@CrossOrigin(origins = "10.1.207.97:3000")
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

			while ((line = rd.readLine()) != null) {
				result.append(line);
				try {
					StockDataDetail detail = new ObjectMapper().readValue(line, new TypeReference<StockDataDetail>() {
					});
					System.out.println(line);
					stocksDataRepository.save(
							new StocksData(detail.getSymbol(), detail.getCompanyName(), detail.getPrimaryExchange(), detail.getLatestPrice(), detail.getLatestTime()));

				} catch (Exception e) {
					;//do nothing
				}
			}
			stockList.add(result);
			System.out.println("FETCHED: " + stock.getSymbol()+ " "+ count);
			if (count==0)break;
		}
		return ResponseEntity.ok()
				.body(stockList.toString());
	}
	@CrossOrigin(origins = "10.1.207.97:3000")
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

	@CrossOrigin(origins = "10.1.207.97:3000")
	@GetMapping("/all-stocks-data")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
	public ResponseEntity<String> fetchAllStocksData() throws IOException {
		return ResponseEntity.ok()
				.body("");
	}
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/max-stock-value")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
	public ResponseEntity<String> fetchAllStocksData(@RequestParam("symbol") String symbol) throws IOException {
		if(symbol.isEmpty())
			return ResponseEntity.ok("");
		List<StocksData> allData = new ArrayList<>();
		for (StocksData x : stocksDataRepository.findAll()) {
			if (x.getSymbol().equals(symbol)) {
				allData.add(x);
			}
		}
		StocksData max = Collections.max(allData);
		return ResponseEntity.ok()
				.body(max.getLatestPrice().toString());
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
