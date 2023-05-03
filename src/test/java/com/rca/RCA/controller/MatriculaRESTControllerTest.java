package com.rca.RCA.controller;

import com.rca.RCA.service.MatriculaService;
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

import java.time.LocalDate;
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

class MatriculaRESTControllerTest {

    @Mock
    private MatriculaService matriculaService;

    @InjectMocks
    private MatriculaRESTController matriculaRESTController;

    private MatriculaDTO matriculaDTO;

    private AulaDTO aulaDTO;

    private AlumnoDTO alumnoDTO;

    private AnioLectivoDTO anioLectivoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        matriculaDTO = new MatriculaDTO();
        matriculaDTO.setCode("MAT001");
        matriculaDTO.setDate(LocalDate.of(2021, 02, 01));
        matriculaDTO.setCreateAt(LocalDateTime.now());
        matriculaDTO.setStatus("CREATED");

        anioLectivoDTO= new AnioLectivoDTO();
        anioLectivoDTO.setCode("ANI001");
        anioLectivoDTO.setName("2020");
        anioLectivoDTO.setCreateAt(LocalDateTime.now());
        anioLectivoDTO.setStatus("CREATED");
        anioLectivoDTO.setId(UUID.randomUUID().toString());

        aulaDTO = new AulaDTO();
        aulaDTO.setId(UUID.randomUUID().toString());
        aulaDTO.setStatus("CREATED");
        aulaDTO.setCode("AUL001");
        aulaDTO.setCreateAt(LocalDateTime.now());

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

