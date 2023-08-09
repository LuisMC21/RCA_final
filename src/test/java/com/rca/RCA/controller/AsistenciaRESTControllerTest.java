package com.rca.RCA.controller;

import com.rca.RCA.service.AsistenciaService;
import com.rca.RCA.type.*;
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

class AsistenciaRESTControllerTest {

    @Mock
    private AsistenciaService asistenciaService;

    @InjectMocks
    private AsistenciaRESTController asistenciaRESTController;

    private AsistenciaDTO asistenciaDTO;

    private AlumnoDTO alumnoDTO;

    private ClaseDTO claseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        asistenciaDTO= new AsistenciaDTO();
        asistenciaDTO.setState("Presente");
        asistenciaDTO.setCode("ASS001");
        asistenciaDTO.setStatus("CREATED");
        asistenciaDTO.setId(UUID.randomUUID().toString());
        asistenciaDTO.setCreateAt(LocalDateTime.now());

        alumnoDTO= new AlumnoDTO();
        alumnoDTO.setCode("ALU001");
        alumnoDTO.setVaccine("2");
        alumnoDTO.setId(UUID.randomUUID().toString());

        claseDTO= new ClaseDTO();
        claseDTO.setCreateAt(LocalDateTime.now());
        claseDTO.setId(UUID.randomUUID().toString());
        claseDTO.setStatus("CREATED");
        claseDTO.setCode("AUL001");
        claseDTO.setDate(Date.valueOf("2020-10-09"));

        asistenciaDTO.setClaseDTO(claseDTO);
        asistenciaDTO.setAlumnoDTO(alumnoDTO);
    }

    @DisplayName("Test para listar las asistencias")
    @Test
    void list() {
        // Given
        String filter = "";
        int page = 1;
        int size = 10;

        Pagination<AsistenciaDTO> pagination = new Pagination<>();
        pagination.setCountFilter(2);
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        List<AsistenciaDTO> asistenciaDTOS = new ArrayList<>();
        asistenciaDTOS.add(asistenciaDTO);

        AsistenciaDTO asistenciaDTO2 = asistenciaDTO;
        asistenciaDTO2.setState("Falta");
        asistenciaDTO2.setUpdateAt(LocalDateTime.now());
        asistenciaDTO2.setAlumnoDTO(alumnoDTO);

        ClaseDTO claseDTO2= new ClaseDTO();
        claseDTO2.setCreateAt(LocalDateTime.now());
        claseDTO2.setId(UUID.randomUUID().toString());
        claseDTO2.setStatus("CREATED");
        claseDTO2.setCode("AUL001");
        claseDTO2.setDate(Date.valueOf("2020-10-09"));

        asistenciaDTOS.add(asistenciaDTO2);

        pagination.setList(asistenciaDTOS.stream().collect(Collectors.toList()));

        ApiResponse<Pagination<AsistenciaDTO>> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(pagination);

        when(asistenciaService.getList(filter, page, size)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<Pagination<AsistenciaDTO>> actualApiResponse = asistenciaRESTController.list(filter, page, size);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertEquals(expectedApiResponse.getData().getCountFilter(), actualApiResponse.getData().getCountFilter());
        assertThat(actualApiResponse.getMessage()).isEqualTo(expectedApiResponse.getMessage());
        assertThat(actualApiResponse.getData()).isEqualTo(expectedApiResponse.getData());

        verify(asistenciaService).getList(filter, page, size);
    }
    @DisplayName("Test para obtener una asistencia por id")
    @Test
    void one() throws ResourceNotFoundException {
        //given
        ApiResponse<AsistenciaDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(asistenciaDTO);

        when(asistenciaService.one(asistenciaDTO.getId())).thenReturn(expectedApiResponse);

        // When
        ApiResponse<AsistenciaDTO> actualApiResponse = asistenciaRESTController.one(asistenciaDTO.getId());

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(asistenciaService, times(1)).one(asistenciaDTO.getId());
    }

    @DisplayName("Test para agregar una asistencia")
    @Test
    void add() throws ResourceNotFoundException {
        // given
        ApiResponse<AsistenciaDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(asistenciaDTO);
        when(asistenciaService.add(asistenciaDTO)).thenReturn(expectedApiResponse);

        //when
        ApiResponse<AsistenciaDTO> actualApiResponse = asistenciaRESTController.add(asistenciaDTO);

        // then
        assertThat(actualApiResponse).isNotNull();
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals("ok", actualApiResponse.getMessage());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getCreateAt()).isEqualTo(expectedApiResponse.getData().getCreateAt());
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        verify(asistenciaService).add(this.asistenciaDTO);
    }

    @DisplayName("Test para actualizar una asistencia")
    @Test
    void update() throws ResourceNotFoundException {
        //given
        AsistenciaDTO asistenciaDTO2 = asistenciaDTO;
        asistenciaDTO2.setState("Falta");
        asistenciaDTO2.setUpdateAt(LocalDateTime.now());
        asistenciaDTO2.setAlumnoDTO(alumnoDTO);

        ClaseDTO claseDTO2= new ClaseDTO();
        claseDTO2.setCreateAt(LocalDateTime.now());
        claseDTO2.setId(UUID.randomUUID().toString());
        claseDTO2.setStatus("CREATED");
        claseDTO2.setCode("AUL001");
        claseDTO2.setDate(Date.valueOf("2020-10-09"));

        asistenciaDTO2.setClaseDTO(claseDTO2);

        ApiResponse<AsistenciaDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(asistenciaDTO2);

        when(asistenciaService.one(asistenciaDTO2.getId())).thenReturn(expectedApiResponse);
        when(asistenciaService.update(asistenciaDTO2)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<AsistenciaDTO> actualApiResponse = asistenciaRESTController.update(asistenciaDTO2);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getUpdateAt()).isEqualTo(expectedApiResponse.getData().getUpdateAt());
        verify(asistenciaService).update(asistenciaDTO2);
    }

    @DisplayName("Test para eliminar una asistencia")
    @Test
    void delete() {
        // given
        ApiResponse<AsistenciaDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(asistenciaDTO);

        when(asistenciaService.delete(asistenciaDTO.getId())).thenReturn(expectedApiResponse);

        // when
        ApiResponse<AsistenciaDTO> actualApiResponse = asistenciaRESTController.delete(asistenciaDTO.getId());

        // then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(asistenciaService, times(1)).delete(asistenciaDTO.getId());
    }

    @DisplayName("Test para el reporte de asistencias de un alumno")
    @Test
    void exportAsistencia() throws ResourceNotFoundException {
        PeriodoDTO periodoDTO = new PeriodoDTO();
        periodoDTO.setId(UUID.randomUUID().toString());

        AnioLectivoDTO anioLectivoDTO = new AnioLectivoDTO();
        anioLectivoDTO.setId(UUID.randomUUID().toString());

        // given
        byte[] pdfBytes = new byte[100];
        when(asistenciaService.exportAsistencia(anyString(), anyString(), anyString())).thenReturn(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(new ByteArrayResource(pdfBytes))
        );

        // when
        ResponseEntity<Resource> response = asistenciaRESTController.exportAsistencia(alumnoDTO.getId(), periodoDTO.getId() ,anioLectivoDTO.getId());
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertEquals(pdfBytes.length, ((ByteArrayResource) response.getBody()).getByteArray().length);
    }
}
