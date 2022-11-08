package com.rca.RCA.controller;

import com.rca.RCA.service.ImagenService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.ImagenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("imagen")
public class ImagenRESTController {

    @Autowired
    private ImagenService imagenService;

    public ImagenRESTController(){

    }

    @GetMapping
    public ApiResponse<Pagination<ImagenDTO>> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return this.imagenService.getList(filter, page, size);
    }

    @PostMapping
    public ApiResponse<ImagenDTO> add(@RequestBody @Valid ImagenDTO ImagenDTO) {
        return this.imagenService.add(ImagenDTO);
    }

    @PutMapping
    public ApiResponse<ImagenDTO> update(@RequestBody ImagenDTO ImagenDTO) {
        return this.imagenService.update(ImagenDTO);
    }

    @DeleteMapping("{id}")
    public ApiResponse<ImagenDTO> delete(@PathVariable String id) {
        return this.imagenService.delete(id);
    }
}
