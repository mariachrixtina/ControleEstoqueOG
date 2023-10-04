package com.nassau.estoque.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "client_products" )
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientModel implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idClient;
    private String cpf;
    private String nameClient;

}