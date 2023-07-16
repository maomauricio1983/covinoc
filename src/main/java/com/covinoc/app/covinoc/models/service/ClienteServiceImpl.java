package com.covinoc.app.covinoc.models.service;

import com.covinoc.app.covinoc.models.dao.ClienteDao;
import com.covinoc.app.covinoc.models.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {


    @Autowired
    private ClienteDao clienteDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listar() {
        return (List<Cliente>) clienteDao.listarHabilitados();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> porId(Long id) {
        return clienteDao.findById(id);
    }

    @Override
    @Transactional
    public Cliente guardar(Cliente cliente) {
        return clienteDao.save(cliente);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        clienteDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarPorIds(Iterable<Long> ids) {
        return (List<Cliente>) clienteDao.findAllById(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> porEmail(String email) {
        return clienteDao.porEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        return clienteDao.existsByEmail(email);
    }


    public ResponseEntity<Cliente> actualizarCliente(Optional<Cliente> cliente) {
        if (cliente.isPresent()) {
            Cliente clienteDb = cliente.get();
            if (!cliente.get().getEmail().isEmpty() && !cliente.get().getEmail().equalsIgnoreCase(clienteDb.getEmail()) &&
                    porEmail(cliente.get().getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body((Cliente) Collections.singletonMap("mensaje", "Ya existe un usuario con ese correo electrónico."));
            }

            clienteDb.setNombre(cliente.get().getNombre());
            clienteDb.setApellido(cliente.get().getApellido());
            clienteDb.setEmail(cliente.get().getEmail());
            clienteDb.setPassword(cliente.get().getPassword());
            clienteDb.setEstado(cliente.get().getEstado());
            return ResponseEntity.status(HttpStatus.CREATED).body(guardar(clienteDb));
        }
        return null;
    }




}
