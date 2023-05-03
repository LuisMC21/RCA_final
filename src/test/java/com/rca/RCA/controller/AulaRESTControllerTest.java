package com.rca.RCA.controller;

import com.rca.RCA.entity.AnioLectivoEntity;
import com.rca.RCA.entity.AulaEntity;
import com.rca.RCA.entity.GradoEntity;
import com.rca.RCA.entity.SeccionEntity;
import com.rca.RCA.service.AulaService;
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

class AulaRESTControllerTest {

    @Mock
    private AulaService aulaService;

    @InjectMocks
    private AulaRESTController aulaRESTController;

    private GradoEntity gradoEntity;

    private SeccionEntity seccionEntity;

    private AulaEntity aulaEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        seccionEntity= new SeccionEntity();
        seccionEntity.setName('A');
        seccionEntity.setCode("SEC001");
        seccionEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        seccionEntity.setCreateAt(LocalDateTime.now());

        gradoEntity= new GradoEntity();
        gradoEntity.setName('1');
        gradoEntity.setCode("GR001");
        gradoEntity.setUniqueIdentifier(UUID.randomUUID().toString());

        aulaEntity= new AulaEntity();
        aulaEntity.setCreateAt(LocalDateTime.now());
        aulaEntity.setUniqueIdentifier(UUID.randomUUID().toString());
        aulaEntity.setStatus("CREATED");
        aulaEntity.setCode("AUL001");
        aulaEntity.setSeccionEntity(seccionEntity);
        aulaEntity.setGradoEntity(gradoEntity);
    }

    @DisplayName("Test para listar las aulas")
    @Test
    void list() {
        // Given
        String filter = "";
        int page = 1;
        int size = 10;

        Pagination<AulaDTO> pagination = new Pagination<>();
        pagination.setCountFilter(2);
        pagination.setTotalPages(pagination.processAndGetTotalPages(size));
        List<AulaEntity> aulaEntities = new ArrayList<>();
        aulaEntities.add(aulaEntity);

        AulaEntity aulaEntity2 = new AulaEntity();
        aulaEntity2.setCode("AUL001");
        aulaEntity2.setUpdateAt(LocalDateTime.now());
        aulaEntity2.setGradoEntity(gradoEntity);

        SeccionEntity seccionEntity2 = new SeccionEntity();
        seccionEntity2.setName('B');
        seccionEntity2.setCode("GR002");
        seccionEntity2.setCreateAt(LocalDateTime.now());
        aulaEntity2.setSeccionEntity(seccionEntity2);

        aulaEntities.add(aulaEntity2);

        pagination.setList(aulaEntities.stream().map(AulaEntity::getAulaDTO).collect(Collectors.toList()));

        ApiResponse<Pagination<AulaDTO>> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(pagination);

        when(aulaService.getList(filter, page, size)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<Pagination<AulaDTO>> actualApiResponse = aulaRESTController.list(filter, page, size);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertEquals(expectedApiResponse.getData().getCountFilter(), actualApiResponse.getData().getCountFilter());
        assertThat(actualApiResponse.getMessage()).isEqualTo(expectedApiResponse.getMessage());
        assertThat(actualApiResponse.getData()).isEqualTo(expectedApiResponse.getData());

        verify(aulaService).getList(filter, page, size);
    }

    @DisplayName("Test para obtener un aula por id")
    @Test
    void one() throws ResourceNotFoundException {
        //given
        ApiResponse<AulaDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(aulaEntity.getAulaDTO());

        when(aulaService.one(aulaEntity.getUniqueIdentifier())).thenReturn(expectedApiResponse);

        // When
        ApiResponse<AulaDTO> actualApiResponse = aulaRESTController.one(aulaEntity.getUniqueIdentifier());

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(aulaService, times(1)).one(aulaEntity.getUniqueIdentifier());
    }

    @DisplayName("Test para agregar un aula")
    @Test
    void add() throws ResourceNotFoundException, AttributeException {
        // given
        ApiResponse<AulaDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(aulaEntity.getAulaDTO());
        when(aulaService.add(aulaEntity.getAulaDTO())).thenReturn(expectedApiResponse);

        //when
        ApiResponse<AulaDTO> actualApiResponse = aulaRESTController.add(aulaEntity.getAulaDTO());

        // then
        assertThat(actualApiResponse).isNotNull();
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals("ok", actualApiResponse.getMessage());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getCreateAt()).isEqualTo(expectedApiResponse.getData().getCreateAt());
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());
        verify(aulaService).add(this.aulaEntity.getAulaDTO());
    }

    @DisplayName("Test para actualizar un aula")
    @Test
    void update() throws ResourceNotFoundException, AttributeException {
        //given
        AulaDTO aulaDTO2 = new AulaDTO();
        aulaDTO2.setCode("AUL001");
        aulaDTO2.setUpdateAt(LocalDateTime.now());
        aulaDTO2.setGradoDTO(gradoEntity.getGradoDTO());

        SeccionDTO seccionDTO2 = new SeccionDTO();
        seccionDTO2.setName('B');
        seccionDTO2.setCode("GR002");
        seccionDTO2.setUpdateAt(LocalDateTime.now());

        aulaDTO2.setSeccionDTO(seccionDTO2);

        ApiResponse<AulaDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(aulaDTO2);

        when(aulaService.one(seccionEntity.getUniqueIdentifier())).thenReturn(expectedApiResponse);
        when(aulaService.update(aulaDTO2)).thenReturn(expectedApiResponse);

        // When
        ApiResponse<AulaDTO> actualApiResponse = aulaRESTController.update(aulaDTO2);

        // Then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getCode()).isEqualTo(expectedApiResponse.getData().getCode());
        assertThat(actualApiResponse.getData().getUpdateAt()).isEqualTo(expectedApiResponse.getData().getUpdateAt());
        verify(aulaService).update(aulaDTO2);
    }

    @DisplayName("Test para eliminar un aula")
    @Test
    void delete() throws ResourceNotFoundException {
        // given
        ApiResponse<AulaDTO> expectedApiResponse = new ApiResponse<>();
        expectedApiResponse.setSuccessful(true);
        expectedApiResponse.setMessage("ok");
        expectedApiResponse.setData(aulaEntity.getAulaDTO());

        when(aulaService.delete(aulaEntity.getUniqueIdentifier())).thenReturn(expectedApiResponse);

        // when
        ApiResponse<AulaDTO> actualApiResponse = aulaRESTController.delete(aulaEntity.getUniqueIdentifier());

        // then
        assertTrue(actualApiResponse.isSuccessful());
        assertEquals(expectedApiResponse, actualApiResponse);
        assertThat(actualApiResponse.getData().getId()).isEqualTo(expectedApiResponse.getData().getId());

        verify(aulaService, times(1)).delete(aulaEntity.getUniqueIdentifier());
    }

    @DisplayName("Test para exportar lista de apoderados de un aula  en pdf")
    @Test
    void exportListApoderados() throws ResourceNotFoundException {
        AnioLectivoEntity anioLectivoEntity = new AnioLectivoEntity();
        anioLectivoEntity.setUniqueIdentifier(UUID.randomUUID().toString());

            // Mock the service method call
            byte[] pdfBytes = new byte[100];
            when(aulaService.exportListApoderados(anyString(), anyString())).thenReturn(
                    ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_PDF)
                            .body(new ByteArrayResource(pdfBytes))
            );

            // Call the controller method
            ResponseEntity<Resource> response = aulaRESTController.exportListApoderados(aulaEntity.getUniqueIdentifier(), anioLectivoEntity.getUniqueIdentifier());

            // Verify the response
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
            assertEquals(pdfBytes.length, ((ByteArrayResource) response.getBody()).getByteArray().length);
    }
}