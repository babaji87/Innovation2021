package com.example.Hackathon.Repository;

import com.example.Hackathon.model.StocksData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface StocksDataRepository extends JpaRepository<StocksData, Long>{

}