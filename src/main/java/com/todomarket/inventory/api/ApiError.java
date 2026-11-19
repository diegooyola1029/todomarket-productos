package com.todomarket.inventory.api;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record ApiError(
        @Schema(example = "El registro solicitado no existe.") String message,
        @Schema(example = "2026-11-18T14:30:00") LocalDateTime timestamp) { }
