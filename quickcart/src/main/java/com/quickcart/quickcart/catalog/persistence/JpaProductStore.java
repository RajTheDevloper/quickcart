package com.quickcart.quickcart.catalog.persistence;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.quickcart.quickcart.catalog.Product;
import com.quickcart.quickcart.catalog.ProductStore;

@Repository
@Transactional
public class JpaProductStore implements ProductStore {

	private final ProductJpaRepository repository;

	public JpaProductStore(ProductJpaRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public void save(Product product) {
		ProductEntity entity = repository.findBySku(product.sku()).map(existing -> {
			existing.update(product.name(), product.category(), BigDecimal.valueOf(product.price()), product.stock());
			return existing;
		}).orElseGet(() -> new ProductEntity(product.sku(), product.name(), product.category(),
				BigDecimal.valueOf(product.price()), product.stock()));
		repository.save(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Product> findBySku(String sku) {
		return repository.findBySku(sku).map(this::toProduct);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Product> findAll() {
		return repository.findAll().stream().map(this::toProduct).toList();
	}

	@Override
	public void deleteBySku(String sku) {
		repository.deleteBySku(sku);
	}

	private Product toProduct(ProductEntity e) {
		return new Product(e.getSku(), e.getName(), e.getCategory(), e.getPrice().doubleValue(), e.getStock());
	}

}
