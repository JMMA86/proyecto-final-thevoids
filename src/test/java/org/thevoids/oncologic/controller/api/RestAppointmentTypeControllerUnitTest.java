package org.thevoids.oncologic.controller.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.thevoids.oncologic.dto.AppointmentTypeDTO;
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

        List<AppointmentTypeDTO> result = controller.getAllAppointmentTypes();

        assertEquals(1, result.size());
        assertEquals(typeDTO.getTypeId(), result.get(0).getTypeId());
    }

    @Test
    void getAppointmentTypeById_Success() {
        when(appointmentTypeService.getAppointmentTypeById(1L)).thenReturn(type);
        when(appointmentTypeMapper.toAppointmentTypeDTO(type)).thenReturn(typeDTO);

        ResponseEntity<AppointmentTypeDTO> response = controller.getAppointmentTypeById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(typeDTO, response.getBody());
    }

    @Test
    void getAppointmentTypeById_NotFound() {
        when(appointmentTypeService.getAppointmentTypeById(1L)).thenReturn(null);

        ResponseEntity<AppointmentTypeDTO> response = controller.getAppointmentTypeById(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void createAppointmentType_Success() {
        when(appointmentTypeMapper.toAppointmentType(typeDTO)).thenReturn(type);
        when(appointmentTypeService.createAppointmentType(type)).thenReturn(type);
        when(appointmentTypeMapper.toAppointmentTypeDTO(type)).thenReturn(typeDTO);

        ResponseEntity<AppointmentTypeDTO> response = controller.createAppointmentType(typeDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(typeDTO, response.getBody());
    }

    @Test
    void createAppointmentType_BadRequest() {
        when(appointmentTypeMapper.toAppointmentType(typeDTO)).thenReturn(type);
        when(appointmentTypeService.createAppointmentType(type)).thenThrow(new IllegalArgumentException());

        ResponseEntity<AppointmentTypeDTO> response = controller.createAppointmentType(typeDTO);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void updateAppointmentType_Success() {
        when(appointmentTypeMapper.toAppointmentType(typeDTO)).thenReturn(type);
        when(appointmentTypeService.updateAppointmentType(type)).thenReturn(type);
        when(appointmentTypeMapper.toAppointmentTypeDTO(type)).thenReturn(typeDTO);

        ResponseEntity<AppointmentTypeDTO> response = controller.updateAppointmentType(1L, typeDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(typeDTO, response.getBody());
    }

    @Test
    void updateAppointmentType_BadRequest() {
        when(appointmentTypeMapper.toAppointmentType(typeDTO)).thenReturn(type);
        when(appointmentTypeService.updateAppointmentType(type)).thenThrow(new IllegalArgumentException());

        ResponseEntity<AppointmentTypeDTO> response = controller.updateAppointmentType(1L, typeDTO);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void deleteAppointmentType_Success() {
        ResponseEntity<Void> response = controller.deleteAppointmentType(1L);
        assertEquals(200, response.getStatusCodeValue());
        verify(appointmentTypeService, times(1)).deleteAppointmentType(1L);
    }

    @Test
    void deleteAppointmentType_NotFound() {
        doThrow(new IllegalArgumentException()).when(appointmentTypeService).deleteAppointmentType(1L);

        ResponseEntity<Void> response = controller.deleteAppointmentType(1L);

        assertEquals(404, response.getStatusCodeValue());
    }
}
