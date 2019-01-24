package com.trustatrader.trustatrader.repo;

import com.trustatrader.trustatrader.common.Combination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CombinationRepo extends JpaRepository<Combination, Integer> {
    Combination findTopByLocationEqualsAndTradeEquals(String location, String trade);
    Combination getTopByStatusEquals(String status);
}
