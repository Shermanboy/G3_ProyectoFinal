package com.aydsii.demo.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "reclamos")
public class Reclamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 20)
    private String tipo;
    @Column(nullable = false, length = 120)
    private String motivo;
    @Column(nullable = false, length = 2000)
    private String descripcion;
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;
    @Column(nullable = false, length = 30)
    private String estado;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
    @OneToMany(mappedBy = "reclamo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evidencia> evidencias = new ArrayList<>();

    protected Reclamo() {
    }

    public Reclamo(String t, String m, String d, Cliente c, Pedido p) {
        tipo = t;
        motivo = m;
        descripcion = d;
        cliente = c;
        pedido = p;
        fechaCreacion = LocalDateTime.now();
        estado = "Pendiente";
    }

    public void agregarEvidencia(Evidencia e) {
        evidencias.add(e);
        e.setReclamo(this);
    }

    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public List<Evidencia> getEvidencias() {
        return evidencias;
    }
}