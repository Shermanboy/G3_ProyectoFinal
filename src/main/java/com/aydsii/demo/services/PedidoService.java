package com.aydsii.demo.services;

import com.aydsii.demo.dtos.DatosEntregaRequest;
import com.aydsii.demo.dtos.PedidoResponse;
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
import java.util.List;
import com.aydsii.demo.dtos.DatosEntregaRequest;

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

    private void validarDatosEntrega(DatosEntregaRequest datos) {

        if (datos == null) {
            throw new IllegalArgumentException(
                    "Los datos de entrega son obligatorios.");
        }

        StringBuilder faltantes = new StringBuilder();

        if (datos.calle() == null || datos.calle().isBlank()) {
            faltantes.append("calle, ");
        }

        if (datos.numero() == null || datos.numero().isBlank()) {
            faltantes.append("número, ");
        }

        if (datos.localidad() == null || datos.localidad().isBlank()) {
            faltantes.append("localidad, ");
        }

        if (datos.provincia() == null || datos.provincia().isBlank()) {
            faltantes.append("provincia, ");
        }

        if (datos.codigoPostal() == null || datos.codigoPostal().isBlank()) {
            faltantes.append("código postal, ");
        }

        if (datos.telefono() == null || datos.telefono().isBlank()) {
            faltantes.append("teléfono, ");
        }

        if (faltantes.length() > 0) {
            String campos = faltantes.substring(0, faltantes.length() - 2);

            throw new IllegalArgumentException(
                    "Faltan completar los siguientes datos de entrega: " + campos + ".");
        }
    }

    private String formatearDatosEntrega(DatosEntregaRequest datos) {

        return "Calle: " + datos.calle()
                + ", Número: " + datos.numero()
                + ", Localidad: " + datos.localidad()
                + ", Provincia: " + datos.provincia()
                + ", Código postal: " + datos.codigoPostal()
                + ", Teléfono: " + datos.telefono();
    }

    @Transactional
    public Pedido realizarPedido(Long idCliente, DatosEntregaRequest datosEntrega) {

        // FA-03: Validar datos de entrega
        validarDatosEntrega(datosEntrega);

        // Buscar carrito del cliente
        Carrito carrito = carritoRepository.findByCliente_IdCliente(idCliente)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró un carrito para el cliente " + idCliente));

        // FA-01: Validar que el carrito tenga productos
        if (carrito.estaVacio()) {
            throw new IllegalStateException(
                    "El carrito está vacío. Debe agregar productos antes de realizar el pedido.");
        }

        // FA-02: Validar disponibilidad de todos los productos
        StringBuilder erroresStock = new StringBuilder();

        for (DetalleCarrito dc : carrito.getDetalles()) {

            Producto producto = dc.getProducto();

            int cantidadSolicitada = dc.getCantidad();
            int cantidadDisponible = producto.consultarDisponibilidad();

            if (cantidadSolicitada > cantidadDisponible) {

                erroresStock.append("Producto: ")
                        .append(producto.getNombre())
                        .append(" (ID: ")
                        .append(producto.getIdProducto())
                        .append("). Cantidad solicitada: ")
                        .append(cantidadSolicitada)
                        .append(". Cantidad disponible: ")
                        .append(cantidadDisponible)
                        .append(".\n");
            }
        }

        // Si hay productos sin disponibilidad suficiente, se rechaza el pedido
        if (erroresStock.length() > 0) {
            throw new IllegalStateException(
                    "Disponibilidad insuficiente para los siguientes productos:\n"
                            + erroresStock);
        }

        // Generar código único de pedido
        String codigo = GeneradorCodigoPedido.getInstance().generarCodigo();

        // Crear cabecera del pedido
        Pedido pedido = new Pedido();
        pedido.setCodigo(codigo);
        pedido.setEstado("Pendiente");
        pedido.setFechaCreacion(new Date());
        pedido.setMontoTotal(carrito.calcularSubtotal().doubleValue());
        pedido.setDatosEntrega(formatearDatosEntrega(datosEntrega));
        pedido.setCliente(carrito.getCliente());

        // Crear detalles y reservar stock
        for (DetalleCarrito dc : carrito.getDetalles()) {

            Producto producto = dc.getProducto();

            DetallePedido detallePedido = new DetallePedido();
            detallePedido.setProducto(producto);
            detallePedido.setCantidad(dc.getCantidad());
            detallePedido.setPrecioUnitario(dc.getPrecioUnitario().doubleValue());
            detallePedido.setSubtotal(dc.getSubtotal().doubleValue());

            pedido.agregarDetalle(detallePedido);

            producto.reservarStock(dc.getCantidad());
            productoRepository.save(producto);
        }

        // Guardar pedido
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // Vaciar carrito después de generar el pedido
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

    @Transactional(readOnly = true)
    public List<PedidoResponse> listarPedidosCliente(Long idCliente) {

        List<Pedido> pedidos = pedidoRepository.findByCliente_IdCliente(idCliente);

        return pedidos.stream()
                .map(PedidoResponse::from)
                .toList();
    }
}