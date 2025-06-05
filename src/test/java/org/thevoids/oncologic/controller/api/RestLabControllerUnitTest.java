package org.thevoids.oncologic.controller.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.thevoids.oncologic.dto.entity.LabDTO;
import org.thevoids.oncologic.exception.InvalidOperationException;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.service.FileService;
import org.thevoids.oncologic.service.LabService;

import com.fasterxml.jackson.databind.ObjectMapper;

class RestLabControllerUnitTest {

    @InjectMocks
    private RestLabController labController;

    @Mock
    private LabService labService;

    @Mock
    private FileService fileService;

    @Mock
    private ObjectMapper objectMapper;

    private LabDTO testLab1;
    private LabDTO testLab2;
    private Date testDate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test date
        testDate = new Date();

        // Initialize test labs
        testLab1 = new LabDTO();
        testLab1.setLabId(1L);
        testLab1.setPatientId(1L);
        testLab1.setLabTechnicianId(1L);
        testLab1.setTestType("Blood Test");
        testLab1.setRequestDate(testDate);
        testLab1.setCompletionDate(null);
        testLab1.setResult(null);
        testLab1.setAttachment(null);

        testLab2 = new LabDTO();
        testLab2.setLabId(2L);
        testLab2.setPatientId(2L);
        testLab2.setLabTechnicianId(2L);
        testLab2.setTestType("X-Ray");
        testLab2.setRequestDate(testDate);
        testLab2.setCompletionDate(null);
        testLab2.setResult(null);
        testLab2.setAttachment(null);
    }

    @Test
    void testGetAllLabs_Success() {
        // Arrange
        when(labService.getAllLabs()).thenReturn(Arrays.asList(testLab1, testLab2));

        // Act
        ResponseEntity<?> response = labController.getAllLabs(); // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        List<?> labsRaw = (List<?>) response.getBody();
        assertNotNull(labsRaw);
        assertEquals(2, labsRaw.size());
        assertTrue(labsRaw.get(0) instanceof LabDTO);
        assertEquals("Blood Test", ((LabDTO) labsRaw.get(0)).getTestType());
        assertEquals("X-Ray", ((LabDTO) labsRaw.get(1)).getTestType());
    }

    @Test
    void testGetAllLabs_Failure() {
        // Arrange
        when(labService.getAllLabs()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = labController.getAllLabs();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        // For INTERNAL_SERVER_ERROR, body is expected to be null (controller returns
        // .body(null))
        assertNull(response.getBody());
    }

    @Test
    void testGetLabById_Success() {
        // Arrange
        when(labService.getLabById(1L)).thenReturn(testLab1);

        // Act
        ResponseEntity<?> response = labController.getLabById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        LabDTO lab = (LabDTO) response.getBody();
        assertNotNull(lab);
        assertEquals(1L, lab.getLabId());
        assertEquals("Blood Test", lab.getTestType());
    }

    @Test
    void testGetLabById_NotFound() {
        // Arrange
        when(labService.getLabById(1L)).thenThrow(new ResourceNotFoundException("Lab", "id", 1L));

        // Act
        ResponseEntity<?> response = labController.getLabById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // For NOT_FOUND, body is expected to be null (controller returns .body(null))
        assertNull(response.getBody());
    }

    @Test
    void testGetLabById_NullId() {
        // Act
        ResponseEntity<?> response = labController.getLabById(null);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testAssignLab_Success() {
        // Arrange
        when(labService.assignLab(1L, 1L, testDate, "Blood Test", null, null)).thenReturn(testLab1);

        // Act
        LabDTO request = new LabDTO();
        request.setPatientId(1L);
        request.setLabTechnicianId(1L);
        request.setRequestDate(testDate);
        request.setTestType("Blood Test");
        ResponseEntity<LabDTO> response = labController.assignLab(request);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        LabDTO lab = response.getBody();
        assertNotNull(lab);
        assertEquals(1L, lab.getLabId());
        assertEquals("Blood Test", lab.getTestType());
    }

    @Test
    void testAssignLab_BadRequest() {
        // Arrange
        when(labService.assignLab(1L, 1L, testDate, "Blood Test", null, null))
                .thenThrow(new InvalidOperationException("Invalid data"));

        // Act
        LabDTO request = new LabDTO();
        request.setPatientId(1L);
        request.setLabTechnicianId(1L);
        request.setRequestDate(testDate);
        request.setTestType("Blood Test");
        ResponseEntity<LabDTO> response = labController.assignLab(request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // For BAD_REQUEST, body is expected to be null (controller returns .body(null))
        assertNull(response.getBody());
    }

    @Test
    void testAssignLab_NullRequest() {
        // Act
        ResponseEntity<LabDTO> response = labController.assignLab(null);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdateLab_Success() {
        // Arrange
        when(labService.updateLab(any(LabDTO.class))).thenReturn(testLab1);

        // Act
        ResponseEntity<?> response = labController.updateLab(1L, testLab1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        LabDTO lab = (LabDTO) response.getBody();
        assertNotNull(lab);
        assertEquals(1L, lab.getLabId());
        assertEquals("Blood Test", lab.getTestType());
        verify(labService, times(1)).updateLab(any(LabDTO.class));
    }

    @Test
    void testUpdateLab_BadRequest() {
        // Arrange
        when(labService.updateLab(any(LabDTO.class))).thenThrow(new InvalidOperationException("Invalid data"));

        // Act
        ResponseEntity<?> response = labController.updateLab(1L, testLab1);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // For BAD_REQUEST, body is expected to be null (controller returns .body(null))
        assertNull(response.getBody());
    }

    @Test
    void testUpdateLab_NullBody() {
        // Act
        ResponseEntity<?> response = labController.updateLab(1L, null);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDeleteLab_Success() {
        // Act
        ResponseEntity<?> response = labController.deleteLab(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(labService, times(1)).deleteLab(1L);
    }

    @Test
    void testDeleteLab_NotFound() {
        // Arrange
        doThrow(new ResourceNotFoundException("Lab", "id", 1L)).when(labService).deleteLab(anyLong());

        // Act
        ResponseEntity<?> response = labController.deleteLab(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // For NOT_FOUND, body is expected to be null (controller returns .build())
        assertNull(response.getBody());
    }

    @Test
    void testDeleteLab_NullId() {
        // Act
        ResponseEntity<?> response = labController.deleteLab(null);
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testGetLabById_RuntimeException() {
        when(labService.getLabById(1L)).thenThrow(new RuntimeException("Unexpected error"));
        ResponseEntity<?> response = labController.getLabById(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetAllLabs_RuntimeException() {
        when(labService.getAllLabs()).thenThrow(new RuntimeException("Unexpected error"));
        ResponseEntity<?> response = labController.getAllLabs();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testAssignLab_RuntimeException() {
        when(labService.assignLab(anyLong(), anyLong(), any(), any(), any(), any()))
                .thenThrow(new RuntimeException("Unexpected error"));
        LabDTO request = new LabDTO();
        request.setPatientId(1L);
        request.setLabTechnicianId(1L);
        request.setRequestDate(new java.util.Date());
        request.setTestType("Blood Test");
        ResponseEntity<LabDTO> response = labController.assignLab(request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateLab_RuntimeException() {
        when(labService.updateLab(any(LabDTO.class))).thenThrow(new RuntimeException("Unexpected error"));
        ResponseEntity<?> response = labController.updateLab(1L, testLab1);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testDeleteLab_RuntimeException() {
        doThrow(new RuntimeException("Unexpected error")).when(labService).deleteLab(anyLong());
        ResponseEntity<?> response = labController.deleteLab(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testAssignLabWithFile_Success() throws Exception {
        // Arrange
        String labData = "{\"patientId\":1,\"labTechnicianId\":1,\"requestDate\":\"2023-12-01T10:00:00Z\"}";
        MultipartFile mockFile = mock(MultipartFile.class);
        when(objectMapper.readValue(labData, LabDTO.class)).thenReturn(testLab1);
        when(labService.assignLabWithFile(eq(1L), eq(1L), any(Date.class), eq("Blood Test"), eq(null), eq(null),
                eq(mockFile))).thenReturn(testLab1);

        // Act
        ResponseEntity<LabDTO> response = labController.assignLabWithFile(labData, mockFile);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        LabDTO lab = response.getBody();
        assertNotNull(lab);
        assertEquals(1L, lab.getLabId());
        assertEquals("Blood Test", lab.getTestType());
        verify(labService).assignLabWithFile(eq(1L), eq(1L), any(Date.class), eq("Blood Test"), eq(null), eq(null),
                eq(mockFile));
    }

    @Test
    void testAssignLabWithFile_Success_NoFile() throws Exception {
        // Arrange
        String labData = "{\"patientId\":1,\"labTechnicianId\":1,\"requestDate\":\"2023-12-01T10:00:00Z\"}";
        when(objectMapper.readValue(labData, LabDTO.class)).thenReturn(testLab1);
        when(labService.assignLabWithFile(eq(1L), eq(1L), any(Date.class), eq("Blood Test"), eq(null), eq(null),
                eq(null))).thenReturn(testLab1);

        // Act
        ResponseEntity<LabDTO> response = labController.assignLabWithFile(labData, null);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        LabDTO lab = response.getBody();
        assertNotNull(lab);
        assertEquals(1L, lab.getLabId());
        verify(labService).assignLabWithFile(eq(1L), eq(1L), any(Date.class), eq("Blood Test"), eq(null), eq(null),
                eq(null));
    }

    @Test
    void testAssignLabWithFile_InvalidJson() throws Exception {
        // Arrange
        String invalidLabData = "invalid json";
        MultipartFile mockFile = mock(MultipartFile.class);

        when(objectMapper.readValue(invalidLabData, LabDTO.class)).thenThrow(new RuntimeException("Invalid JSON"));

        // Act
        ResponseEntity<LabDTO> response = labController.assignLabWithFile(invalidLabData, mockFile);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testAssignLabWithFile_NullLabData() throws Exception {
        // Arrange
        String labData = "{}";
        MultipartFile mockFile = mock(MultipartFile.class);

        when(objectMapper.readValue(labData, LabDTO.class)).thenReturn(null);

        // Act
        ResponseEntity<LabDTO> response = labController.assignLabWithFile(labData, mockFile);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testAssignLabWithFile_InvalidOperation() throws Exception {
        // Arrange
        String labData = "{\"patientId\":1,\"labTechnicianId\":1,\"requestDate\":\"2023-12-01T10:00:00Z\"}";
        MultipartFile mockFile = mock(MultipartFile.class);

        when(objectMapper.readValue(labData, LabDTO.class)).thenReturn(testLab1);
        when(labService.assignLabWithFile(eq(1L), eq(1L), any(Date.class), eq("Blood Test"), eq(null), eq(null),
                eq(mockFile)))
                .thenThrow(new InvalidOperationException("Invalid file"));

        // Act
        ResponseEntity<LabDTO> response = labController.assignLabWithFile(labData, mockFile);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testAssignLabWithFile_ResourceNotFound() throws Exception {
        // Arrange
        String labData = "{\"patientId\":1,\"labTechnicianId\":1,\"requestDate\":\"2023-12-01T10:00:00Z\"}";
        MultipartFile mockFile = mock(MultipartFile.class);

        when(objectMapper.readValue(labData, LabDTO.class)).thenReturn(testLab1);
        when(labService.assignLabWithFile(eq(1L), eq(1L), any(Date.class), eq("Blood Test"), eq(null), eq(null),
                eq(mockFile)))
                .thenThrow(new ResourceNotFoundException("Patient", "id", 1L));

        // Act
        ResponseEntity<LabDTO> response = labController.assignLabWithFile(labData, mockFile);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateLabWithFile_Success() throws Exception {
        // Arrange
        Long labId = 1L;
        String labData = "{\"labId\":1,\"testType\":\"Updated Blood Test\"}";
        MultipartFile mockFile = mock(MultipartFile.class);
        LabDTO updatedLab = new LabDTO();
        updatedLab.setLabId(labId);
        updatedLab.setTestType("Updated Blood Test");

        when(objectMapper.readValue(labData, LabDTO.class)).thenReturn(updatedLab);
        when(labService.updateLabWithFile(any(LabDTO.class), eq(mockFile))).thenReturn(updatedLab);

        // Act
        ResponseEntity<LabDTO> response = labController.updateLabWithFile(labId, labData, mockFile);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        LabDTO lab = response.getBody();
        assertNotNull(lab);
        assertEquals(labId, lab.getLabId());
        assertEquals("Updated Blood Test", lab.getTestType());
        verify(labService).updateLabWithFile(any(LabDTO.class), eq(mockFile));
    }

    @Test
    void testUpdateLabWithFile_Success_NoFile() throws Exception {
        // Arrange
        Long labId = 1L;
        String labData = "{\"labId\":1,\"testType\":\"Updated Blood Test\"}";
        LabDTO updatedLab = new LabDTO();
        updatedLab.setLabId(labId);
        updatedLab.setTestType("Updated Blood Test");

        when(objectMapper.readValue(labData, LabDTO.class)).thenReturn(updatedLab);
        when(labService.updateLabWithFile(any(LabDTO.class), eq(null))).thenReturn(updatedLab);

        // Act
        ResponseEntity<LabDTO> response = labController.updateLabWithFile(labId, labData, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        LabDTO lab = response.getBody();
        assertNotNull(lab);
        assertEquals(labId, lab.getLabId());
        verify(labService).updateLabWithFile(any(LabDTO.class), eq(null));
    }

    @Test
    void testUpdateLabWithFile_InvalidJson() throws Exception {
        // Arrange
        Long labId = 1L;
        String invalidLabData = "invalid json";
        MultipartFile mockFile = mock(MultipartFile.class);

        when(objectMapper.readValue(invalidLabData, LabDTO.class)).thenThrow(new RuntimeException("Invalid JSON"));

        // Act
        ResponseEntity<LabDTO> response = labController.updateLabWithFile(labId, invalidLabData, mockFile);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateLabWithFile_NullLabData() throws Exception {
        // Arrange
        Long labId = 1L;
        String labData = "{}";
        MultipartFile mockFile = mock(MultipartFile.class);

        when(objectMapper.readValue(labData, LabDTO.class)).thenReturn(null);

        // Act
        ResponseEntity<LabDTO> response = labController.updateLabWithFile(labId, labData, mockFile);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdateLabWithFile_InvalidOperation() throws Exception {
        // Arrange
        Long labId = 1L;
        String labData = "{\"labId\":1,\"testType\":\"Updated Blood Test\"}";
        MultipartFile mockFile = mock(MultipartFile.class);
        LabDTO updatedLab = new LabDTO();
        updatedLab.setLabId(labId);

        when(objectMapper.readValue(labData, LabDTO.class)).thenReturn(updatedLab);
        when(labService.updateLabWithFile(any(LabDTO.class), eq(mockFile)))
                .thenThrow(new InvalidOperationException("Invalid file"));

        // Act
        ResponseEntity<LabDTO> response = labController.updateLabWithFile(labId, labData, mockFile);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateLabWithFile_ResourceNotFound() throws Exception {
        // Arrange
        Long labId = 1L;
        String labData = "{\"labId\":1,\"testType\":\"Updated Blood Test\"}";
        MultipartFile mockFile = mock(MultipartFile.class);
        LabDTO updatedLab = new LabDTO();
        updatedLab.setLabId(labId);

        when(objectMapper.readValue(labData, LabDTO.class)).thenReturn(updatedLab);
        when(labService.updateLabWithFile(any(LabDTO.class), eq(mockFile)))
                .thenThrow(new ResourceNotFoundException("Lab", "id", labId));

        // Act
        ResponseEntity<LabDTO> response = labController.updateLabWithFile(labId, labData, mockFile);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testServeFile_Success() throws IOException {
        // Arrange
        String filename = "test-file.pdf";
        String expectedFilePath = "labs/" + filename;

        // Create a temporary file that actually exists
        Path tempFile = Files.createTempFile("test-file", ".pdf");
        Files.write(tempFile, "test content".getBytes());
        String fullPath = tempFile.toString();

        when(fileService.getFullPath(expectedFilePath)).thenReturn(fullPath);

        // Act
        ResponseEntity<Resource> response = labController.serveFile(filename);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(fileService).getFullPath(expectedFilePath);

        // Cleanup
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testServeFile_FileNotFound() {
        // Arrange
        String filename = "nonexistent-file.pdf";
        String expectedFilePath = "labs/" + filename;
        String fullPath = "/uploads/labs/nonexistent-file.pdf";

        when(fileService.getFullPath(expectedFilePath)).thenReturn(fullPath);

        // Act
        ResponseEntity<Resource> response = labController.serveFile(filename);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(fileService).getFullPath(expectedFilePath);
    }

    @Test
    void testServeFile_InternalError() {
        // Arrange
        String filename = "test-file.pdf";
        String expectedFilePath = "labs/" + filename;

        when(fileService.getFullPath(expectedFilePath)).thenThrow(new RuntimeException("IO Error"));

        // Act
        ResponseEntity<Resource> response = labController.serveFile(filename);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(fileService).getFullPath(expectedFilePath);
    }

    @Test
    void testAssignLab_NotFound() {
        // Arrange
        when(labService.assignLab(1L, 1L, testDate, null, null, null))
                .thenThrow(new ResourceNotFoundException("Patient", "id", 1L));

        // Act
        LabDTO request = new LabDTO();
        request.setPatientId(1L);
        request.setLabTechnicianId(1L);
        request.setRequestDate(testDate);
        ResponseEntity<LabDTO> response = labController.assignLab(request);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateLab_NotFound() {
        // Arrange
        when(labService.updateLab(any(LabDTO.class)))
                .thenThrow(new ResourceNotFoundException("Lab", "id", 1L));

        // Act
        ResponseEntity<?> response = labController.updateLab(1L, testLab1);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}