package com.covinoc.app.covinoc.models.dao;

import com.covinoc.app.covinoc.models.entity.Cliente;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClienteDao extends CrudRepository<Cliente,Long> {

    Optional<Cliente> findByEmail(String email);

    @Query("select u from Cliente u where u.email=?1")
    Optional<Cliente> porEmail(String email);


    @Query("select c from Cliente c where c.estado = 'habilitado'")
    List<Cliente> listarHabilitados();






    boolean existsByEmail(String email);
}
