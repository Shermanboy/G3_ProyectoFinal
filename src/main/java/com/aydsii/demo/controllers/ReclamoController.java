package com.aydsii.demo.controllers;

import com.aydsii.demo.dtos.ReclamoResponse;
import com.aydsii.demo.services.ReclamoService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/clientes/{idCliente}/pedidos/{idPedido}/reclamos")
public class ReclamoController {
    private final ReclamoService service;

    public ReclamoController(ReclamoService s) {
        service = s;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReclamoResponse> realizarReclamo(@PathVariable Long idCliente, @PathVariable Long idPedido,
            @RequestParam String tipo, @RequestParam String motivo, @RequestParam String descripcion,
            @RequestParam("archivos") List<MultipartFile> archivos) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.registrar(idCliente, idPedido, tipo, motivo, descripcion, archivos));
    }
}