package com.trustatrader.trustatrader.repo;

import com.trustatrader.trustatrader.common.Item_Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface item_repo extends JpaRepository<Item_Model, Integer> {
}
