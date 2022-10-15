package com.rca.RCA.controller;

import com.rca.RCA.service.ImagenService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.ImagenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("imagen")
public class ImagenRESTController {

    @Autowired
    private ImagenService imagenService;

    public ImagenRESTController(){

    }

    @GetMapping
    public Pagination<ImagenDTO> list(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return this.imagenService.getList(filter, page, size);
    }

    @PostMapping
    public ApiResponse<ImagenDTO> add(@RequestBody ImagenDTO ImagenDTO) {
        return this.imagenService.add(ImagenDTO);
    }

    @PutMapping
    public void update(@RequestBody ImagenDTO ImagenDTO) {
        this.imagenService.update(ImagenDTO);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        this.imagenService.delete(id);
    }
}
