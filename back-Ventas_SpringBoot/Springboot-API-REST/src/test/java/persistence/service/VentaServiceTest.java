package persistence.service;

import com.citt.exceptions.VentaNotFoundException;
import com.citt.persistence.entity.Venta;
import com.citt.persistence.repository.VentaRepository;
import com.citt.persistence.services.VentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    private Venta venta;

    @BeforeEach
    void setUp() {
        venta = Venta.builder()
                .idVenta(1L)
                .direccionCompra("Calle Falsa 123")
                .valorCompra(1000)
                .fechaCompra(LocalDate.of(2025, 4, 14))
                .despachoGenerado(false)
                .build();
    }

    @Test
    @DisplayName("Guarda una venta válida")
    void guardarVentaValida() {
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);

        Venta guardada = ventaService.saveVenta(venta);

        verify(ventaRepository, times(1)).save(venta);
        assertNotNull(guardada);
        assertEquals("Calle Falsa 123", guardada.getDireccionCompra());
        assertEquals(Integer.valueOf(1000), guardada.getValorCompra());
    }

    @Test
    @DisplayName("Actualiza solo despachoGenerado sin perder los demás datos")
    void actualizarEstadoDespachoPreservaDatos() throws VentaNotFoundException {
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Venta actualizada = ventaService.updateDespachoGenerado(1L, true);

        assertTrue(actualizada.getDespachoGenerado());
        assertEquals("Calle Falsa 123", actualizada.getDireccionCompra());
        assertEquals(Integer.valueOf(1000), actualizada.getValorCompra());
        assertEquals(LocalDate.of(2025, 4, 14), actualizada.getFechaCompra());
    }

    @Test
    @DisplayName("Al guardar una venta sin estado, se inicializa como no despachada")
    void inicializaEstadoDespacho() {
        venta.setDespachoGenerado(null);
        when(ventaRepository.save(any(Venta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Venta guardada = ventaService.saveVenta(venta);

        assertFalse(guardada.getDespachoGenerado());
    }
}
