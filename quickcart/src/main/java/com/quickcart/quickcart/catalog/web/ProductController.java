package com.quickcart.quickcart.catalog.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quickcart.quickcart.catalog.CatalogService;
import com.quickcart.quickcart.catalog.Product;

@RestController
@RequestMapping("api/products")
public class ProductController {
	
	private final CatalogService catalog;
	
	public ProductController(CatalogService catalog) {
		this.catalog = catalog;
	}
	
	@GetMapping
	public List<Product> list(@RequestParam(required = false) String category){
		
		return category == null ? catalog.findAll() : catalog.byCategory(category);
		
	}
	
	@GetMapping("/{sku}")
	public Product get(@PathVariable String sku) {
		return catalog.getBySku(sku);
	}
	
	@PostMapping("/{sku}")
	public ResponseEntity<Product> create(@RequestBody Product peoduct){
		Product saved = catalog.create(peoduct);
		return ResponseEntity.created(URI.create("/api/products/" + saved.sku())).body(saved);
	}
	
	@PutMapping("/{sku}")
	public Product update(@PathVariable String sku, @RequestBody Product product) {
		
		return catalog.update(sku, product);
	}
	
	@DeleteMapping("/{sku}")
	public ResponseEntity<Void> delete(@PathVariable String sku){
		catalog.delete(sku);
		return ResponseEntity.noContent().build();
	}

}
