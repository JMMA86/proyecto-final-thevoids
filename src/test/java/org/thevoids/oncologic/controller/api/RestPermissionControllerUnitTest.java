package org.thevoids.oncologic.controller.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thevoids.oncologic.dto.entity.PermissionDTO;
import org.thevoids.oncologic.entity.Permission;
import org.thevoids.oncologic.mapper.PermissionMapper;
import org.thevoids.oncologic.service.PermissionService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestPermissionControllerUnitTest {

    @Mock
    private PermissionService permissionService;

    @Mock
    private PermissionMapper permissionMapper;

    @InjectMocks
    private RestPermissionController restPermissionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(restPermissionController).build();
    }

    @Test
    void getAllPermissions_ReturnsPermissionList() throws Exception {
        // Arrange
        Permission permission1 = new Permission();
        permission1.setPermissionId(1L);
        permission1.setPermissionName("VIEW_USERS");

        Permission permission2 = new Permission();
        permission2.setPermissionId(2L);
        permission2.setPermissionName("EDIT_USERS");

        List<Permission> permissions = Arrays.asList(permission1, permission2);

        PermissionDTO permissionDTO1 = new PermissionDTO();
        permissionDTO1.setPermissionId(1L);
        permissionDTO1.setPermissionName("VIEW_USERS");

        PermissionDTO permissionDTO2 = new PermissionDTO();
        permissionDTO2.setPermissionId(2L);
        permissionDTO2.setPermissionName("EDIT_USERS");

        when(permissionService.getAllPermissions()).thenReturn(permissions);
        when(permissionMapper.toPermissionDTO(permission1)).thenReturn(permissionDTO1);
        when(permissionMapper.toPermissionDTO(permission2)).thenReturn(permissionDTO2);

        // Act
        var result = mockMvc.perform(get("/api/v1/permissions")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.exito").value(true))
              .andExpect(jsonPath("$.mensaje").value("Permisos recuperados con éxito"))
              .andExpect(jsonPath("$.datos").isArray())
              .andExpect(jsonPath("$.datos[0].permissionId").value(1))
              .andExpect(jsonPath("$.datos[0].permissionName").value("VIEW_USERS"))
              .andExpect(jsonPath("$.datos[1].permissionId").value(2))
              .andExpect(jsonPath("$.datos[1].permissionName").value("EDIT_USERS"));
        
        verify(permissionService, times(1)).getAllPermissions();
    }

    @Test
    void getAllPermissions_ReturnsError() throws Exception {
        // Arrange
        when(permissionService.getAllPermissions()).thenThrow(new RuntimeException("Error al recuperar permisos"));

        // Act
        var result = mockMvc.perform(get("/api/v1/permissions")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isInternalServerError())
              .andExpect(jsonPath("$.exito").value(false))
              .andExpect(jsonPath("$.mensaje").value("Error al recuperar los permisos: Error al recuperar permisos"))
              .andExpect(jsonPath("$.datos").isEmpty());
        
        verify(permissionService, times(1)).getAllPermissions();
    }

    @Test
    void getPermissionById_PermissionExists_ReturnsPermission() throws Exception {
        // Arrange
        Long permissionId = 1L;
        Permission permission = new Permission();
        permission.setPermissionId(permissionId);
        permission.setPermissionName("VIEW_USERS");

        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setPermissionId(permissionId);
        permissionDTO.setPermissionName("VIEW_USERS");

        when(permissionService.getPermission(permissionId)).thenReturn(Optional.of(permission));
        when(permissionMapper.toPermissionDTO(permission)).thenReturn(permissionDTO);

        // Act
        var result = mockMvc.perform(get("/api/v1/permissions/{permissionId}", permissionId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.exito").value(true))
              .andExpect(jsonPath("$.mensaje").value("Permiso recuperado con éxito"))
              .andExpect(jsonPath("$.datos.permissionId").value(permissionId))
              .andExpect(jsonPath("$.datos.permissionName").value("VIEW_USERS"));
        
        verify(permissionService, times(1)).getPermission(permissionId);
    }

    @Test
    void getPermissionById_PermissionNotFound_ReturnsError() throws Exception {
        // Arrange
        Long permissionId = 1L;
        when(permissionService.getPermission(permissionId)).thenReturn(Optional.empty());

        // Act
        var result = mockMvc.perform(get("/api/v1/permissions/{permissionId}", permissionId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isNotFound())
              .andExpect(jsonPath("$.exito").value(false))
              .andExpect(jsonPath("$.mensaje").value("Error al recuperar el permiso: Permiso no encontrado"))
              .andExpect(jsonPath("$.datos").isEmpty());
        
        verify(permissionService, times(1)).getPermission(permissionId);
    }

    @Test
    void createPermission_SuccessfulCreation_ReturnsPermission() throws Exception {
        // Arrange
        Permission permission = new Permission();
        permission.setPermissionName("VIEW_USERS");

        Permission createdPermission = new Permission();
        createdPermission.setPermissionId(1L);
        createdPermission.setPermissionName("VIEW_USERS");

        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setPermissionName("VIEW_USERS");

        PermissionDTO createdPermissionDTO = new PermissionDTO();
        createdPermissionDTO.setPermissionId(1L);
        createdPermissionDTO.setPermissionName("VIEW_USERS");

        when(permissionMapper.toPermission(any(PermissionDTO.class))).thenReturn(permission);
        when(permissionService.createPermission(any(Permission.class))).thenReturn(createdPermission);
        when(permissionMapper.toPermissionDTO(createdPermission)).thenReturn(createdPermissionDTO);

        // Act
        var result = mockMvc.perform(post("/api/v1/permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"permissionName\":\"VIEW_USERS\"}"));

        // Assert
        result.andExpect(status().isCreated())
              .andExpect(jsonPath("$.exito").value(true))
              .andExpect(jsonPath("$.mensaje").value("Permiso creado con éxito"))
              .andExpect(jsonPath("$.datos.permissionId").value(1))
              .andExpect(jsonPath("$.datos.permissionName").value("VIEW_USERS"));
        
        verify(permissionService, times(1)).createPermission(any(Permission.class));
    }

    @Test
    void createPermission_FailedCreation_ReturnsError() throws Exception {
        // Arrange
        Permission permission = new Permission();
        permission.setPermissionName("VIEW_USERS");

        when(permissionMapper.toPermission(any(PermissionDTO.class))).thenReturn(permission);
        when(permissionService.createPermission(any(Permission.class)))
            .thenThrow(new RuntimeException("Nombre de permiso duplicado"));

        // Act
        var result = mockMvc.perform(post("/api/v1/permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"permissionName\":\"VIEW_USERS\"}"));

        // Assert
        result.andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.exito").value(false))
              .andExpect(jsonPath("$.mensaje").value("Error al crear el permiso: Nombre de permiso duplicado"))
              .andExpect(jsonPath("$.datos").isEmpty());
        
        verify(permissionService, times(1)).createPermission(any(Permission.class));
    }

    @Test
    void updatePermission_PermissionExists_ReturnsUpdatedPermission() throws Exception {
        // Arrange
        Long permissionId = 1L;
        
        Permission existingPermission = new Permission();
        existingPermission.setPermissionId(permissionId);
        existingPermission.setPermissionName("VIEW_USERS");
        
        Permission updatedPermission = new Permission();
        updatedPermission.setPermissionId(permissionId);
        updatedPermission.setPermissionName("VIEW_USERS_UPDATED");
        
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setPermissionName("VIEW_USERS_UPDATED");
        
        PermissionDTO updatedPermissionDTO = new PermissionDTO();
        updatedPermissionDTO.setPermissionId(permissionId);
        updatedPermissionDTO.setPermissionName("VIEW_USERS_UPDATED");
        
        when(permissionService.getPermission(permissionId)).thenReturn(Optional.of(existingPermission));
        when(permissionService.updatePermission(any(Permission.class))).thenReturn(updatedPermission);
        when(permissionMapper.toPermissionDTO(updatedPermission)).thenReturn(updatedPermissionDTO);
        
        // Act
        var result = mockMvc.perform(put("/api/v1/permissions/{permissionId}", permissionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"permissionName\":\"VIEW_USERS_UPDATED\"}"));
                
        // Assert
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.exito").value(true))
              .andExpect(jsonPath("$.mensaje").value("Permiso actualizado con éxito"))
              .andExpect(jsonPath("$.datos.permissionId").value(permissionId))
              .andExpect(jsonPath("$.datos.permissionName").value("VIEW_USERS_UPDATED"));
              
        verify(permissionService, times(1)).getPermission(permissionId);
        verify(permissionService, times(1)).updatePermission(any(Permission.class));
    }
    
    @Test
    void updatePermission_PermissionNotFound_ReturnsError() throws Exception {
        // Arrange
        Long permissionId = 1L;
        when(permissionService.getPermission(permissionId)).thenReturn(Optional.empty());
        
        // Act
        var result = mockMvc.perform(put("/api/v1/permissions/{permissionId}", permissionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"permissionName\":\"VIEW_USERS_UPDATED\"}"));
                
        // Assert
        result.andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.exito").value(false))
              .andExpect(jsonPath("$.mensaje").value("Error al actualizar el permiso: Permiso no encontrado"))
              .andExpect(jsonPath("$.datos").isEmpty());
              
        verify(permissionService, times(1)).getPermission(permissionId);
        verify(permissionService, times(0)).updatePermission(any(Permission.class));
    }
    
    @Test
    void deletePermission_SuccessfulDeletion_ReturnsSuccess() throws Exception {
        // Arrange
        Long permissionId = 1L;
        Permission deletedPermission = new Permission();
        deletedPermission.setPermissionId(permissionId);
        deletedPermission.setPermissionName("VIEW_USERS");
        
        when(permissionService.deletePermission(permissionId)).thenReturn(deletedPermission);
        
        // Act
        var result = mockMvc.perform(delete("/api/v1/permissions/{permissionId}", permissionId)
                .contentType(MediaType.APPLICATION_JSON));
                
        // Assert
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.exito").value(true))
              .andExpect(jsonPath("$.mensaje").value("Permiso eliminado con éxito"))
              .andExpect(jsonPath("$.datos").isEmpty());
              
        verify(permissionService, times(1)).deletePermission(permissionId);
    }
    
    @Test
    void deletePermission_FailedDeletion_ReturnsError() throws Exception {
        // Arrange
        Long permissionId = 1L;
        when(permissionService.deletePermission(permissionId))
            .thenThrow(new RuntimeException("No se puede eliminar un permiso en uso"));
        
        // Act
        var result = mockMvc.perform(delete("/api/v1/permissions/{permissionId}", permissionId)
                .contentType(MediaType.APPLICATION_JSON));
                
        // Assert
        result.andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.exito").value(false))
              .andExpect(jsonPath("$.mensaje").value("Error al eliminar el permiso: No se puede eliminar un permiso en uso"))
              .andExpect(jsonPath("$.datos").isEmpty());
              
        verify(permissionService, times(1)).deletePermission(permissionId);
    }
}
