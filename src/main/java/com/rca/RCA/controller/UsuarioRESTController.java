package com.rca.RCA.controller;

import com.rca.RCA.service.UsuarioService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.GradoDTO;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.UsuarioDTO;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/usuario")
public class UsuarioRESTController {

    @Autowired
    private UsuarioService usuarioService;

    public UsuarioRESTController(){

    }

    @GetMapping
    public ApiResponse<Pagination<UsuarioDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return this.usuarioService.getList(filter, page, size);
    }

    @GetMapping("{id}")
    public ApiResponse<UsuarioDTO> one(@PathVariable String id) throws ResourceNotFoundException {
        return this.usuarioService.one(id);
    }

    @PutMapping
    public ApiResponse<UsuarioDTO> update(@Valid @RequestBody UsuarioDTO UsuarioDTO) throws ResourceNotFoundException, AttributeException {
        return this.usuarioService.update(UsuarioDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<UsuarioDTO> delete(@PathVariable String id) {
        return this.usuarioService.delete(id);
    }
}
