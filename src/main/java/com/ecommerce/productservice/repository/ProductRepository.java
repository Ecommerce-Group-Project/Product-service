package com.ecommerce.productservice.repository;

import org.springframework.stereotype.Repository;
import com.ecommerce.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}
