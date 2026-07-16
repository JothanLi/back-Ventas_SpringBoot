package com.citt.persistence.services;

import com.citt.exceptions.VentaNotFoundException;
import com.citt.persistence.entity.Venta;
import com.citt.persistence.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;

    public VentaServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> findAllVentas() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta saveVenta(Venta venta) {
        venta.setIdVenta(null);
        if (venta.getDespachoGenerado() == null) {
            venta.setDespachoGenerado(false);
        }
        return ventaRepository.save(venta);
    }

    @Override
    public Venta updateVenta(Long idVenta, Venta venta) throws VentaNotFoundException {
        Venta ventaExistente = findById(idVenta);
        ventaExistente.setDireccionCompra(venta.getDireccionCompra());
        ventaExistente.setValorCompra(venta.getValorCompra());
        ventaExistente.setFechaCompra(venta.getFechaCompra());
        ventaExistente.setDespachoGenerado(venta.getDespachoGenerado());
        return ventaRepository.save(ventaExistente);
    }

    @Override
    public Venta updateDespachoGenerado(Long idVenta, Boolean despachoGenerado) throws VentaNotFoundException {
        Venta ventaExistente = findById(idVenta);
        ventaExistente.setDespachoGenerado(despachoGenerado);
        return ventaRepository.save(ventaExistente);
    }

    @Override
    public void deleteVenta(Long idVenta) throws VentaNotFoundException {
        Venta venta = findById(idVenta);
        ventaRepository.delete(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public Venta findById(Long idVenta) throws VentaNotFoundException {
        return ventaRepository.findById(idVenta)
                .orElseThrow(() -> new VentaNotFoundException("Venta no encontrada con el ID: " + idVenta));
    }
}
