package com.quickcart.quickcart.catalog;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CatalogSeeder implements CommandLineRunner {
	
	private final ProductStore store;
	private final CatalogService catalog;
	private final ApplicationContext context;
	
	public CatalogSeeder(ProductStore store, CatalogService catalog, ApplicationContext context) {
		this.store  = store;
		this.catalog = catalog;
		this.context = context;
		
	}
	
	public void run(String... args) {
		  store.save(new Product("S1", "Pen", "Stationery", 10, 100));
	        store.save(new Product("E1", "Headphones", "Electronics", 1500, 12));
	        store.save(new Product("E3", "Mouse", "Electronics", 450, 0));
	        System.out.println(">>> Seeded, in stock: " + catalog.inStock());
	        System.out.println(">>> Same CatalogService instance? " + (context.getBean(CatalogService.class) == catalog));
	}

}
