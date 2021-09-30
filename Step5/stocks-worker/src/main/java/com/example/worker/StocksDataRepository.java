package com.example.worker;

import com.example.worker.model.StocksData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StocksDataRepository extends JpaRepository<StocksData, Long> {
}