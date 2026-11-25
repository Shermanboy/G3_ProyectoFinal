package com.aydsii.demo.dtos;

import com.aydsii.demo.entities.Pedido;
import java.util.Date;

public record PedidoResponse(
        Long idPedido,
        String codigo,
        String estado,
        Date fechaCreacion,
        Double montoTotal,
        String datosEntrega
) {
    public static PedidoResponse from(Pedido p) {
        return new PedidoResponse(
                p.getIdPedido(),
                p.getCodigo(),
                p.getEstado(),
                p.getFechaCreacion(),
                p.getMontoTotal(),
                p.getDatosEntrega()
        );
    }
}