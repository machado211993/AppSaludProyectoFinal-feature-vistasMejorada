package com.serviciosalud.demo.repositorios;

import com.serviciosalud.demo.entidades.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Samu Noah
 */
@Repository
public interface PacienteRepositorio extends JpaRepository<Paciente, String> {

  /*@Query("SELECT p FROM Paciente p WHERE p.id= :id")
    public Paciente buscarPacientePorId(@Param("id") String id);

    @Query("SELECT p FROM Paciente p WHERE p.dni= :dni")
    public Paciente buscarPacientePorDni(@Param("dni") Integer dni);

    @Query("SELECT p FROM Paciente p WHERE p.email = :email")
    public Paciente buscarPacientePorEmail(@Param("email") String email);*/

}
