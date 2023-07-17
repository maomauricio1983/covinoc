package com.covinoc.app.covinoc.controllers;


import com.covinoc.app.covinoc.exceptions.CrearClienteException;
import com.covinoc.app.covinoc.exceptions.DetalleClienteException;
import com.covinoc.app.covinoc.exceptions.EditarClienteException;
import com.covinoc.app.covinoc.exceptions.ListarClientesException;
import com.covinoc.app.covinoc.models.entity.Cliente;
import com.covinoc.app.covinoc.models.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;


    @GetMapping("/listar")
    public Map<String, List<Cliente>> listar() {
        try {
            return Collections.singletonMap("clientes", clienteService.listar());
        } catch (Exception e) {

            throw new ListarClientesException("Error al listar clientes", e);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        try {
            Optional<Cliente> usuarioOptional = clienteService.porId(id);
            if (usuarioOptional.isPresent()) {
                return ResponseEntity.ok(usuarioOptional.get());
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            throw new DetalleClienteException("Error al obtener el detalle del cliente", e);
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Cliente cliente, BindingResult result) {
        try {
            if (result.hasErrors()) {
                return validar(result);
            }

            if (!cliente.getEmail().isEmpty() && clienteService.existePorEmail(cliente.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("mensaje", "Ya existe un usuario con ese correo electrónico."));
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.guardar(cliente));
        } catch (Exception e) {
            throw new CrearClienteException("Error al crear el cliente", e);
        }
    }


    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id) {
        try {
            if (result.hasErrors()) {
                return validar(result);
            }

            Optional<Cliente> o = clienteService.porId(id);
            ResponseEntity<?> mensaje = clienteService.actualizarCliente(Optional.ofNullable(cliente));
            if (mensaje != null) return mensaje;
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            throw new EditarClienteException("Error al editar el cliente", e);
        }
    }

    @PatchMapping("/deshabilitado/{id}/{estado}")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id,@PathVariable String estado) {
        try {

          Optional<Cliente> cliente = clienteService.porId(id);
          Cliente cliente1;
            if (cliente != null) {
                cliente.get().setEstado(estado);
                cliente1 = cliente.get();


                clienteService.guardar(cliente1);
            }

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            throw new EditarClienteException("Error al editar el estado del  cliente", e);
        }
    }


    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }
}
