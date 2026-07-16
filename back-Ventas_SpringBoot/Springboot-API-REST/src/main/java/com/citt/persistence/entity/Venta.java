package com.citt.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idVenta;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccionCompra;

    @NotNull(message = "El valor de compra es obligatorio")
    @Positive(message = "El valor de compra debe ser mayor que cero")
    private Integer valorCompra;

    @NotNull(message = "La fecha de compra es obligatoria")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaCompra;

    @NotNull(message = "El campo despachoGenerado debe ser proporcionado")
    @Builder.Default
    private Boolean despachoGenerado = false;
}
