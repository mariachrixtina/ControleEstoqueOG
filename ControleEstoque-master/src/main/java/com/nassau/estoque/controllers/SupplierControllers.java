package com.nassau.estoque.controllers;

import com.nassau.estoque.dtos.SupplierRecordDto;
import com.nassau.estoque.enums.EnumSupplier;
import com.nassau.estoque.models.SupplierModel;
import com.nassau.estoque.repositories.SupplierRepository;
import com.nassau.estoque.services.SupplierServices;
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
@RequestMapping("/suppliers")
public class SupplierControllers {

    @Autowired
    SupplierRepository supplierRepository;
    @Autowired
    SupplierServices supplierServices;
    @PostMapping
    public ResponseEntity<Object> saveSupplier(@RequestBody @Valid SupplierRecordDto supplierRecordDto) {
        SupplierModel s = new SupplierModel();
        BeanUtils.copyProperties(supplierRecordDto, s);
        supplierRepository.save(s);
        return ResponseEntity.status(HttpStatus.CREATED).body(EnumSupplier.CREATE_SUCCESSFUL);
    }
    @GetMapping
    public ResponseEntity<List<SupplierModel>> findAll() {
        List<SupplierModel> listOfSuppliers = supplierServices.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(listOfSuppliers);
    }
    @GetMapping("/{idSupplier}")
    public ResponseEntity<Object> findSupplierById(@PathVariable UUID idSupplier) {
        try{
           SupplierModel Supplier = supplierServices.findById(idSupplier);
           return ResponseEntity.status(HttpStatus.OK).body(Supplier);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @DeleteMapping("/{idSupplier}")
    public ResponseEntity<Object> deleteSupplier(@PathVariable UUID idSupplier) {
        try {
            Optional<SupplierModel> s = supplierRepository.findById(idSupplier);
            if(s.isPresent()) {
                supplierRepository.deleteById(idSupplier);
                return ResponseEntity.status(HttpStatus.OK).body(EnumSupplier.DELETE_SUCCESSFUL);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(EnumSupplier.NOT_FOUND);
        }
        catch (RuntimeException err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }
    @PutMapping("/{idSupplier}")
    public ResponseEntity<Object> updateSupplier(@PathVariable UUID idSupplier, @RequestBody @Valid SupplierModel updateSupplier) {
        try {
            if(supplierRepository.findById(idSupplier).isPresent()) {
                supplierServices.updateDate(idSupplier, updateSupplier);
                return ResponseEntity.status(HttpStatus.OK).body(EnumSupplier.UPDATE_SUCCESSFUL);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(EnumSupplier.NOT_FOUND);
        }
        catch (RuntimeException err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }
}
