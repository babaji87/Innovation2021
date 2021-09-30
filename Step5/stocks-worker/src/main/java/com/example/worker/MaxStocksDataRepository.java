package com.example.worker;

import com.example.worker.model.MaxStockData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaxStocksDataRepository extends JpaRepository<MaxStockData, Long> {

}

