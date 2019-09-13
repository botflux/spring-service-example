package com.ecommerce.microcommerce.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.exception.ProductNotFoundException;
import com.ecommerce.microcommerce.model.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Api
@RestController
public class ProductController {

    @Autowired
    private ProductDao productDao;

    @GetMapping(value = "products")
    public List<Product> showProducts () {
        return productDao.findAll();
    }

    @GetMapping(value = "products/{id}")
    public Product showProduct (@PathVariable int id) throws ProductNotFoundException {
        Product product = productDao.findById(id);

        if (product == null) {
            throw new ProductNotFoundException ("Product with id " + id + " does not exist");
        }

        return product;
    }

    @PostMapping(value = "products")
    public ResponseEntity<Void> addProduct (@Valid @RequestBody Product product) {
        Product createdProduct = productDao.save(product);

        if (product == null) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProduct.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "test/products/{price}")
    public List<Product> showGreaterThan (@PathVariable int price) {
        return productDao.findByPriceGreaterThan(price);
    }
}
