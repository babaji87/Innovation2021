package com.example.worker;

import java.util.*;
import java.util.stream.Collectors;

import com.example.worker.model.StockMessage;
import com.example.worker.model.StocksData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JdbcMessageHandler {
    private static final String QUEUE_NAME = "que001";

    private final Logger logger = LoggerFactory.getLogger(QueueReceiveController.class);

    List<StocksData>  listStockData = new ArrayList<StocksData>();
     public void handleJdbcMessage(List<Map<String, Object>> message) {
        listStockData.clear();
        try {
            for (Map<String, Object> resultMap : message) {
                StocksData data = new StocksData();
                for (String column : resultMap.keySet()) {
                    Object o = resultMap.get(column);
                    if (column.equals("id")){
                        data.setId(new Long(o.toString()));
                    }
                    else if (column.equals("symbol")){
                        data.setSymbol((String)o);

                    } else if (column.equals("company_name"))
                        data.setCompanyName((String)o);
                    else if (column.equals("primary_exchange"))
                        data.setPrimaryExchange((String)o);
                    else if (column.equals("latest_price"))
                    data.setLatestPrice(new Double(o.toString()));
                    else if (column.equals("latest_time"))
                        data.setLatestTime((String)o);
                }

                 listStockData.add(data);

            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }

    @JmsListener(destination = QUEUE_NAME, containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(StockMessage message) {
        {
            logger.info("Received message from queue: {}", message.getName());
            List<StocksData> allData =listStockData.stream().filter(x->x.getSymbol().equals(message.getName())).collect(Collectors.toList());
            StocksData max = Collections.max(allData);
            System.out.println(max.getSymbol()+"  "+max.getLatestPrice()+"\n");
        }


    }
}
