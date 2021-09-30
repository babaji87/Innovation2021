package com.example.worker;


import com.example.worker.model.MaxStockData;
import com.example.worker.model.StocksData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class QueueReceiveController {

    private static final String QUEUE_NAME = "anjnaq2";

    private final Logger logger = LoggerFactory.getLogger(QueueReceiveController.class);
    @Autowired
    private StocksDataRepository stocksDataRepository;
    @Autowired
    private MaxStocksDataRepository maxStocksDataRepository;
    @JmsListener(destination = QUEUE_NAME, containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(String stock) {
        logger.info("Received message: {}", stock);
        final List<StocksData> stocks = new ArrayList<>();
        for (StocksData x : stocksDataRepository.findAll()) {
            if (x.getSymbol().equals(stock)) {
                stocks.add(x);
            }
        }

        StocksData max = Collections.max(stocks);
       // System.out.println(stock+"  "+max.getLatestPrice()+"\n");
        maxStocksDataRepository.save(new MaxStockData(stock, max.getLatestPrice(), max.getLatestTime()));
    }

}
