package com.example.Hackathon.controller;

import com.example.Hackathon.Repository.StocksDataRepository;
import com.example.Hackathon.model.StockDataDetail;
import com.example.Hackathon.model.StockDetails;
import com.example.Hackathon.model.StocksData;
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
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
public class StocksController {
	@Autowired
	private StocksDataRepository stocksDataRepository;
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/stocks")
	@ApiOperation("Let the battle begin!!!! Go Go Go!!!")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
	public ResponseEntity<String> fetchStocks() throws IOException {
		List stockList = new ArrayList();
		List<StockDetails> allStocks= getAllStocks();
		StringBuilder result = new StringBuilder();
		for(StockDetails stock: allStocks){
			HttpClient client = HttpClientBuilder.create().build();
			String url = "https://cloud.iexapis.com/stable/stock/"+stock.getSymbol()+"/quote?token=pk_53f96e249be3442d803886bb59504119";
			HttpGet request = new HttpGet(url);
			// add request header
			org.apache.http.HttpResponse response = client.execute(request);

			BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
				StockDataDetail detail = new ObjectMapper().readValue(line, new TypeReference<StockDataDetail>(){});
				stocksDataRepository.save(new StocksData(detail.getSymbol(), detail.getCompanyName(), detail.getPrimaryExchange(), detail.getLatestPrice(), detail.getLatestTime()));
			}
			stockList.add(result);
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
	@GetMapping("/stocksdata")
	@CrossOrigin(origins = "http://localhost:3000")
	public List<StocksData> getAllData() {
		return stocksDataRepository.findAll();
	}

	@PostMapping("/stocksdata")
	@CrossOrigin(origins = "http://localhost:3000")
	public StocksData createStocksData(@RequestBody StocksData data) {
		return stocksDataRepository.save(data);
	}
}
