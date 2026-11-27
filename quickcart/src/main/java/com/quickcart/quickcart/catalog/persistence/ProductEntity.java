package com.quickcart.quickcart.catalog.persistence;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class ProductEntity {

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private long id;
		
		@Column(nullable = false, unique = true, length = 40)
		private String sku;
		
		@Column(nullable = false, length = 200)
		private String name;
		
		@Column(nullable = false, length = 80)
		private String category;
		
		@Column(nullable = false, precision = 12, scale = 2)
		private BigDecimal price;
		
		@Column(nullable = false)
		private int stock;
		
		protected ProductEntity() {
			//required by JPA
		}
		
		public ProductEntity(String sku, String name, String category, BigDecimal price, int stock) {
			this.sku = sku;
			this.name = name;
			this.category = category;
			this.price = price;
			this.stock = stock;
		}
		
		public void update(String name, String category, BigDecimal price, int stock) {
			this.name = name;
			this.category = category;
			this.price = price;
			this.stock = stock;
		}
		
		public long getId() {return id;}
		public String getSku() { return sku;}
		public String getName() { return name;}
		public String getCategory() { return category;}
		public BigDecimal getPrice() { return price;}
		public int getStock() { return stock;}
		
	
}

