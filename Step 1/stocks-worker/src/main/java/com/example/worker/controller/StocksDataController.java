package com.example.worker.controller;

import com.example.worker.Repository.StocksDataRepository;
import com.example.worker.model.StockDataDetail;
import com.example.worker.model.StocksData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1")
public class StocksDataController {
	@Autowired
	private StocksDataRepository stocksDataRepository;

	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("/max-stock-value")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
	public ResponseEntity<String> fetchAllStocksData(@RequestParam("symbol") String symbol) throws IOException {
		if(symbol.isEmpty())
			return ResponseEntity.ok("");
		List<StocksData> allData =stocksDataRepository.findAll().stream().filter(x->x.getSymbol().equals(symbol)).collect(Collectors.toList());
		StocksData max = Collections.max(allData);
		return ResponseEntity.ok()
				.body(max.getLatestPrice().toString());
	}
	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping("/")
	@ApiOperation("Creates a stockdata.")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "OK", response = StocksData.class)})
	public StocksData creatStock(@RequestBody String stocksData) throws JsonProcessingException {
		StockDataDetail detail = new ObjectMapper().readValue(stocksData, new TypeReference<StockDataDetail>() {
		});
		StocksData save = stocksDataRepository.save(
				new StocksData(detail.getSymbol(), detail.getCompanyName(), detail.getPrimaryExchange(), detail.getLatestPrice(), detail.getLatestTime()));
		System.out.println("createStock 3: "+save);
		return save;


	}
}
