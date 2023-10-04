package com.nassau.estoque.controllers;

import com.nassau.estoque.dtos.BuyRecordDto;
import com.nassau.estoque.enums.EnumBuy;
import com.nassau.estoque.models.BuyModel;
import com.nassau.estoque.models.ClientModel;
import com.nassau.estoque.models.ProductModel;
import com.nassau.estoque.repositories.BuyRepository;
import com.nassau.estoque.services.BuyServices;
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
@RequestMapping("/buys")
public class BuyControllers {

    @Autowired
    BuyRepository buyRepository;
    @Autowired
    BuyServices buyServices;

    @PostMapping
    public ResponseEntity<Object> saveBuy(@RequestBody @Valid BuyRecordDto BuyRecordDto) {
        BuyModel b = new BuyModel();
        BeanUtils.copyProperties(BuyRecordDto, b);

        // Converter a string de ID do fornecedor para UUID
        UUID clienteId = UUID.fromString(BuyRecordDto.cliente());
        UUID produtoId = UUID.fromString(BuyRecordDto.produto());
        // Criar um objeto SupplierModel com o ID do fornecedor (ou carregar o
        // fornecedor do banco de dados)
        ClientModel cliente = new ClientModel();
        ProductModel produto = new ProductModel();

        cliente.setIdClient(clienteId);
        produto.setIdProduct(produtoId);
        // Atribuir o fornecedor ao produto
        b.setCliente(cliente);
        b.setProduto(produto);
        buyRepository.save(b);

        return ResponseEntity.status(HttpStatus.CREATED).body(EnumBuy.CREATE_SUCCESSFUL);
    }

    @GetMapping
    public ResponseEntity<List<BuyModel>> findAll() {
        List<BuyModel> listOfBuys = buyServices.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(listOfBuys);
    }

    @GetMapping("/{idBuy}")
    public ResponseEntity<Object> findBuyById(@PathVariable UUID idBuy) {
        try {
            BuyModel Buy = buyServices.findById(idBuy);
            return ResponseEntity.status(HttpStatus.OK).body(Buy);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{idBuy}")
    public ResponseEntity<Object> deleteBuy(@PathVariable UUID idBuy) {
        try {
            Optional<BuyModel> p = buyRepository.findById(idBuy);
            if (p.isPresent()) {
                buyRepository.deleteById(idBuy);
                return ResponseEntity.status(HttpStatus.OK).body(EnumBuy.DELETE_SUCCESSFUL);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(EnumBuy.NOT_FOUND);
        } catch (RuntimeException err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }

    @PutMapping("/{idBuy}")
    public ResponseEntity<Object> updateBuy(@PathVariable UUID idBuy,
            @RequestBody @Valid BuyModel updateBuy) {
        try {
            if (buyRepository.findById(idBuy).isPresent()) {
                buyServices.updateDate(idBuy, updateBuy);
                return ResponseEntity.status(HttpStatus.OK).body(EnumBuy.UPDATE_SUCCESSFUL);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(EnumBuy.NOT_FOUND);
        } catch (RuntimeException err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }
}
