package com.citt.dto;

import jakarta.validation.constraints.NotNull;

public record ActualizarEstadoDespachoVentaRequest(
        @NotNull(message = "despachoGenerado es obligatorio")
        Boolean despachoGenerado
) {
}
