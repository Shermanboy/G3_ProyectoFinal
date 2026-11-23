package com.aydsii.demo.services;

import com.aydsii.demo.dtos.ReclamoResponse;
import com.aydsii.demo.entities.*;
import com.aydsii.demo.repositories.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class ReclamoService {
    private static final Logger log = LoggerFactory.getLogger(ReclamoService.class);
    private static final long MAX = 5L * 1024 * 1024;
    private static final Set<String> EXT = Set.of("jpg", "jpeg", "png", "pdf");
    private final ClienteRepository clientes;
    private final PedidoRepository pedidos;
    private final ReclamoRepository reclamos;
    private final EvidenciaRepository evidencias;
    private final Path dir;

    public ReclamoService(ClienteRepository c, PedidoRepository p, ReclamoRepository r, EvidenciaRepository e,
            @Value("${app.evidencias.dir:uploads/evidencias}") String dir) {
        clientes = c;
        pedidos = p;
        reclamos = r;
        evidencias = e;
        this.dir = Paths.get(dir).toAbsolutePath().normalize();
    }

    @Transactional
    public ReclamoResponse registrar(Long cid, Long pid, String tipo, String motivo, String descripcion,
            List<MultipartFile> files) {
        Cliente cliente = clientes.findById(cid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el cliente."));
        Pedido pedido = pedidos.findByIdPedidoAndCliente_IdCliente(pid, cid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "El pedido no existe o no pertenece al cliente."));
        validar(tipo, motivo, descripcion, files);
        try {
            Files.createDirectories(dir);
            Reclamo r = reclamos.save(new Reclamo(tipo.trim().toUpperCase(Locale.ROOT), motivo.trim(),
                    descripcion.trim(), cliente, pedido));
            for (MultipartFile f : files) {
                String original = Paths.get(f.getOriginalFilename()).getFileName().toString();
                Path dest = dir.resolve(UUID.randomUUID() + "." + ext(original)).normalize();
                if (!dest.startsWith(dir))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre de archivo no valido.");
                f.transferTo(dest);
                Evidencia ev = new Evidencia(original, dest.toString());
                r.agregarEvidencia(ev);
                evidencias.save(ev);
            }
            log.info("CU-04 registrado: reclamoId={}, clienteId={}, pedidoId={}, tipo={}", r.getId(), cid, pid,
                    r.getTipo());
            return new ReclamoResponse(r.getId(), pid, r.getTipo(), r.getMotivo(), r.getDescripcion(),
                    r.getFechaCreacion(), r.getEstado(),
                    r.getEvidencias().stream().map(x -> new ReclamoResponse.EvidenciaResponse(x.getId(),
                            x.getNombreArchivo(), x.getRutaArchivo())).toList());
        } catch (IOException ex) {
            log.error("Fallo al almacenar evidencia clienteId={} pedidoId={}", cid, pid, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo almacenar la evidencia.");
        }
    }

    private void validar(String t, String m, String d, List<MultipartFile> fs) {
        if (t == null || !(t.equalsIgnoreCase("RECLAMO") || t.equalsIgnoreCase("DEVOLUCION")))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo debe ser RECLAMO o DEVOLUCION.");
        if (m == null || m.isBlank() || m.length() > 120)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Motivo obligatorio, maximo 120 caracteres.");
        if (d == null || d.isBlank() || d.length() > 2000)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Descripcion obligatoria, maximo 2000 caracteres.");
        if (fs == null || fs.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Adjunte al menos una evidencia.");
        for (MultipartFile f : fs) {
            if (f.isEmpty())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se aceptan archivos vacios.");
            if (f.getSize() > MAX)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximo 5 MB por archivo.");
            if (!EXT.contains(ext(f.getOriginalFilename())))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formatos permitidos: JPG, JPEG, PNG o PDF.");
        }
    }

    private String ext(String n) {
        if (n == null || !n.contains("."))
            return "";
        return n.substring(n.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }
}