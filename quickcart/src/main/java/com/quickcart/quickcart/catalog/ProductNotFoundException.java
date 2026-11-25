package com.quickcart.quickcart.catalog;

public class ProductNotFoundException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProductNotFoundException(String sku) {
		super("Product not found: " + sku);
	}
}
