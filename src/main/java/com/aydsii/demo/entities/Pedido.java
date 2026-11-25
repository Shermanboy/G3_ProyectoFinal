package com.aydsii.demo.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;
    private String estado;
    private Date fechaCreacion;
    private Double montoTotal;
<<<<<<< HEAD

    
=======
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
>>>>>>> 5e60d16083854d2f2acdd3db79e0bba5734eb44e
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedido> detalles;

    public Pedido() {
    }

    public boolean puedeCancelarse() {
        return estado != null && (estado.equalsIgnoreCase("Pendiente") || estado.equalsIgnoreCase("Aceptado"));
    }

    public void cancelar() {
        if (puedeCancelarse())
            estado = "Cancelado";
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}