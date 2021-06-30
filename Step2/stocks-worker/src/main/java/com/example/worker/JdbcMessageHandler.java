package com.example.worker;

import java.util.*;
import java.util.stream.Collectors;

import com.example.worker.model.StocksData;
import org.springframework.stereotype.Component;

@Component
public class JdbcMessageHandler {

    public void handleJdbcMessage(List<Map<String, Object>> message) {
        List<StocksData>  listStockData = new ArrayList<StocksData>();
        Set<String> symbols = new HashSet<String>();
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
                        symbols.add(data.getSymbol());
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
        for(String symbol: symbols)
        {
            List<StocksData> allData =listStockData.stream().filter(x->x.getSymbol().equals(symbol)).collect(Collectors.toList());
            StocksData max = Collections.max(allData);
            System.out.println(max.getSymbol()+"  "+max.getLatestPrice()+"\n");
        }
        System.out.println("End");

    }
}
