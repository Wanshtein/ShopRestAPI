package de.surname.spring.SpringShopRestAPI.services;

import de.surname.spring.SpringShopRestAPI.models.Product;
import de.surname.spring.SpringShopRestAPI.repository.ProductRepository;
import de.surname.spring.SpringShopRestAPI.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(int id) {
        productRepository.deleteById(id);
    }
}
