package com.trustatrader.trustatrader.service;

import com.trustatrader.trustatrader.common.Item_Model;
import com.trustatrader.trustatrader.repo.item_repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Item_Service {

    @Autowired
    private item_repo itemRepo;

    public void saveItem(Item_Model itemModel) {
        itemRepo.save(itemModel);
    }
}
