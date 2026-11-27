package com.aydsii.demo.dtos;

public record RealizarPedidoRequest(
        Long idCliente,
        DatosEntregaRequest datosEntrega) {
}