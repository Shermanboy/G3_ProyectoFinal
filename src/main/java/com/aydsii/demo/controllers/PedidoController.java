package com.aydsii.demo.controllers;

import com.aydsii.demo.dtos.PedidoResponse;
import com.aydsii.demo.dtos.RealizarPedidoRequest;
import com.aydsii.demo.entities.Pedido;
import com.aydsii.demo.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<?> realizarPedido(
            @RequestBody RealizarPedidoRequest request) {

        try {

            if (request == null || request.idCliente() == null) {
                return ResponseEntity.badRequest()
                        .body("El identificador del cliente es obligatorio.");
            }

            Pedido pedido = pedidoService.realizarPedido(
                    request.idCliente(),
                    request.datosEntrega());

            return ResponseEntity.status(201)
                    .body(PedidoResponse.from(pedido));

        } catch (IllegalArgumentException | IllegalStateException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Método que agarra la petición delete para cancelar un pedido
    @DeleteMapping("/{idPedido}/cancelar")
    public ResponseEntity<String> cancelarPedido(@PathVariable Long idPedido) {
        boolean fueCancelado = pedidoService.cancelarPedido(idPedido);

        if (fueCancelado) {
            return ResponseEntity.ok("Pedido cancelado exitosamente y stock devuelto.");
        } else {
            return ResponseEntity.badRequest()
                    .body("Error: El pedido no se encuentra en un estado válido para cancelación o no existe.");
        }
    }

    @GetMapping("/clientes/{idCliente}")
    public ResponseEntity<List<PedidoResponse>> listarPedidosCliente(
            @PathVariable Long idCliente) {

        return ResponseEntity.ok(
                pedidoService.listarPedidosCliente(idCliente));
    }

}