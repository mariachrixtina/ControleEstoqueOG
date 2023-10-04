package com.nassau.estoque.dtos;

import jakarta.validation.constraints.NotNull;

public record ProductRecordDto(@NotNull String name, @NotNull Float price, @NotNull Integer quantity, @NotNull String supplier) {
}
