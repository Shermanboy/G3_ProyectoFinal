package com.aydsii.demo.services;

import com.aydsii.demo.entities.Carrito;
import com.aydsii.demo.entities.DetalleCarrito;
import com.aydsii.demo.entities.DetallePedido;
import com.aydsii.demo.entities.Pedido;
import com.aydsii.demo.entities.Producto;
import com.aydsii.demo.repositories.CarritoRepository;
import com.aydsii.demo.repositories.PedidoRepository;
import com.aydsii.demo.repositories.ProductoRepository;
import com.aydsii.demo.utils.GeneradorCodigoPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Transactional
    public Pedido realizarPedido(Long idCliente, String datosEntrega) {
        if (datosEntrega == null || datosEntrega.isBlank()) {
            throw new IllegalArgumentException("Los datos de entrega son obligatorios.");
        }

        Carrito carrito = carritoRepository.findByCliente_IdCliente(idCliente)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un carrito para el cliente " + idCliente));

        if (carrito.estaVacio()) {
            throw new IllegalStateException("El carrito está vacío.");
        }

        // 1er loop: validar stock de todos los productos antes de modificar nada
        for (DetalleCarrito dc : carrito.getDetalles()) {
            if (!dc.getProducto().hayStock(dc.getCantidad())) {
                throw new IllegalStateException("Stock insuficiente para el producto ID: " + dc.getProducto().getIdProducto());
            }
        }

        // Singleton: generar código único de pedido
        String codigo = GeneradorCodigoPedido.getInstance().generarCodigo();

        // Crear cabecera del Pedido
        Pedido pedido = new Pedido();
        pedido.setCodigo(codigo);
        pedido.setEstado("Pendiente");
        pedido.setFechaCreacion(new Date());
        pedido.setMontoTotal(carrito.calcularSubtotal().doubleValue());
        pedido.setDatosEntrega(datosEntrega);
        pedido.setCliente(carrito.getCliente());

        // 2do loop: crear detalles, asociarlos al pedido y reservar stock
        for (DetalleCarrito dc : carrito.getDetalles()) {
            Producto prod = dc.getProducto();

            DetallePedido dp = new DetallePedido();
            dp.setProducto(prod);
            dp.setCantidad(dc.getCantidad());
            dp.setPrecioUnitario(dc.getPrecioUnitario().doubleValue());
            dp.setSubtotal(dc.getSubtotal().doubleValue());

            pedido.agregarDetalle(dp);

            prod.reservarStock(dc.getCantidad());
            productoRepository.save(prod);
        }

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        carrito.vaciar();
        carritoRepository.save(carrito);

        return pedidoGuardado;
    }

    @Transactional
    public boolean cancelarPedido(Long idPedido) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(idPedido);

        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();

            if (pedido.puedeCancelarse()) {
                pedido.cancelar();

                for (DetallePedido detalle : pedido.getDetalles()) {
                    Producto producto = detalle.getProducto();
                    producto.sumarStock(detalle.getCantidad());
                    productoRepository.save(producto);
                }

                pedidoRepository.actualizarEstado(pedido.getIdPedido(), pedido.getEstado());
                return true;
            }
        }
        return false;
    }
}