package com.example.worker;

import com.example.worker.model.StocksData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StocksDataRepository extends JpaRepository<StocksData, Long> {
   
}
