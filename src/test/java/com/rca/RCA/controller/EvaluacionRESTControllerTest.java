package com.rca.RCA.controller;

import com.rca.RCA.service.EvaluacionService;
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


class EvaluacionRESTControllerTest {

    @Mock
    private EvaluacionService evaluacionService;

    @InjectMocks
    private EvaluacionRESTController evaluacionRESTController;

    private EvaluacionDTO evaluacionDTO;

    private AlumnoDTO alumnoDTO;

    private PeriodoDTO periodoDTO;

    private DocentexCursoDTO docentexCursoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        evaluacionDTO = new EvaluacionDTO();
        evaluacionDTO.setId(UUID.randomUUID().toString());
        evaluacionDTO.setDate(Date.valueOf("2020-10-01"));
        evaluacionDTO.setCode("EVA001");
        evaluacionDTO.setCreateAt(LocalDateTime.now());
        evaluacionDTO.setNote("15");

        alumnoDTO = new AlumnoDTO();
        alumnoDTO.setId(UUID.randomUUID().toString());

        docentexCursoDTO = new DocentexCursoDTO();
        docentexCursoDTO.setId(UUID.randomUUID().toString());

        evaluacionDTO.setAlumnoDTO(alumnoDTO);
        evaluacionDTO.setDocentexCursoDTO(docentexCursoDTO);

        periodoDTO= new PeriodoDTO();
        periodoDTO.setId(UUID.randomUUID().toString());
    }

    @DisplayName("Test para listar evaluaciones")
    @Test
    void list() {
        // Given
        String filter = "";
        int page = 1;
        int size = 10;

        Pagination<EvaluacionDTO> pagination = new Pagination<>();
        pagination.setCountFilter(1);
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        List<EvaluacionDTO> evaluacionDTOS = new ArrayList<>();
        evaluacionDTOS.add(evaluacionDTO);

        pagination.setList(evaluacionDTOS.stream().collect(Collectors.toList()));

        ApiResponse<Pagination<EvaluacionDTO>> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(pagination);

        when(evaluacionService.getList(filter, page, size)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<Pagination<EvaluacionDTO>> actualApiResponse = evaluacionRESTController.list(filter, page, size);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertEquals(expectedApiResponse.getData().getCountFilter(), actualApiResponse.getData().getCountFilter());
        assertThat(actualApiResponse.getMessage()).isEqualTo(expectedApiResponse.getMessage());
        assertThat(actualApiResponse.getData()).isEqualTo(expectedApiResponse.getData());

        verify(evaluacionService).getList(filter, page, size);
    }

    @DisplayName("Test para obtener una evaluacion con id")
    @Test
    void one() throws ResourceNotFoundException {
        //given
        ApiResponse<EvaluacionDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(evaluacionDTO);

        when(evaluacionService.one(evaluacionDTO.getId())).thenReturn(expectedApiResponse);

        // When
        ApiResponse<EvaluacionDTO> actualApiResponse = evaluacionRESTController.one(evaluacionDTO.getId());

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(evaluacionService, times(1)).one(evaluacionDTO.getId());
    }


    @DisplayName("Test para agregar una evaluacion")
    @Test
    void add() throws ResourceNotFoundException {
        // given
        ApiResponse<EvaluacionDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(evaluacionDTO);
        when(evaluacionService.add(evaluacionDTO)).thenReturn(expectedApiResponse);

        //when
        ApiResponse<EvaluacionDTO> actualApiResponse = evaluacionRESTController.add(evaluacionDTO);

        // then
        assertThat(actualApiResponse).isNotNull();
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals("ok", actualApiResponse.getMessage());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getCreateAt()).isEqualTo(expectedApiResponse.getData().getCreateAt());
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        verify(evaluacionService).add(this.evaluacionDTO);
    }


    @DisplayName("Test para actualizar una evaluacion")
    @Test
    void update() throws ResourceNotFoundException {
        //given
        EvaluacionDTO evaluacionDTO2 = evaluacionDTO;
        evaluacionDTO2.setDate(Date.valueOf("2021-11-10"));

        ApiResponse<EvaluacionDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(evaluacionDTO2);

        when(evaluacionService.one(evaluacionDTO2.getId())).thenReturn(expectedApiResponse);
        when(evaluacionService.update(evaluacionDTO2)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<EvaluacionDTO> actualApiResponse = evaluacionRESTController.update(evaluacionDTO2);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getUpdateAt()).isEqualTo(expectedApiResponse.getData().getUpdateAt());
        verify(evaluacionService).update(evaluacionDTO2);
    }

    @DisplayName("Test para eliminar una evaluacion")
    @Test
    void delete() {
        // given
        ApiResponse<EvaluacionDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(evaluacionDTO);

        when(evaluacionService.delete(evaluacionDTO.getId())).thenReturn(expectedApiResponse);

        // when
        ApiResponse<EvaluacionDTO> actualApiResponse = evaluacionRESTController.delete(evaluacionDTO.getId());

        // then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(evaluacionService, times(1)).delete(evaluacionDTO.getId());
    }

    @Test
    void notas() {
        //given
        AnioLectivoDTO anioLectivoDTO = new AnioLectivoDTO();
        anioLectivoDTO.setId(UUID.randomUUID().toString());
        byte[] pdfBytes = new byte[100];
        when(evaluacionService.exportBoletaNotas(anyString(), anyString())).thenReturn(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(new ByteArrayResource(pdfBytes))
        );

        // when
        ResponseEntity<Resource> response = evaluacionRESTController.notas(periodoDTO.getId(), alumnoDTO.getId());
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertEquals(pdfBytes.length, ((ByteArrayResource) response.getBody()).getByteArray().length);
    }

    @Test
    void exportNotas() {
        //given
        CursoDTO cursoDTO = new CursoDTO();
        cursoDTO.setId(UUID.randomUUID().toString());
        docentexCursoDTO.setCursoDTO(cursoDTO);

        AnioLectivoDTO anioLectivoDTO = new AnioLectivoDTO();
        anioLectivoDTO.setId(UUID.randomUUID().toString());

        byte[] pdfBytes = new byte[100];
        when(evaluacionService.exportNotas(anyString(), anyString(), anyString())).thenReturn(
                ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_PDF)
                            .body(new ByteArrayResource(pdfBytes))
                );

        // when
        ResponseEntity<Resource> response = evaluacionRESTController.exportNotas(anioLectivoDTO.getId(), periodoDTO.getId(), docentexCursoDTO.getCursoDTO().getId());
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertEquals(pdfBytes.length, ((ByteArrayResource) response.getBody()).getByteArray().length);
        }
}
