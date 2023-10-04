package com.nassau.estoque.dtos;

import jakarta.validation.constraints.NotNull;

public record BuyRecordDto(@NotNull String cliente, @NotNull String produto) {
}
