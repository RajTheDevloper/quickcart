package com.quickcart.quickcart.playground;

//import java.util.Objects;

public class Product {
	private final String sku;
	private final String name;
	private final String category;
	private final double price;
	private final int stock;

	public Product(String sku, String name, String category, double price, int stock) {
		this.sku = sku;
		this.name = name;
		this.category = category;
		this.price = price;
		this.stock = stock;
	}

	public String getSku() {
		return sku;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public double getPrice() {
		return price;
	}

	public int getStock() {
		return stock;
	}

	@Override
	public String toString() {
		return name + " (" + price + ")";
	}
}

//public class Product {
//	
//	@Override
//	public int hashCode() {
//		return Objects.hash(sku);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Product other = (Product) obj;
//		return Objects.equals(sku, other.sku);
//	}
//
//	private final String sku;
//	
//	public Product(String sku) {
//		this.sku = sku;
//	}
//	
//	public String getSku() {
//		return sku;
//	}
//	
//}
