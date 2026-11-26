package com.quickcart.quickcart.catalog;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CatalogService {
	
	private final ProductStore store;
	
	public CatalogService(ProductStore store) {
		this.store = store;
	}
	
	public Product getBySku(String sku) {
		return store.findBySku(sku).orElseThrow(() -> new ProductNotFoundException(sku));
	}
	
	public List<Product> inStock(){
		return store.findAll().stream().filter(p -> p.stock() > 0).toList();
	}
	
	public void reduceStock(String sku, int quantity) {
		Product p = getBySku(sku);
		if(quantity > p.stock()) {
			throw new IllegalStateException("Not enough stock for " + sku);
		}
		store.save(new Product(p.sku(), p.name(), p.category(), p.price(), p.stock() - quantity));
	}
	
	
	//search method in CatalogService:
	public List<Product> search(String text){
		String needle = text.toLowerCase();
		return store.findAll().stream().filter(p -> p.name().toLowerCase().contains(needle)).toList();
	}
	
	
	
	

}
