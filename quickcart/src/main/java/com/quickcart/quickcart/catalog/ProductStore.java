package com.quickcart.quickcart.catalog;

import java.util.List;
import java.util.Optional;

public interface ProductStore {
	
	void save(Product product);
	Optional<Product> findBySku(String sku);
	List<Product> findAll();

}
