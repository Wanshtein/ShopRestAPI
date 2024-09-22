package de.surname.spring.SpringShopRestAPI.repository;

import de.surname.spring.SpringShopRestAPI.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Override
    Page<Product> findAll(Pageable pageable);
}
