package com.aydsii.demo.dtos;

public record DatosEntregaRequest(
        String calle,
        String numero,
        String localidad,
        String provincia,
        String codigoPostal,
        String telefono
) {
}