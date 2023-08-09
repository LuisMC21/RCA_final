package com.rca.RCA.controller;

import com.rca.RCA.service.DocenteService;
import com.rca.RCA.type.*;
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
import static org.mockito.Mockito.*;

class DocenteRESTControllerTest {

    @Mock
    private DocenteService docenteService;

    @InjectMocks
    private DocenteRESTController docenteRESTController;

    private DocenteDTO docenteDTO;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        docenteDTO = new DocenteDTO();
        docenteDTO.setSpecialty("CIENCIAS");
        docenteDTO.setDose('1');
        docenteDTO.setCode("DOC001");
        docenteDTO.setId(UUID.randomUUID().toString());
        docenteDTO.setCreateAt(LocalDateTime.now());

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setName("Pedro");
        usuarioDTO.setPa_surname("Pedro");
        usuarioDTO.setMa_surname("Pedro");
        usuarioDTO.setBirthdate(Date.valueOf("2020-10-10"));
        usuarioDTO.setNumdoc("78987678");
        usuarioDTO.setType_doc("DNI");
        usuarioDTO.setEmail("pedro@gmail.com");
        usuarioDTO.setPassword("pedro");
        usuarioDTO.setGra_inst("Bachiller");
        usuarioDTO.setTel("983719301");
        usuarioDTO.setRol("TEACHER");

        docenteDTO.setUsuarioDTO(usuarioDTO);

    }

    @DisplayName("Test para listar docentes")
    @Test
    void list() {
        // Given
        String filter = "";
        int page = 1;
        int size = 10;

        Pagination<DocenteDTO> pagination = new Pagination<>();
        pagination.setCountFilter(2);
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        List<DocenteDTO> docenteDTOS = new ArrayList<>();
        docenteDTOS.add(docenteDTO);

        DocenteDTO docenteDTO2 = new DocenteDTO();
        docenteDTO2.setSpecialty("CIENCIAS SOCIALES");
        docenteDTO2.setDose('1');
        docenteDTO2.setCode("DOC002");
        docenteDTO2.setId(UUID.randomUUID().toString());
        docenteDTO2.setCreateAt(LocalDateTime.now());

        UsuarioDTO usuarioDTO2= new UsuarioDTO();
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

        docenteDTO2.setUsuarioDTO(usuarioDTO2);

        docenteDTOS.add(docenteDTO2);

        pagination.setList(docenteDTOS.stream().collect(Collectors.toList()));

        ApiResponse<Pagination<DocenteDTO>> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(pagination);

        when(docenteService.getList(filter, page, size)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<Pagination<DocenteDTO>> actualApiResponse = docenteRESTController.list(filter, page, size);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertEquals(expectedApiResponse.getData().getCountFilter(), actualApiResponse.getData().getCountFilter());
        assertThat(actualApiResponse.getMessage()).isEqualTo(expectedApiResponse.getMessage());
        assertThat(actualApiResponse.getData()).isEqualTo(expectedApiResponse.getData());

        verify(docenteService).getList(filter, page, size);
    }

    @DisplayName("Test para obtener un docente con id")
    @Test
    void one() throws ResourceNotFoundException {
        //given
        ApiResponse<DocenteDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(docenteDTO);

        when(docenteService.one(docenteDTO.getId())).thenReturn(expectedApiResponse);

        // When
        ApiResponse<DocenteDTO> actualApiResponse = docenteRESTController.one(docenteDTO.getId());

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(docenteService, times(1)).one(docenteDTO.getId());
    }
    @DisplayName("Test para agregar un docente")
    @Test
    void add() throws ResourceNotFoundException, AttributeException {
        // given
        ApiResponse<DocenteDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(docenteDTO);
        when(docenteService.add(docenteDTO)).thenReturn(expectedApiResponse);

        //when
        ApiResponse<DocenteDTO> actualApiResponse = docenteRESTController.add(docenteDTO);

        // then
        assertThat(actualApiResponse).isNotNull();
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals("ok", actualApiResponse.getMessage());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getCreateAt()).isEqualTo(expectedApiResponse.getData().getCreateAt());
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        verify(docenteService).add(this.docenteDTO);
    }

    @DisplayName("Test para actualizar un docente")
    @Test
    void update() throws ResourceNotFoundException, AttributeException {
        //given
        DocenteDTO docenteDTO2 = new DocenteDTO();
        docenteDTO2.setSpecialty("CIENCIAS");
        docenteDTO2.setDose('2');
        docenteDTO2.setCode("DOC001");
        docenteDTO2.setId(docenteDTO.getId());
        docenteDTO2.setCreateAt(docenteDTO.getCreateAt());

        docenteDTO2.setUsuarioDTO(usuarioDTO);


        ApiResponse<DocenteDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(docenteDTO2);

        when(docenteService.one(docenteDTO.getId())).thenReturn(expectedApiResponse);
        when(docenteService.update(docenteDTO2)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<DocenteDTO> actualApiResponse = docenteRESTController.update(docenteDTO2);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getUpdateAt()).isEqualTo(expectedApiResponse.getData().getUpdateAt());
        verify(docenteService).update(docenteDTO2);
    }

    @DisplayName("Test para eliminar un docente")
    @Test
    void delete() throws ResourceNotFoundException {
        // given
        ApiResponse<DocenteDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(docenteDTO);

        when(docenteService.delete(docenteDTO.getId())).thenReturn(expectedApiResponse);

        // when
        ApiResponse<DocenteDTO> actualApiResponse = docenteRESTController.delete(docenteDTO.getId());

        // then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(docenteService, times(1)).delete(docenteDTO.getId());
    }

}