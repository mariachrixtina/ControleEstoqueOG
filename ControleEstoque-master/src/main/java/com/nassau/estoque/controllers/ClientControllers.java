package com.nassau.estoque.controllers;

import com.nassau.estoque.dtos.ClientRecordDto;
import com.nassau.estoque.enums.EnumClient;
import com.nassau.estoque.models.ClientModel;
import com.nassau.estoque.repositories.ClientRepository;
import com.nassau.estoque.services.ClientServices;
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
@RequestMapping("/clients")
public class ClientControllers {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientServices clientServices;
    @PostMapping
    public ResponseEntity<Object> saveClient(@RequestBody @Valid ClientRecordDto clientRecordDto) {
        ClientModel c = new ClientModel();
        BeanUtils.copyProperties(clientRecordDto, c);
        clientRepository.save(c);
        return ResponseEntity.status(HttpStatus.CREATED).body(EnumClient.CREATE_SUCCESSFUL);
    }
    @GetMapping
    public ResponseEntity<List<ClientModel>> findAll() {
        List<ClientModel> listOfClients = clientServices.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(listOfClients);
    }
    @GetMapping("/{idClient}")
    public ResponseEntity<Object> findClientById(@PathVariable UUID idClient) {
        try{
           ClientModel Client = clientServices.findById(idClient);
           return ResponseEntity.status(HttpStatus.OK).body(Client);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @DeleteMapping("/{idClient}")
    public ResponseEntity<Object> deleteClient(@PathVariable UUID idClient) {
        try {
            Optional<ClientModel> s = clientRepository.findById(idClient);
            if(s.isPresent()) {
                clientRepository.deleteById(idClient);
                return ResponseEntity.status(HttpStatus.OK).body(EnumClient.DELETE_SUCCESSFUL);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(EnumClient.NOT_FOUND);
        }
        catch (RuntimeException err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }
    @PutMapping("/{idClient}")
    public ResponseEntity<Object> updateClient(@PathVariable UUID idClient, @RequestBody @Valid ClientModel updateClient) {
        try {
            if(clientRepository.findById(idClient).isPresent()) {
                clientServices.updateDate(idClient, updateClient);
                return ResponseEntity.status(HttpStatus.OK).body(EnumClient.UPDATE_SUCCESSFUL);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(EnumClient.NOT_FOUND);
        }
        catch (RuntimeException err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err.getMessage());
        }
    }
}
