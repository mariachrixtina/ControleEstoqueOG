package com.nassau.estoque.controllers;

import com.nassau.estoque.dtos.ProductRecordDto;
import com.nassau.estoque.enums.EnumProduct;
import com.nassau.estoque.models.ProductModel;
import com.nassau.estoque.models.SupplierModel;
import com.nassau.estoque.repositories.ProductRepository;
import com.nassau.estoque.services.ProductServices;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductControllers {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductServices productServices;

    @PostMapping
    public ResponseEntity<Object> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        ProductModel p = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, p);

        // Converter a string de ID do fornecedor para UUID
        UUID supplierId = UUID.fromString(productRecordDto.supplier());

        // Criar um objeto SupplierModel com o ID do fornecedor (ou carregar o
        // fornecedor do banco de dados)
        SupplierModel supplier = new SupplierModel();
        supplier.setIdFornecedor(supplierId);

        // Atribuir o fornecedor ao produto
        p.setSupplier(supplier);
 
        productRepository.save(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(EnumProduct.CREATE_SUCCESSFULL);
    }

    @GetMapping
    public ResponseEntity<List<ProductModel>> findAll() {
        List<ProductModel> listOfProducts = productServices.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(listOfProducts);
    }

    @GetMapping("/{idProduct}")
    public ResponseEntity<Object> findProductById(@PathVariable UUID idProduct) {
        try {
            ProductModel product = productServices.findById(idProduct);
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{idProduct}")
    public ResponseEntity<Object> deleteProduct(@PathVariable UUID idProduct) {
        try {
            Optional<ProductModel> p = productRepository.findById(idProduct);
            if (p.isPresent()) {
                productRepository.deleteById(idProduct);
                return ResponseEntity.status(HttpStatus.OK).body(EnumProduct.DELETE_SUCCESSFULL);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(EnumProduct.NOT_FOUND);
        } catch (RuntimeException err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }

    @PutMapping("/{idProduct}")
    public ResponseEntity<Object> updateProduct(@PathVariable UUID idProduct,
            @RequestBody @Valid ProductModel updateProduct) {
        try {
            if (productRepository.findById(idProduct).isPresent()) {
                productServices.updateDate(idProduct, updateProduct);
                return ResponseEntity.status(HttpStatus.OK).body(EnumProduct.UPDATE_SUCCESSFULL);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(EnumProduct.NOT_FOUND);
        } catch (RuntimeException err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }
}
