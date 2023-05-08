package com.rca.RCA.controller;

import com.rca.RCA.service.UsuarioService;
import com.rca.RCA.type.ApiResponse;
import com.rca.RCA.type.Pagination;
import com.rca.RCA.type.UsuarioDTO;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
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

class UsuarioRESTControllerTest {
    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioRESTController usuarioRESTController;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setCreateAt(LocalDateTime.now());
        usuarioDTO.setId(UUID.randomUUID().toString());
        usuarioDTO.setStatus("CREATED");
        usuarioDTO.setName("user");
        usuarioDTO.setPa_surname("user");
        usuarioDTO.setMa_surname("user");
        usuarioDTO.setBirthdate(Date.valueOf("2020-10-10"));
        usuarioDTO.setNumdoc("71987578");
        usuarioDTO.setType_doc("DNI");
        usuarioDTO.setEmail("user@gmail.com");
        usuarioDTO.setPassword("user");
        usuarioDTO.setGra_inst("Bachiller");
        usuarioDTO.setTel("983719301");
        usuarioDTO.setRol("TEACHER");
    }

    @DisplayName("Test para listar usuarios")
    @Test
    void list() {
        // Given
        String filter = "";
        int page = 1;
        int size = 10;

        Pagination<UsuarioDTO> pagination = new Pagination<>();
        pagination.setCountFilter(2);
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        List<UsuarioDTO> usuarioDTOS = new ArrayList<>();
        usuarioDTOS.add(usuarioDTO);

        UsuarioDTO usuarioDTO2 = new UsuarioDTO();
        usuarioDTO2.setCreateAt(LocalDateTime.now());
        usuarioDTO2.setId(UUID.randomUUID().toString());
        usuarioDTO2.setStatus("CREATED");
        usuarioDTO2.setName("Pablo");
        usuarioDTO2.setPa_surname("Pablo");
        usuarioDTO2.setMa_surname("Pablo");
        usuarioDTO2.setBirthdate(Date.valueOf("2020-11-10"));
        usuarioDTO2.setNumdoc("78986678");
        usuarioDTO2.setType_doc("DNI");
        usuarioDTO2.setEmail("pablo@gmail.com");
        usuarioDTO2.setPassword("pablo");
        usuarioDTO2.setGra_inst("Bachiller");
        usuarioDTO2.setTel("983713301");
        usuarioDTO2.setRol("TEACHER");

        usuarioDTOS.add(usuarioDTO2);

        pagination.setList(usuarioDTOS.stream().collect(Collectors.toList()));

        ApiResponse<Pagination<UsuarioDTO>> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(pagination);

        when(usuarioService.getList(filter, page, size)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<Pagination<UsuarioDTO>> actualApiResponse = usuarioRESTController.list(filter, page, size);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertEquals(expectedApiResponse.getData().getCountFilter(), actualApiResponse.getData().getCountFilter());
        assertThat(actualApiResponse.getMessage()).isEqualTo(expectedApiResponse.getMessage());
        assertThat(actualApiResponse.getData()).isEqualTo(expectedApiResponse.getData());

        verify(usuarioService).getList(filter, page, size);
    }

    @DisplayName("Test para obtener un usuario con id")
    @Test
    void one() throws ResourceNotFoundException {
        //given
        ApiResponse<UsuarioDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(usuarioDTO);

        when(usuarioService.one(usuarioDTO.getId())).thenReturn(expectedApiResponse);

        // When
        ApiResponse<UsuarioDTO> actualApiResponse = usuarioRESTController.one(usuarioDTO.getId());

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(usuarioService, times(1)).one(usuarioDTO.getId());
    }

    @DisplayName("Test para actualizar un usuario")
    @Test
    void update() throws ResourceNotFoundException, AttributeException {
        //given
        UsuarioDTO usuarioDTO2 = usuarioDTO;
        usuarioDTO2.setName("Nombre actualizado");


        ApiResponse<UsuarioDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(usuarioDTO2);

        when(usuarioService.one(usuarioDTO2.getId())).thenReturn(expectedApiResponse);
        when(usuarioService.update(usuarioDTO2)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<UsuarioDTO> actualApiResponse = usuarioRESTController.update(usuarioDTO2);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getUpdateAt()).isEqualTo(expectedApiResponse.getData().getUpdateAt());
        verify(usuarioService).update(usuarioDTO2);
    }

    @DisplayName("Test para eliminar un usuario")
    @Test
    void delete() throws ResourceNotFoundException {
        // given
        ApiResponse<UsuarioDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(usuarioDTO);

        when(usuarioService.delete(usuarioDTO.getId())).thenReturn(expectedApiResponse);

        // when
        ApiResponse<UsuarioDTO> actualApiResponse = usuarioRESTController.delete(usuarioDTO.getId());

        // then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(usuarioService, times(1)).delete(usuarioDTO.getId());
    }
}