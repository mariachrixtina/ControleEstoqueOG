package com.nassau.estoque.dtos;

import jakarta.validation.constraints.NotNull;

public record SupplierRecordDto(@NotNull String cnpj, @NotNull String name_supplier, @NotNull String typeProduct) {
}
