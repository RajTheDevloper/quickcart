package com.quickcart.quickcart.catalog;

public record Product(String sku, String name, String category, double price, int stock) {
	public Product {
		if(price < 0) throw new IllegalArgumentException("Price cannot be negative");
	}
}