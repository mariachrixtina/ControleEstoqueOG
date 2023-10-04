package com.nassau.estoque.dtos;

import jakarta.validation.constraints.NotNull;

public record ClientRecordDto(@NotNull String cpf, @NotNull String nameClient) {
}
