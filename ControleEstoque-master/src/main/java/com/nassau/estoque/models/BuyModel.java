package com.nassau.estoque.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "buy_products" )
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyModel implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idbuy;

    @ManyToOne
    @JoinColumn(name = "IdClient")
    private ClientModel cliente;

    @ManyToOne
    @JoinColumn(name = "IdProduct")
    private ProductModel produto;    
}
