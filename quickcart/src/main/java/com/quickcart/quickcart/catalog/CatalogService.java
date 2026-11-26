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
	
	//REST API's example
	public List<Product> findAll() {
	    return store.findAll();
	}

	public List<Product> byCategory(String category) {
	    return store.findAll().stream()
	            .filter(p -> p.category().equalsIgnoreCase(category))
	            .toList();
	}

	public Product create(Product product) {
	    if (store.findBySku(product.sku()).isPresent()) {
	        throw new IllegalStateException("SKU already exists: " + product.sku());
	    }
	    store.save(product);
	    return product;
	}

	public Product update(String sku, Product product) {
	    getBySku(sku);   // throws ProductNotFoundException if missing
	    Product updated = new Product(sku, product.name(), product.category(), product.price(), product.stock());
	    store.save(updated);
	    return updated;
	}

	public void delete(String sku) {
	    getBySku(sku);
	    store.deleteBySku(sku);
	}
	
	
	
	

}
