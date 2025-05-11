package org.thevoids.oncologic.controller.api;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.thevoids.oncologic.dto.ClinicDTO;
import org.thevoids.oncologic.entity.Clinic;
import org.thevoids.oncologic.mapper.ClinicMapper;
import org.thevoids.oncologic.service.ClinicService;

class RestClinicControllerUnitTest {

    @InjectMocks
    private RestClinicController controller;

    @Mock
    private ClinicService clinicService;

    @Mock
    private ClinicMapper clinicMapper;

    private Clinic clinic;
    private ClinicDTO clinicDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clinic = new Clinic();
        clinic.setId(1L);
        clinic.setName("City Clinic");

        clinicDTO = new ClinicDTO();
        clinicDTO.setId(1L);
        clinicDTO.setName("City Clinic");
    }

    @SuppressWarnings("null")
    @Test
    void getAllClinics_Success() {
        when(clinicService.getAllClinics()).thenReturn(Arrays.asList(clinic));
        when(clinicMapper.toClinicDTO(clinic)).thenReturn(clinicDTO);

        ResponseEntity<List<ClinicDTO>> response = controller.getAllClinics();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(clinicDTO.getId(), response.getBody().get(0).getId());
    }

    @Test
    void getAllClinics_InternalServerError() {
        when(clinicService.getAllClinics()).thenThrow(new RuntimeException());
        ResponseEntity<List<ClinicDTO>> response = controller.getAllClinics();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void getClinicById_Success() {
        when(clinicService.getClinicById(1L)).thenReturn(clinic);
        when(clinicMapper.toClinicDTO(clinic)).thenReturn(clinicDTO);

        ResponseEntity<ClinicDTO> response = controller.getClinicById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clinicDTO, response.getBody());
    }

    @Test
    void getClinicById_NotFound() {
        when(clinicService.getClinicById(1L)).thenReturn(null);

        ResponseEntity<ClinicDTO> response = controller.getClinicById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getClinicById_InternalServerError() {
        when(clinicService.getClinicById(1L)).thenThrow(new RuntimeException());
        ResponseEntity<ClinicDTO> response = controller.getClinicById(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void createClinic_Success() {
        when(clinicMapper.toClinic(clinicDTO)).thenReturn(clinic);
        when(clinicService.createClinic(clinic)).thenReturn(clinic);
        when(clinicMapper.toClinicDTO(clinic)).thenReturn(clinicDTO);

        ResponseEntity<ClinicDTO> response = controller.createClinic(clinicDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(clinicDTO, response.getBody());
    }

    @Test
    void createClinic_BadRequest() {
        when(clinicMapper.toClinic(clinicDTO)).thenReturn(clinic);
        when(clinicService.createClinic(clinic)).thenThrow(new IllegalArgumentException());

        ResponseEntity<ClinicDTO> response = controller.createClinic(clinicDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createClinic_InternalServerError() {
        when(clinicMapper.toClinic(clinicDTO)).thenReturn(clinic);
        when(clinicService.createClinic(clinic)).thenThrow(new RuntimeException());
        ResponseEntity<ClinicDTO> response = controller.createClinic(clinicDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void updateClinic_Success() {
        when(clinicMapper.toClinic(clinicDTO)).thenReturn(clinic);
        when(clinicService.updateClinic(1L, clinic)).thenReturn(clinic);
        when(clinicMapper.toClinicDTO(clinic)).thenReturn(clinicDTO);

        ResponseEntity<ClinicDTO> response = controller.updateClinic(1L, clinicDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clinicDTO, response.getBody());
    }

    @Test
    void updateClinic_BadRequest() {
        when(clinicMapper.toClinic(clinicDTO)).thenReturn(clinic);
        when(clinicService.updateClinic(1L, clinic)).thenThrow(new IllegalArgumentException());

        ResponseEntity<ClinicDTO> response = controller.updateClinic(1L, clinicDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateClinic_InternalServerError() {
        when(clinicMapper.toClinic(clinicDTO)).thenReturn(clinic);
        when(clinicService.updateClinic(1L, clinic)).thenThrow(new RuntimeException());
        ResponseEntity<ClinicDTO> response = controller.updateClinic(1L, clinicDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deleteClinic_Success() {
        ResponseEntity<Void> response = controller.deleteClinic(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(clinicService, times(1)).deleteClinic(1L);
    }

    @Test
    void deleteClinic_NotFound() {
        doThrow(new IllegalArgumentException()).when(clinicService).deleteClinic(1L);

        ResponseEntity<Void> response = controller.deleteClinic(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteClinic_InternalServerError() {
        doThrow(new RuntimeException()).when(clinicService).deleteClinic(1L);
        ResponseEntity<Void> response = controller.deleteClinic(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
