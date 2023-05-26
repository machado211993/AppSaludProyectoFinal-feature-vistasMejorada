package com.serviciosalud.demo.repositorios;

import com.serviciosalud.demo.entidades.Turno;
import com.serviciosalud.demo.enumeraciones.Estado;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TurnoRepositorio extends JpaRepository<Turno, String> {

    @Query("SELECT t FROM Turno t WHERE t.fecha= :fecha")
    public Optional<List<Turno>> buscarPorFecha(@Param("fecha") String fecha);

    
}
