package com.aydsii.demo.repositories;

import com.aydsii.demo.entities.Pedido;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findByIdPedidoAndCliente_IdCliente(Long idPedido, Long idCliente);

    @Modifying
    @Transactional
    @Query("UPDATE Pedido p SET p.estado=:estado WHERE p.idPedido=:id")
    void actualizarEstado(@Param("id") Long id, @Param("estado") String estado);
}