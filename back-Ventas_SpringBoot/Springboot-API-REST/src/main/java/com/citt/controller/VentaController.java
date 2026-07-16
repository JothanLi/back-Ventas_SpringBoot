package com.citt.controller;

import com.citt.dto.ActualizarEstadoDespachoVentaRequest;
import com.citt.exceptions.VentaNotFoundException;
import com.citt.persistence.entity.Venta;
import com.citt.persistence.services.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
@Tag(name = "Venta", description = "Controlador para gestionar ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva venta")
    public ResponseEntity<Venta> crearVenta(@Valid @RequestBody Venta venta) {
        Venta ventaGuardada = ventaService.saveVenta(venta);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{idVenta}")
                .buildAndExpand(ventaGuardada.getIdVenta())
                .toUri();
        return ResponseEntity.created(location).body(ventaGuardada);
    }

    @PutMapping("/{idVenta}")
    @Operation(summary = "Actualizar completamente una venta")
    public ResponseEntity<Venta> actualizarVenta(
            @PathVariable Long idVenta,
            @Valid @RequestBody Venta venta) throws VentaNotFoundException {
        return ResponseEntity.ok(ventaService.updateVenta(idVenta, venta));
    }

    @PatchMapping("/{idVenta}/despacho")
    @Operation(summary = "Actualizar únicamente el estado de despacho de una venta")
    public ResponseEntity<Venta> actualizarEstadoDespacho(
            @PathVariable Long idVenta,
            @Valid @RequestBody ActualizarEstadoDespachoVentaRequest request) throws VentaNotFoundException {
        return ResponseEntity.ok(
                ventaService.updateDespachoGenerado(idVenta, request.despachoGenerado())
        );
    }

    @GetMapping
    @Operation(summary = "Obtener todas las ventas")
    public ResponseEntity<List<Venta>> getVentas() {
        return ResponseEntity.ok(ventaService.findAllVentas());
    }

    @GetMapping("/{idVenta}")
    @Operation(summary = "Obtener una venta por ID")
    public ResponseEntity<Venta> obtenerVenta(@PathVariable Long idVenta) throws VentaNotFoundException {
        return ResponseEntity.ok(ventaService.findById(idVenta));
    }

    @DeleteMapping("/{idVenta}")
    @Operation(summary = "Eliminar una venta")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long idVenta) throws VentaNotFoundException {
        ventaService.deleteVenta(idVenta);
        return ResponseEntity.noContent().build();
    }
}
