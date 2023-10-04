package com.nassau.estoque.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "supplier_products" )
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierModel implements Serializable {
    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idFornecedor;
    private String cnpj;
    private String name_supplier;
    private String typeProduct;
}