package com.quickcart.quickcart.catalog;

public class CatalogDemo {

	public static void main(String[] args) {

		ProductStore store = new InMemoryProductStore(); //manually I have done the dependency injection
		store.save(new Product("S1", "Pen", "Stationary", 10, 100));
		store.save(new Product("E1", "Headphones", "Electronics", 1500, 12));
		store.save(new Product("E3", "Mouse", "Electronics", 450, 0));

		CatalogService catalog = new CatalogService(store);//manually I have done the dependency injection

		System.out.println(catalog.getBySku("E1"));
		catalog.reduceStock("E1", 2);
		System.out.println("Stock after sale: " + catalog.getBySku("E1").stock());
		System.out.println("In stock: " + catalog.inStock());
		
		try {
            catalog.getBySku("X9");
        } catch (ProductNotFoundException e) {
            System.out.println("Caught: " + e.getMessage());
        }
        try {
            catalog.reduceStock("E1", 99);
        } catch (IllegalStateException e) {
            System.out.println("Caught: " + e.getMessage());
        }

	}

}
