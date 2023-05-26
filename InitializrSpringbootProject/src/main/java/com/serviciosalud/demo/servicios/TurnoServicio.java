package com.serviciosalud.demo.servicios;

import com.serviciosalud.demo.MiExcepcion.MiExcepcion;
import org.springframework.beans.factory.annotation.Autowired;
import com.serviciosalud.demo.entidades.Paciente;
import com.serviciosalud.demo.entidades.Profesional;
import com.serviciosalud.demo.entidades.Turno;
import com.serviciosalud.demo.enumeraciones.Estado;
import com.serviciosalud.demo.repositorios.PacienteRepositorio;
import com.serviciosalud.demo.repositorios.ProfesionalRepositorio;
import com.serviciosalud.demo.repositorios.TurnoRepositorio;
import com.serviciosalud.demo.repositorios.UsuarioRepositorio;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TurnoServicio {

    @Autowired
    ProfesionalRepositorio profesionalRepositorio;

    @Autowired
    PacienteRepositorio pacienteRepositorio;

    @Autowired
    TurnoRepositorio turnoRepositorio;

    @Transactional
    public void registrar(String idPaciente, String idProfesional, String mes, String dia, String hora, String motivoConsulta) throws MiExcepcion {

        Turno turno = new Turno();

        Optional<Paciente> respuestaPaciente = pacienteRepositorio.findById(idPaciente);
        Optional<Profesional> respuestaProfesional = profesionalRepositorio.findById(idProfesional);

        System.out.println("turnoServ:paci" + respuestaPaciente);
        System.out.println("turnoServ:prof" + respuestaProfesional);

        if (respuestaPaciente.isPresent() && respuestaProfesional.isPresent()) {

            Paciente paciente = respuestaPaciente.get();
            turno.setPaciente(paciente);

            Profesional profesional = respuestaProfesional.get();
            turno.setProfesional(profesional);

            Date fecha = new Date();
            fecha.setMonth(11);
            fecha.setDate(27);
            fecha.setHours(12);
            System.out.println("fechaTurno" + fecha.toString());

            turno.setFecha(buscarFecha(dia, mes, profesional, hora).toString());
            turno.setHorario(hora);
            turno.setEstado(Estado.RESERVADO);
            turno.setMotivoDeConsulta(motivoConsulta);

            turnoRepositorio.save(turno);
        }
    }

    public LocalDate buscarFecha(String dia, String mes, Profesional profesional, String hora) throws MiExcepcion {
        // Convertir el mes a Month
        Month mesComparar = Month.valueOf(mes.toUpperCase());

        // Convertir el dia a DayOfWeek
        DayOfWeek diaComparar = DayOfWeek.valueOf(dia.toUpperCase());

        // PRIMER FECHA DEL MES
        LocalDate fechaActual = LocalDate.of(LocalDate.now().getYear(), mesComparar, 1);
        // ULTIMA FECHA DEL MES
        LocalDate fechaFin = LocalDate.of(LocalDate.now().getYear(), mesComparar, mesComparar.length(false));

        while (fechaActual.isBefore(fechaFin) || fechaActual.isEqual(fechaFin)) { //recorre todos los dias del mes elegido {ej:junio}

            //optional puede recibir o no una respuesta && List<Turno> porque de una misma fecha pueda haber +deUno pero con distinta hora
            Optional<List<Turno>> respuestaTurno = turnoRepositorio.buscarPorFecha(fechaActual.toString());

            if (respuestaTurno.isPresent()) { //si existe un turno con la misma fecha en la BD...
                List<Turno> turnoEnBD = respuestaTurno.get(); // guardo el turno encontrado en BD

                validarFecha(turnoEnBD, profesional, hora); //valido que no sea con el mismo prof. a la misma hora

                // buscar solo el dia que eligio el paciente {ej: lunes}, del mes elegido {ej: junio} /// todos los lunes de junio
                if (fechaActual.getDayOfWeek() == diaComparar) { // diaActual irá incrementando hasta que sea igual a diaComparar

                    System.out.println("TServ83" + fechaActual.toString());

                    //devuelve el primer dia disponible {siguiendo con el ej: el 1° lunes de junio}
                    return fechaActual;
                }
                fechaActual = fechaActual.plusDays(1); // diaActual irá incrementando de a uno

            } else { //si no existe un turno con la misma fecha en la BD...  *no hay validarFecha()*

                // buscar solo el dia que eligio el paciente {ej: lunes}, del mes elegido {ej: junio} /// todos los lunes de junio
                if (fechaActual.getDayOfWeek() == diaComparar) { // diaActual irá incrementando hasta que sea igual a diaComparar

                    System.out.println("TServ83" + fechaActual.toString());

                    //devuelve el primer dia disponible {siguiendo con el ej: el 1° lunes de junio}
                    return fechaActual;
                }
                fechaActual = fechaActual.plusDays(1); // diaActual irá incrementando de a uno

            }
        }
        return null;
    }

    public void validarFecha(List<Turno> turnoEnBD, Profesional profesional, String hora) throws MiExcepcion {

        for (Turno aux : turnoEnBD) { // recorremos uno por uno
            if (aux.getProfesional().equals(profesional) && aux.getHorario().equals(hora)) {
                throw new MiExcepcion("Esa fecha ya está reservada con el mismo profesional a esa misma hora!!!!");
            }
        }

    }
}
