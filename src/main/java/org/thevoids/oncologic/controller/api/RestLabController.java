package org.thevoids.oncologic.controller.api;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.thevoids.oncologic.dto.entity.LabDTO;
import org.thevoids.oncologic.exception.InvalidOperationException;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.service.FileService;
import org.thevoids.oncologic.service.LabService;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/labs")
@Tag(name = "Laboratorios", description = "API para la gestión de exámenes de laboratorio")
public class RestLabController {
    @Autowired
    private LabService labService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Retrieves all labs.
     *
     * @return a list of all labs as DTOs.
     */
    @Operation(summary = "Obtener todos los exámenes", description = "Recupera una lista de todos los exámenes de laboratorio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de exámenes recuperada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LabDTO.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado para ver exámenes")
    })
    @PreAuthorize("hasAuthority('VIEW_LABS')")
    @GetMapping
    public ResponseEntity<List<LabDTO>> getAllLabs() {
        try {
            List<LabDTO> labs = labService.getAllLabs();
            return ResponseEntity.ok(labs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a specific lab by its ID.
     *
     * @param id the ID of the lab to retrieve.
     * @return the lab with the specified ID as a DTO.
     */
    @Operation(summary = "Obtener examen por ID", description = "Recupera un examen de laboratorio específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Examen encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LabDTO.class))),
            @ApiResponse(responseCode = "404", description = "Examen no encontrado"),
            @ApiResponse(responseCode = "403", description = "No autorizado para ver exámenes")
    })
    @PreAuthorize("hasAuthority('VIEW_LABS')")
    @GetMapping("/{id}")
    public ResponseEntity<LabDTO> getLabById(
            @Parameter(description = "ID del examen a buscar") @PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            LabDTO lab = labService.getLabById(id);
            return ResponseEntity.ok(lab);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PreAuthorize("hasAuthority('ASSIGN_LABS')")
    @PostMapping
    public ResponseEntity<LabDTO> assignLab(@RequestBody LabDTO labDTO) {
        if (labDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            Long patientId = labDTO.getPatientId();
            Long userId = labDTO.getLabTechnicianId();
            Date requestDate = labDTO.getRequestDate();
            String testType = labDTO.getTestType();
            Date completionDate = labDTO.getCompletionDate();
            String result = labDTO.getResult();
            LabDTO createdLabDTO = labService.assignLab(patientId, userId, requestDate, testType, completionDate,
                    result);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLabDTO);
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Updates a lab by its ID.
     *
     * @param id     the ID of the lab to update.
     * @param labDTO the updated lab as a DTO.
     * @return the updated lab as a DTO.
     */
    @Operation(summary = "Actualizar examen", description = "Actualiza un examen de laboratorio existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Examen actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LabDTO.class))),
            @ApiResponse(responseCode = "404", description = "Examen no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            @ApiResponse(responseCode = "403", description = "No autorizado para actualizar exámenes")
    })
    @PreAuthorize("hasAuthority('UPDATE_LABS')")
    @PutMapping("/{id}")
    public ResponseEntity<LabDTO> updateLab(
            @Parameter(description = "ID del examen a actualizar") @PathVariable Long id,
            @Parameter(description = "Datos del examen a actualizar") @RequestBody LabDTO labDTO) {
        if (labDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            labDTO.setLabId(id);
            LabDTO updatedLab = labService.updateLab(labDTO);
            return ResponseEntity.ok(updatedLab);
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Assigns a lab with file attachment.
     *
     * @param labData the lab data as JSON string.
     * @param file    the file attachment (optional).
     * @return the created lab as a DTO.
     */
    @Operation(summary = "Asignar examen con archivo", description = "Asigna un nuevo examen de laboratorio con archivo adjunto opcional")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Examen asignado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LabDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de asignación inválidos"),
            @ApiResponse(responseCode = "404", description = "Paciente o técnico no encontrado"),
            @ApiResponse(responseCode = "403", description = "No autorizado para asignar exámenes")
    })
    @PreAuthorize("hasAuthority('ASSIGN_LABS')")
    @PostMapping(value = "/with-file", consumes = { "multipart/form-data" })
    public ResponseEntity<LabDTO> assignLabWithFile(
            @Parameter(description = "Datos del examen en formato JSON") @RequestParam("labData") String labData,
            @Parameter(description = "Archivo adjunto (opcional)") @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            LabDTO labDTO = objectMapper.readValue(labData, LabDTO.class);
            if (labDTO == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            Long patientId = labDTO.getPatientId();
            Long userId = labDTO.getLabTechnicianId();
            Date requestDate = labDTO.getRequestDate();
            String testType = labDTO.getTestType();
            Date completionDate = labDTO.getCompletionDate();
            String result = labDTO.getResult();

            LabDTO createdLabDTO = labService.assignLabWithFile(patientId, userId, requestDate, testType,
                    completionDate, result, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLabDTO);
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Updates a lab with file attachment.
     *
     * @param id      the ID of the lab to update.
     * @param labData the updated lab data as JSON string.
     * @param file    the file attachment (optional).
     * @return the updated lab as a DTO.
     */
    @Operation(summary = "Actualizar examen con archivo", description = "Actualiza un examen de laboratorio con archivo adjunto opcional")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Examen actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LabDTO.class))),
            @ApiResponse(responseCode = "404", description = "Examen no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
            @ApiResponse(responseCode = "403", description = "No autorizado para actualizar exámenes")
    })
    @PreAuthorize("hasAuthority('UPDATE_LABS')")
    @PutMapping(value = "/{id}/with-file", consumes = { "multipart/form-data" })
    public ResponseEntity<LabDTO> updateLabWithFile(
            @Parameter(description = "ID del examen a actualizar") @PathVariable Long id,
            @Parameter(description = "Datos del examen en formato JSON") @RequestParam("labData") String labData,
            @Parameter(description = "Archivo adjunto (opcional)") @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            LabDTO labDTO = objectMapper.readValue(labData, LabDTO.class);
            if (labDTO == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            labDTO.setLabId(id);
            LabDTO updatedLab = labService.updateLabWithFile(labDTO, file);
            return ResponseEntity.ok(updatedLab);
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Serves uploaded files.
     *
     * @param filename the name of the file to serve.
     * @return the file as a Resource.
     */
    @Operation(summary = "Descargar archivo adjunto", description = "Descarga un archivo adjunto de un examen de laboratorio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Archivo descargado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Archivo no encontrado"),
            @ApiResponse(responseCode = "403", description = "No autorizado para descargar archivos")
    })
    @PreAuthorize("hasAuthority('VIEW_LABS')")
    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(
            @Parameter(description = "Nombre del archivo a descargar") @PathVariable String filename) {
        try {
            String filePath = "labs/" + filename;
            String fullPath = fileService.getFullPath(filePath);
            Path path = Paths.get(fullPath);
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes a lab by its ID.
     *
     * @param id the ID of the lab to delete.
     * @return a response entity with no content.
     */
    @Operation(summary = "Eliminar examen", description = "Elimina un examen de laboratorio del sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Examen eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Examen no encontrado"),
            @ApiResponse(responseCode = "403", description = "No autorizado para eliminar exámenes")
    })
    @PreAuthorize("hasAuthority('DELETE_LABS')")
    @DeleteMapping("/{id}")
    public ResponseEntity<LabDTO> deleteLab(
            @Parameter(description = "ID del examen a eliminar") @PathVariable Long id) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            labService.deleteLab(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
