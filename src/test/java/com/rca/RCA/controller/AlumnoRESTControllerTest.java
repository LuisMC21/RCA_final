package com.rca.RCA.controller;

import com.rca.RCA.service.AlumnoService;
import com.rca.RCA.type.*;
import com.rca.RCA.util.exceptions.AttributeException;
import com.rca.RCA.util.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

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

class AlumnoRESTControllerTest {
/*
    @Mock
    private AlumnoService alumnoService;

    @InjectMocks
    private AlumnoRESTController alumnoRESTController;

    private AlumnoDTO alumnoDTO;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        alumnoDTO = new AlumnoDTO();
        alumnoDTO.setCode("ALU001");
        alumnoDTO.setId(UUID.randomUUID().toString());
        alumnoDTO.setStatus("CREATED");
        alumnoDTO.setDiseases("1");
        alumnoDTO.setNamecon_pri("PEDRO");
        alumnoDTO.setTelcon_pri("983716278");
        alumnoDTO.setNamecon_sec("Pablo");
        alumnoDTO.setTelcon_sec("982736178");
        alumnoDTO.setType_insurance("SIS");
        alumnoDTO.setVaccine("SI");
        alumnoDTO.setCreateAt(LocalDateTime.now());

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

        alumnoDTO.setUsuarioDTO(usuarioDTO);
    }

    @DisplayName("Test para listar alumnos")
    @Test
    void list() {
        // Given
        String filter = "";
        int page = 1;
        int size = 10;

        Pagination<AlumnoDTO> pagination = new Pagination<>();
        pagination.setCountFilter(2);
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        List<AlumnoDTO> docenteDTOS = new ArrayList<>();
        docenteDTOS.add(alumnoDTO);

        AlumnoDTO alumnoDTO2 = new AlumnoDTO();
        alumnoDTO2.setCode("ALU002");
        alumnoDTO2.setId(UUID.randomUUID().toString());
        alumnoDTO2.setStatus("CREATED");
        alumnoDTO2.setDiseases("1");
        alumnoDTO2.setNamecon_pri("PEDRO");
        alumnoDTO2.setTelcon_pri("923716268");
        alumnoDTO2.setNamecon_sec("Pablo");
        alumnoDTO2.setTelcon_sec("912736158");
        alumnoDTO2.setType_insurance("SIS");
        alumnoDTO2.setVaccine("SI");
        alumnoDTO2.setCreateAt(LocalDateTime.now());

        UsuarioDTO usuarioDTO2= new UsuarioDTO();
        usuarioDTO2.setName("Pedro");
        usuarioDTO2.setPa_surname("Pedro");
        usuarioDTO2.setMa_surname("Pedro");
        usuarioDTO2.setBirthdate(Date.valueOf("2020-07-05"));
        usuarioDTO2.setNumdoc("78912678");
        usuarioDTO2.setType_doc("DNI");
        usuarioDTO2.setEmail("pedro@gmail.com");
        usuarioDTO2.setPassword("pedro");
        usuarioDTO2.setGra_inst("Bachiller");
        usuarioDTO2.setTel("983253301");
        usuarioDTO2.setRol("STUDENT");

        alumnoDTO2.setUsuarioDTO(usuarioDTO2);

        docenteDTOS.add(alumnoDTO2);

        pagination.setList(docenteDTOS.stream().collect(Collectors.toList()));

        ApiResponse<Pagination<AlumnoDTO>> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(pagination);

        when(alumnoService.getList(filter, page, size)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<Pagination<AlumnoDTO>> actualApiResponse = alumnoRESTController.list(filter, page, size);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertEquals(expectedApiResponse.getData().getCountFilter(), actualApiResponse.getData().getCountFilter());
        assertThat(actualApiResponse.getMessage()).isEqualTo(expectedApiResponse.getMessage());
        assertThat(actualApiResponse.getData()).isEqualTo(expectedApiResponse.getData());

        verify(alumnoService).getList(filter, page, size);
    }

    @DisplayName("Test para obtener un alumno con id")
    @Test
    void one() throws ResourceNotFoundException {
        //given
        ApiResponse<AlumnoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(alumnoDTO);

        when(alumnoService.one(alumnoDTO.getId())).thenReturn(expectedApiResponse);

        // When
        ApiResponse<AlumnoDTO> actualApiResponse = alumnoRESTController.one(alumnoDTO.getId());

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(alumnoService, times(1)).one(alumnoDTO.getId());
    }

    @DisplayName("Test para agregar un alumno")
    @Test
    void add() throws ResourceNotFoundException, AttributeException {
        // given
        ApiResponse<AlumnoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(alumnoDTO);
        when(alumnoService.add(alumnoDTO)).thenReturn(expectedApiResponse);

        //when
        ApiResponse<AlumnoDTO> actualApiResponse = alumnoRESTController.add(alumnoDTO);

        // then
        assertThat(actualApiResponse).isNotNull();
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals("ok", actualApiResponse.getMessage());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getCreateAt()).isEqualTo(expectedApiResponse.getData().getCreateAt());
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        verify(alumnoService).add(this.alumnoDTO);
    }

    @DisplayName("Test para actualizar un alumno")
    @Test
    void update() throws ResourceNotFoundException {
        //given
        AlumnoDTO alumnoDTO2 = new AlumnoDTO();
        alumnoDTO2.setCode("ALU001");
        alumnoDTO2.setId(UUID.randomUUID().toString());
        alumnoDTO2.setStatus("CREATED");
        alumnoDTO2.setDiseases("1");
        alumnoDTO2.setNamecon_pri("PEDRO");
        alumnoDTO2.setTelcon_pri("923006268");
        alumnoDTO2.setNamecon_sec("Pablo");
        alumnoDTO2.setTelcon_sec("912556158");
        alumnoDTO2.setType_insurance("SIS");
        alumnoDTO2.setVaccine("SI");
        alumnoDTO2.setCreateAt(LocalDateTime.now());

        alumnoDTO2.setUsuarioDTO(usuarioDTO);

        ApiResponse<AlumnoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(alumnoDTO2);
        when(alumnoService.one(alumnoDTO2.getId())).thenReturn(expectedApiResponse);
        when(alumnoService.update(alumnoDTO2)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<AlumnoDTO> actualApiResponse = alumnoRESTController.update(alumnoDTO2);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getUpdateAt()).isEqualTo(expectedApiResponse.getData().getUpdateAt());
        verify(alumnoService).update(alumnoDTO2);
    }

    @DisplayName("Test para eliminar un alumno")
    @Test
    void delete() {
        // given
        ApiResponse<AlumnoDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(alumnoDTO);

        when(alumnoService.delete(alumnoDTO.getId())).thenReturn(expectedApiResponse);

        // when
        ApiResponse<AlumnoDTO> actualApiResponse = alumnoRESTController.delete(alumnoDTO.getId());

        // then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(alumnoService, times(1)).delete(alumnoDTO.getId());
    }

    @DisplayName("Test para el reporte de datos personales de un alumno")
    @Test
    void datosPersonales() {
        // given
        byte[] pdfBytes = new byte[100];
        when(alumnoService.datosPersonales(anyString())).thenReturn(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(new ByteArrayResource(pdfBytes))
        );

        // when
        ResponseEntity<Resource> response = alumnoRESTController.datosPersonales(alumnoDTO.getId());
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertEquals(pdfBytes.length, ((ByteArrayResource) response.getBody()).getByteArray().length);

    }*/
}