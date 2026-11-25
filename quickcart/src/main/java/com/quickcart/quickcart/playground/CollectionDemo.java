package com.quickcart.quickcart.playground;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CollectionDemo {

	public static void main(String[] args) {
		Map<String, Integer> stock = new HashMap<>();
		stock.put("pen", 10);
		stock.put("book", 5);
		stock.put("pen", 20);

		System.out.println(stock.get("pen"));
		System.out.println(stock.size());
		System.out.println(stock.getOrDefault("bag", 0));
		for (Map.Entry<String, Integer> e : stock.entrySet()) {
			System.out.println(e.getKey() + " => " + e.getValue());
		}

//		Set<Product> products = new HashSet<>();
//		products.add(new Product("SKU-1"));
//		products.add(new Product("SKU-1"));
//		System.out.println("Product in set: " + products.size()); 

		List<Product> products = List.of(new Product("S1", "Pen", "Stationery", 10, 100),
										new Product("S2", "Notebook", "Stationery", 60, 0),
										new Product("E1", "Headphones", "Electronics", 1500, 12),
										new Product("E2", "Keyboard", "Electronics", 800, 5),
										new Product("E3", "Mouse", "Electronics", 450, 0));
		
		 // 1. names of products costing more than 100, sorted
		List<String> expensive = products.stream().filter(p -> p.getPrice() > 100).map(Product::getName).sorted()
				.toList();
		System.out.println(expensive);
        // 2. total price of everything in stock (price * stock)
		double total = products.stream().mapToDouble(p -> p.getPrice()  * p.getStock()).sum();
		System.out.println(total);
        // 3. out-of-stock products
		List<Product> outOfStock = products.stream().filter(p -> p.getStock() == 0).toList();
		System.out.println(outOfStock);
        // 4. products grouped by category
		Map<String, List<Product>> byCategory = products.stream().collect(Collectors.groupingBy(Product::getCategory));
		System.out.println(byCategory);
		byCategory.forEach((Category, items) -> System.out.println(Category + ": " + items.size() + " products"));
        // 5. find "E2" safely, print its name, or "Not found" for "X9"
		Optional<Product> found = products.stream().filter(p -> p.getSku().equals("E14")).findFirst();
		System.out.println(found.map(Product::getName).orElse("Not found"));
		// print the names of all the Electronics products that are in stock
		List<String> ElectronicProd = products.stream().filter(p -> p.getCategory().equals("Electronics")).filter(p -> p.getStock() > 0).map(Product::getName).toList();
		System.out.println(ElectronicProd);
		//count products cheaper than 500
		Long cheap = products.stream().filter(p -> p.getPrice() < 500).count();
		System.out.println("V2. " + cheap);
		//use of peek and the toList importance at the end  
		products.stream().peek(p -> System.out.println("lookingg at " + p.getName())).filter(p -> p.getPrice() > 100); //no output add toList
		//Print the names of all Stationery products.
		products.stream().filter(p -> p.getCategory().equals("Stationery")).map(Product::getName).toList();  // [Pen, Notebook]
		//Print true or false for "is any product out of stock?" Hint: the ending step anyMatch(...) takes a lambda and returns a boolean, so it needs no filter before it.
		products.stream().anyMatch(p -> p.getStock() == 0);                                                  // true
		
		
		
		


	}

}
