package com.example.demo.repositories;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.example.demo.entities.Product;

public interface ProductRepository extends CrudRepository<Product,Integer>{
	Optional <Product> findByProductName(String name);
	Optional<Product> findByProductId(int id);
}