        matriculaDTO.setAulaDTO(aulaDTO);
        matriculaDTO.setAlumnoDTO(alumnoDTO);
        matriculaDTO.setAnioLectivoDTO(anioLectivoDTO);
    }

    @DisplayName("Test para listar matrículas")
    @Test
    void list() {
        // Given
        String filter = "";
        int page = 1;
        int size = 10;

        Pagination<MatriculaDTO> pagination = new Pagination<>();
        pagination.setCountFilter(2);
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        List<MatriculaDTO> matriculaDTOS = new ArrayList<>();
        matriculaDTOS.add(matriculaDTO);

        MatriculaDTO matriculaDTO2 = new MatriculaDTO();
        matriculaDTO2.setCode("MAT002");
        matriculaDTO2.setDate(LocalDate.of(2022, 02, 01));
        matriculaDTO2.setCreateAt(LocalDateTime.now());
        matriculaDTO2.setStatus("CREATED");

        AnioLectivoDTO anioLectivoDTO2= new AnioLectivoDTO();
        anioLectivoDTO2.setCode("ANI002");
        anioLectivoDTO2.setName("2021");
        anioLectivoDTO2.setCreateAt(LocalDateTime.now());
        anioLectivoDTO2.setStatus("CREATED");
        anioLectivoDTO2.setId(UUID.randomUUID().toString());

        AulaDTO aulaDTO2 = new AulaDTO();
        aulaDTO2.setId(UUID.randomUUID().toString());
        aulaDTO2.setStatus("CREATED");
        aulaDTO2.setCode("AUL002");
        aulaDTO2.setCreateAt(LocalDateTime.now());

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

        matriculaDTO2.setAulaDTO(aulaDTO2);
        matriculaDTO2.setAlumnoDTO(alumnoDTO);
        matriculaDTO2.setAnioLectivoDTO(anioLectivoDTO2);
        pagination.setList(matriculaDTOS.stream().collect(Collectors.toList()));

        ApiResponse<Pagination<MatriculaDTO>> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(pagination);

        when(matriculaService.getList(filter, page, size)).thenReturn(expectedApiResponse);
        // When
        ApiResponse<Pagination<MatriculaDTO>> actualApiResponse = matriculaRESTController.list(filter, page, size);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertEquals(expectedApiResponse.getData().getCountFilter(), actualApiResponse.getData().getCountFilter());
        assertThat(actualApiResponse.getMessage()).isEqualTo(expectedApiResponse.getMessage());
        assertThat(actualApiResponse.getData()).isEqualTo(expectedApiResponse.getData());

        verify(matriculaService).getList(filter, page, size);
    }

    @DisplayName("Test para obtener una matrícula por id")
    @Test
    void one() throws ResourceNotFoundException {
        //given
        ApiResponse<MatriculaDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(matriculaDTO);

        when(matriculaService.one(matriculaDTO.getId())).thenReturn(expectedApiResponse);

        // When
        ApiResponse<MatriculaDTO> actualApiResponse = matriculaRESTController.one(matriculaDTO.getId());

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(matriculaService, times(1)).one(matriculaDTO.getId());
    }

    @DisplayName("Test para agregar una matrícula")
    @Test
    void add() throws ResourceNotFoundException, AttributeException {
        // given
        ApiResponse<MatriculaDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(matriculaDTO);
        when(matriculaService.add(matriculaDTO)).thenReturn(expectedApiResponse);

        //when
        ApiResponse<MatriculaDTO> actualApiResponse = matriculaRESTController.add(matriculaDTO);

        // then
        assertThat(actualApiResponse).isNotNull();
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals("ok", actualApiResponse.getMessage());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getCreateAt()).isEqualTo(expectedApiResponse.getData().getCreateAt());
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        verify(matriculaService).add(this.matriculaDTO);
    }

    @DisplayName("Test para actualizar una matrícula")
    @Test
    void update() throws ResourceNotFoundException, AttributeException {
        //given
        MatriculaDTO matriculaDTO2 = new MatriculaDTO();
        AnioLectivoDTO anioLectivoDTO2= new AnioLectivoDTO();
        anioLectivoDTO2.setCode("ANI00w");
        anioLectivoDTO2.setName("2021");
        anioLectivoDTO2.setCreateAt(LocalDateTime.now());
        anioLectivoDTO2.setStatus("CREATED");
        anioLectivoDTO2.setId(UUID.randomUUID().toString());

        matriculaDTO2.setAnioLectivoDTO(anioLectivoDTO2);

        ApiResponse<MatriculaDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(matriculaDTO2);

        when(matriculaService.one(matriculaDTO2.getId())).thenReturn(expectedApiResponse);
        when(matriculaService.update(matriculaDTO2)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<MatriculaDTO> actualApiResponse = matriculaRESTController.update(matriculaDTO2);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getUpdateAt()).isEqualTo(expectedApiResponse.getData().getUpdateAt());
        verify(matriculaService).update(matriculaDTO2);
    }

    @DisplayName("Test para eliminar una matrícula")
    @Test
    void delete() throws ResourceNotFoundException {
        // given
        ApiResponse<MatriculaDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(matriculaDTO);

        when(matriculaService.delete(matriculaDTO.getId())).thenReturn(expectedApiResponse);

        // when
        ApiResponse<MatriculaDTO> actualApiResponse = matriculaRESTController.delete(matriculaDTO.getId());

        // then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(matriculaService, times(1)).delete(matriculaDTO.getId());
    }

    @DisplayName("Test para exportar lista de alumnos de un aula  en pdf")
    @Test
    void exportListAlumnos() throws ResourceNotFoundException {
        // given
        byte[] pdfBytes = new byte[100];
        when(matriculaService.exportListaAlumnos(anyString(), anyString())).thenReturn(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(new ByteArrayResource(pdfBytes))
        );

        // when
        ResponseEntity<Resource> response = matriculaRESTController.exportListAlumnos(aulaDTO.getId(), anioLectivoDTO.getId());
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertEquals(pdfBytes.length, ((ByteArrayResource) response.getBody()).getByteArray().length);
    }

    @DisplayName("Test para exportar matrícula de un alumno en un año específico  en pdf")
    @Test
    void exportMatricula() throws ResourceNotFoundException {
    // given
        byte[] pdfBytes = new byte[100];
        when(matriculaService.exportMatricula(anyString(), anyString())).thenReturn(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(new ByteArrayResource(pdfBytes))
        );

        // when
        ResponseEntity<Resource> response = matriculaRESTController.exportMatricula(alumnoDTO.getId(), anioLectivoDTO.getId());
        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertEquals(pdfBytes.length, ((ByteArrayResource) response.getBody()).getByteArray().length);
    }
}