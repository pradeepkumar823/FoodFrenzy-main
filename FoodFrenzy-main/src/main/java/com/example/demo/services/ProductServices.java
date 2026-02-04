package com.example.demo.services;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.example.demo.entities.Product;
import com.example.demo.repositories.ProductRepository;

@Component
@Service
public class ProductServices{
	@Autowired
	private ProductRepository productRepository;

	public void addProduct(Product product){
		this.productRepository.save(product);
	}   

	public List<Product> getAllProducts(){
        return (List<Product>)this.productRepository.findAll();

	}

	public Product getProductByProductId(int productId){
		return this.productRepository.findById(productId)
        .orElse(null);
	}
    public Product getProduct(int productId){
        return getProductByProductId(productId);
    }

    public void updateProduct(Product product, int productId) {
        product.setProductId(productId);
        this.productRepository.save(product);
    }

    public void deleteProduct(int productId) {
        this.productRepository.deleteById(productId);
    }

    public Product getProductByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return this.productRepository.findByProductName(name)
        .orElse(null);
    }
}