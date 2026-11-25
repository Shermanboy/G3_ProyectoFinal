package com.aydsii.demo.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record ReclamoResponse(Long id, Long idPedido, String tipo, String motivo, String descripcion,
        LocalDateTime fechaCreacion, String estado, List<EvidenciaResponse> evidencias) {
    public record EvidenciaResponse(Long id, String nombreArchivo, String rutaArchivo) {
    }
}