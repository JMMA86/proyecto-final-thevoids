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
import org.thevoids.oncologic.dto.entity.AppointmentTypeDTO;
import org.thevoids.oncologic.entity.AppointmentType;
import org.thevoids.oncologic.mapper.AppointmentTypeMapper;
import org.thevoids.oncologic.service.AppointmentTypeService;

class RestAppointmentTypeControllerUnitTest {

    @InjectMocks
    private RestAppointmentTypeController controller;

    @Mock
    private AppointmentTypeService appointmentTypeService;

    @Mock
    private AppointmentTypeMapper appointmentTypeMapper;

    private AppointmentType type;
    private AppointmentTypeDTO typeDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        type = new AppointmentType();
        type.setTypeId(1L);
        type.setTypeName("Consultation");
        type.setStandardDuration(30);

        typeDTO = new AppointmentTypeDTO();
        typeDTO.setTypeId(1L);
        typeDTO.setTypeName("Consultation");
        typeDTO.setStandardDuration(30);
    }

    @Test
    void getAllAppointmentTypes_Success() {
        when(appointmentTypeService.getAllAppointmentTypes()).thenReturn(Arrays.asList(type));
        when(appointmentTypeMapper.toAppointmentTypeDTO(type)).thenReturn(typeDTO);

        ResponseEntity<List<AppointmentTypeDTO>> response = controller.getAllAppointmentTypes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(typeDTO.getTypeId(), response.getBody().get(0).getTypeId());
    }

    @Test
    void getAllAppointmentTypes_InternalServerError() {
        when(appointmentTypeService.getAllAppointmentTypes()).thenThrow(new RuntimeException());
        ResponseEntity<List<AppointmentTypeDTO>> response = controller.getAllAppointmentTypes();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void getAppointmentTypeById_Success() {
        when(appointmentTypeService.getAppointmentTypeById(1L)).thenReturn(type);
        when(appointmentTypeMapper.toAppointmentTypeDTO(type)).thenReturn(typeDTO);

        ResponseEntity<AppointmentTypeDTO> response = controller.getAppointmentTypeById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(typeDTO, response.getBody());
    }

    @Test
    void getAppointmentTypeById_NotFound() {
        when(appointmentTypeService.getAppointmentTypeById(1L)).thenReturn(null);

        ResponseEntity<AppointmentTypeDTO> response = controller.getAppointmentTypeById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAppointmentTypeById_InternalServerError() {
        when(appointmentTypeService.getAppointmentTypeById(1L)).thenThrow(new RuntimeException());
        ResponseEntity<AppointmentTypeDTO> response = controller.getAppointmentTypeById(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void createAppointmentType_Success() {
        when(appointmentTypeMapper.toAppointmentType(typeDTO)).thenReturn(type);
        when(appointmentTypeService.createAppointmentType(type)).thenReturn(type);
        when(appointmentTypeMapper.toAppointmentTypeDTO(type)).thenReturn(typeDTO);

        ResponseEntity<AppointmentTypeDTO> response = controller.createAppointmentType(typeDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(typeDTO, response.getBody());
    }

    @Test
    void createAppointmentType_BadRequest() {
        when(appointmentTypeMapper.toAppointmentType(typeDTO)).thenReturn(type);
        when(appointmentTypeService.createAppointmentType(type)).thenThrow(new IllegalArgumentException());

        ResponseEntity<AppointmentTypeDTO> response = controller.createAppointmentType(typeDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createAppointmentType_InternalServerError() {
        when(appointmentTypeMapper.toAppointmentType(typeDTO)).thenReturn(type);
        when(appointmentTypeService.createAppointmentType(type)).thenThrow(new RuntimeException());
        ResponseEntity<AppointmentTypeDTO> response = controller.createAppointmentType(typeDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void updateAppointmentType_Success() {
        when(appointmentTypeMapper.toAppointmentType(typeDTO)).thenReturn(type);
        when(appointmentTypeService.updateAppointmentType(type)).thenReturn(type);
        when(appointmentTypeMapper.toAppointmentTypeDTO(type)).thenReturn(typeDTO);

        ResponseEntity<AppointmentTypeDTO> response = controller.updateAppointmentType(1L, typeDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(typeDTO, response.getBody());
    }

    @Test
    void updateAppointmentType_BadRequest() {
        when(appointmentTypeMapper.toAppointmentType(typeDTO)).thenReturn(type);
        when(appointmentTypeService.updateAppointmentType(type)).thenThrow(new IllegalArgumentException());

        ResponseEntity<AppointmentTypeDTO> response = controller.updateAppointmentType(1L, typeDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateAppointmentType_InternalServerError() {
        when(appointmentTypeMapper.toAppointmentType(typeDTO)).thenReturn(type);
        when(appointmentTypeService.updateAppointmentType(type)).thenThrow(new RuntimeException());
        ResponseEntity<AppointmentTypeDTO> response = controller.updateAppointmentType(1L, typeDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deleteAppointmentType_Success() {
        ResponseEntity<Void> response = controller.deleteAppointmentType(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(appointmentTypeService, times(1)).deleteAppointmentType(1L);
    }

    @Test
    void deleteAppointmentType_NotFound() {
        doThrow(new IllegalArgumentException()).when(appointmentTypeService).deleteAppointmentType(1L);

        ResponseEntity<Void> response = controller.deleteAppointmentType(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteAppointmentType_InternalServerError() {
        doThrow(new RuntimeException()).when(appointmentTypeService).deleteAppointmentType(1L);
        ResponseEntity<Void> response = controller.deleteAppointmentType(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
