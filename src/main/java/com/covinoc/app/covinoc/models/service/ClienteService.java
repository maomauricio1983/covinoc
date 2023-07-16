package com.covinoc.app.covinoc.models.service;

import com.covinoc.app.covinoc.models.entity.Cliente;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    List<Cliente> listar();
    Optional<Cliente> porId(Long id);
   Cliente guardar(Cliente usuario);
    void eliminar(Long id);
    List<Cliente> listarPorIds(Iterable<Long> ids);

    Optional<Cliente> porEmail(String email);
    boolean existePorEmail(String email);
    ResponseEntity<Cliente> actualizarCliente(Optional<Cliente> cliente);



}
