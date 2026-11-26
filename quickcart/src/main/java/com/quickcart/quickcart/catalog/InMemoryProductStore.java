package com.quickcart.quickcart.catalog;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class InMemoryProductStore implements ProductStore{
	
	private final Map<String, Product> products = new LinkedHashMap<>();
	
	@PostConstruct
	void init() {
		System.out.println(">> InMemoryProductStore created by SPring");
	}

	@Override
	public void save(Product product) {
		products.put(product.sku(), product);
	}

	@Override
	public Optional<Product> findBySku(String sku) {
		return Optional.ofNullable(products.get(sku));
	}

	@Override
	public List<Product> findAll() {
		return List.copyOf(products.values());
	}
	
	

}
