package com.sistemas.monolito.controlador;

import com.sistemas.monolito.dominio.Fase;
import com.sistemas.monolito.servicio.FaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/fase")
public class FaseController {
    @Autowired private FaseService faseService;

    @GetMapping({"/id","/index"})
    public String getIndex(Model model){
        model.addAttribute("listaFases", faseService.listarTodos());

        return "fase/faseIndex";
    }

    @GetMapping("/nuevo")
    public String getFaseNuevoForm(Model model){
        model.addAttribute("fase", new Fase());

        return "fase/faseForm";
    }

    @PostMapping("/nuevo")
    public String postFaseNuevoForm(
            @Valid @ModelAttribute("fase") Fase fase,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            // si hay errores muestra formulario para corrección
            return "fase/faseForm";
        }
        faseService.agregar(fase);
        redirectAttributes.addFlashAttribute("flash", "Agregado Correctamente");

        return "redirect:/fase/index";
    }

    @GetMapping("/editar/{id}")
    public String getFaseEditForm(
            @PathVariable("id") Long id,
            Model model){

        Optional<Fase> buscado = faseService.buscar(id);
        if(buscado.isPresent()){
            model.addAttribute("fase", buscado.get());
        }else{
            model.addAttribute("fase", new Fase());
        }
        return "fase/faseForm";
    }

    @PostMapping("/editar")
    public String postFaseEditForm(
            @Valid @ModelAttribute("fase") Fase fase,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            // en caso de error regresa al formulario
            return "fase/faseForm";
        }
        faseService.actualizar(fase);
        redirectAttributes.addFlashAttribute("flash", "Actualizado Correctamente");

        return "redirect:/fase/index";
    }

    @GetMapping("/eliminar/{id}")
    public String getFaseEliminar(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes){

        faseService.eliminar(id);
        redirectAttributes.addFlashAttribute("flash", "Eliminado Correctamente");

        return "redirect:/fase/index";
    }
}
