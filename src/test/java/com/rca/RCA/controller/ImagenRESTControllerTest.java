package com.rca.RCA.controller;

import com.rca.RCA.service.ImagenService;
import com.rca.RCA.type.*;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImagenRESTControllerTest {

    @Mock
    private ImagenService imagenService;

    @InjectMocks
    private ImagenRESTController imagenRESTController;

    private ImagenFileDTO imagenFileDTO;

    private ImagenDTO imagenDTO;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imagenDTO= new ImagenDTO();
        imagenDTO.setName("Logo");
        imagenDTO.setCode("IMG001");
        imagenDTO.setRoute("/images/lkdasmdkas.jpg");
        imagenDTO.setId(UUID.randomUUID().toString());
        imagenDTO.setCreateAt(LocalDateTime.now());

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setCreateAt(LocalDateTime.now());
        usuarioDTO.setId(UUID.randomUUID().toString());

        imagenDTO.setUsuarioDTO(usuarioDTO);
    }

    @DisplayName("Test para listar las imágenes")
    @Test
    void list() {
        // Given
        String filter = "";
        int page = 1;
        int size = 10;

        Pagination<ImagenDTO> pagination = new Pagination<>();
        pagination.setCountFilter(2);
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        List<ImagenDTO> imagenDTOS = new ArrayList<>();
        imagenDTOS.add(imagenDTO);

        ImagenDTO imagenDTO2 = new ImagenDTO();
        imagenDTO2.setName("Logo");
        imagenDTO2.setCode("IMG001");
        imagenDTO2.setId(UUID.randomUUID().toString());
        imagenDTO2.setCreateAt(LocalDateTime.now());
        imagenDTO2.setUsuarioDTO(usuarioDTO);
        imagenDTOS.add(imagenDTO2);
        pagination.setList(imagenDTOS.stream().collect(Collectors.toList()));

        ApiResponse<Pagination<ImagenDTO>> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(pagination);

        when(imagenService.getList(filter, page, size)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<Pagination<ImagenDTO>> actualApiResponse = imagenRESTController.list(filter, page, size);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertEquals(expectedApiResponse.getData().getCountFilter(), actualApiResponse.getData().getCountFilter());
        assertThat(actualApiResponse.getMessage()).isEqualTo(expectedApiResponse.getMessage());
        assertThat(actualApiResponse.getData()).isEqualTo(expectedApiResponse.getData());

        verify(imagenService).getList(filter, page, size);
    }

    @DisplayName("Test para obtener una imagen por id")
    @Test
    void one() throws ResourceNotFoundException {
        //given
        ApiResponse<ImagenDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(imagenDTO);

        when(imagenService.one(imagenDTO.getId())).thenReturn(expectedApiResponse);

        // When
        ApiResponse<ImagenDTO> actualApiResponse = imagenRESTController.one(imagenDTO.getId());

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());

        verify(imagenService, times(1)).one(imagenDTO.getId());
    }

    @DisplayName("Test para agregar una imagen")
    @Test
    void add() throws AttributeException, ResourceNotFoundException {
        // given
        ApiResponse<ImagenDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");

        imagenFileDTO = new ImagenFileDTO();
        imagenFileDTO.setName("Logo");
        imagenFileDTO.setImagenBase64("base64:imagen/liaj´dsajkdasndas78dsad9as8dusajdonask");

        expectedApiResponse.setData(imagenDTO);
        when(imagenService.add(imagenFileDTO)).thenReturn(expectedApiResponse);

        //when
        ApiResponse<ImagenDTO> actualApiResponse = imagenRESTController.add(imagenFileDTO);

        // then
        assertThat(actualApiResponse).isNotNull();
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        assertThat(actualApiResponse.getData().getName()).isEqualTo(expectedApiResponse.getData().getName());
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());

        verify(imagenService).add(this.imagenFileDTO);
    }

    @DisplayName("Test para actualizar una imagen")
    @Test
    void update() throws ResourceNotFoundException, AttributeException {
        //given
        ImagenDTO imagenDTO2 = imagenDTO;
        imagenDTO2.setName("Logo 2");

        ApiResponse<ImagenDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(imagenDTO2);

        when(imagenService.one(imagenDTO2.getId())).thenReturn(expectedApiResponse);
        when(imagenService.update(imagenDTO2)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<ImagenDTO> actualApiResponse = imagenRESTController.update(imagenDTO2);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getUpdateAt()).isEqualTo(expectedApiResponse.getData().getUpdateAt());
        verify(imagenService).update(imagenDTO2);
    }

    @DisplayName("Test para eliminar una imagen")
    @Test
    void delete() throws ResourceNotFoundException {
        // given
        ApiResponse<ImagenDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(imagenDTO);

        when(imagenService.delete(imagenDTO.getId())).thenReturn(expectedApiResponse);

        // when
        ApiResponse<ImagenDTO> actualApiResponse = imagenRESTController.delete(imagenDTO.getId());

        // then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(imagenService, times(1)).delete(imagenDTO.getId());
    }
}