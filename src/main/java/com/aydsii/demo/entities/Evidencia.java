package com.aydsii.demo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "evidencias")
public class Evidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 255)
    private String nombreArchivo;
    @Column(nullable = false, length = 500)
    private String rutaArchivo;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "reclamo_id", nullable = false)
    private Reclamo reclamo;

    protected Evidencia() {
    }

    public Evidencia(String n, String r) {
        nombreArchivo = n;
        rutaArchivo = r;
    }

    public void setReclamo(Reclamo r) {
        reclamo = r;
    }

    public Long getId() {
        return id;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }
}