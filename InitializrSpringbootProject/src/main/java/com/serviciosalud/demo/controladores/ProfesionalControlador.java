package com.serviciosalud.demo.controladores;

import com.serviciosalud.demo.MiExcepcion.MiExcepcion;
import com.serviciosalud.demo.entidades.Profesional;
import com.serviciosalud.demo.servicios.ProfesionalServicio;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/profesional")
public class ProfesionalControlador {

    @Autowired
    ProfesionalServicio profesionalServicio;

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {

        modelo.put("profesional", profesionalServicio.getOne(id));

        return "modificar_profesional.html";
    }

    @PostMapping("/modificado/{idProfesional}")
    public String modificado(MultipartFile archivo,@PathVariable String idProfesional, @RequestParam @DateTimeFormat(pattern= "yyyy-MM-dd")Date fecha, @RequestParam String nombre, @RequestParam String apellido,
            @RequestParam(required = false) Integer dni, @RequestParam String email, @RequestParam(required = false) Integer telefono,
            @RequestParam String sexo, @RequestParam String password, @RequestParam String password2, @RequestParam Long matricula,
            @RequestParam String especialidad, @RequestParam Double precio, @RequestParam String inicioDia, @RequestParam String finDia,
            @RequestParam String inicioHora, @RequestParam String finHora, @RequestParam Double calificacion, @RequestParam String localidad, @RequestParam String obraSocial,
            @RequestParam Long telefonoLaboral, @RequestParam String descripcion, @RequestParam String nombreEstablecimiento, ModelMap modelo) {

        
        try {
            profesionalServicio.actualizarProfesional(archivo,fecha , idProfesional, nombre, apellido, dni, email, telefono, sexo, password, 
                    password2, matricula, especialidad, precio, inicioDia, finDia, inicioHora, finHora, calificacion, localidad, obraSocial, telefonoLaboral, descripcion, 
                    nombreEstablecimiento, Boolean.TRUE);
            
            /*return "redirect:../../inicio";*/
            return "index.html";
            
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            
            return "modificar_profesional.html";
        }
    }
    
    @GetMapping("/listar")
    public String listar(ModelMap modelo){
        
        List<Profesional> profesionales = profesionalServicio.listaProfesinales();
        
        modelo.addAttribute("profesionales", profesionales);
        
        return "listar_profesionales.html";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, ModelMap modelo){
        
        modelo.put("profesional", profesionalServicio.getOne(id));
        return "eliminar_profesional.html";
    }
    
    @PostMapping("/eliminado/{id}")
    public String eliminado(@PathVariable String id, String nombre, ModelMap modelo){
        
        profesionalServicio.borrarPorId(id);
        
        return "inicio.html";
    }

}
