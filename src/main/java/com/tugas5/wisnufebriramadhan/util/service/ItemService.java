package com.tugas5.wisnufebriramadhan.util.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.tugas5.wisnufebriramadhan.model.ItemModel;
import com.tugas5.wisnufebriramadhan.repository.ItemRepository;

import com.tugas5.wisnufebriramadhan.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    public Optional<ItemModel> getItemById(final String id){
        return itemRepository.findById(id);
    }

	public List<ItemModel> getAllItem(final Integer pageNo, final String sortKey) {
        final int noOfRecord = 5;
        final Pageable page = PageRequest.of(pageNo, noOfRecord, Sort.by(sortKey));
        final Page<ItemModel> pageResult = itemRepository.findAll(page);
        return pageResult.getContent();
    }
    
    public void insertItem(final ItemModel item){
        System.out.println();
        itemRepository.save(item);
    }

    public Map<String, Object> updateItem(ItemModel item) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Query query = new Query(new Criteria("id").is(item.getId()));
            Map<String, Object> objectMap = Utility.objectToMap(item);
            Update updateQuery = new Update();
            objectMap.forEach((key, value) -> {
                if (value != null) {
                    updateQuery.set(key, value);
                }
            });
            mongoTemplate.findAndModify(query, updateQuery, ItemModel.class);
            resultMap.put("success", true);
            resultMap.put("message", "user updated");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("success", false);
            resultMap.put("message", "user update failed");
        }
        return resultMap;
    }
}