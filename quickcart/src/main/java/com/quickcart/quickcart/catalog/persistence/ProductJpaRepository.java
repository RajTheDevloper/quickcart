package com.quickcart.quickcart.catalog.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long>{
	
	Optional<ProductEntity> findBySku(String sku);
	
	void deleteBySku(String sku);

}
