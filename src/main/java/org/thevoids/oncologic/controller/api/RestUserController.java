package org.thevoids.oncologic.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thevoids.oncologic.dto.entity.UserDTO;
import org.thevoids.oncologic.dto.entity.UserWithRolesDTO;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.exception.InvalidOperationException;
import org.thevoids.oncologic.exception.ResourceAlreadyExistsException;
import org.thevoids.oncologic.exception.ResourceNotFoundException;
import org.thevoids.oncologic.mapper.UserMapper;
import org.thevoids.oncologic.service.AssignedRoles;
import org.thevoids.oncologic.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Usuarios", description = "API para la gestión de usuarios del sistema")
public class RestUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AssignedRoles assignedRolesService;

    /**
     * Retrieves all users.
     *
     * @return a list of all users as DTOs.
     */
    @Operation(summary = "Obtener todos los usuarios", description = "Recupera una lista de todos los usuarios del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios recuperada exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver usuarios"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            List<UserDTO> userDTOs = users.stream()
                    .map(userMapper::toUserDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a specific user by its ID.
     *
     * @param userId the ID of the user to retrieve.
     * @return the user with the specified ID as a DTO.
     */
    @Operation(summary = "Obtener usuario por ID", description = "Recupera un usuario específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado para ver usuarios")
    })
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @GetMapping("/{userId}")
    public ResponseEntity<UserWithRolesDTO> getUserById(
        @Parameter(description = "ID del usuario a buscar")
        @PathVariable Long userId
    ) {
        try {
            User user = userService.getUserById(userId);
            UserWithRolesDTO userDTO = userMapper.toUserWithRolesDTO(user);
            return ResponseEntity.ok(userDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Creates a new user.
     *
     * @param userDTO the user to create as a DTO.
     * @return the created user as a DTO.
     */
    @Operation(summary = "Crear nuevo usuario", description = "Crea un nuevo usuario en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para crear usuarios")
    })
    @PreAuthorize("hasAuthority('ADD_USERS')")
    @PostMapping
    public ResponseEntity<UserDTO> createUser(
        @Parameter(description = "Datos del usuario a crear")
        @RequestBody UserDTO userDTO
    ) {
        try {
            User user = userMapper.toUser(userDTO);
            User createdUser = userService.createUser(user);
            UserDTO createdUserDTO = userMapper.toUserDTO(createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDTO);
        } catch (ResourceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Updates an existing user.
     *
     * @param userId  the ID of the user to update.
     * @param userDTO the updated user data.
     * @return the updated user as a DTO.
     */
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
        @ApiResponse(responseCode = "403", description = "No autorizado para actualizar usuarios")
    })
    @PreAuthorize("hasAuthority('EDIT_USERS')")
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(
        @Parameter(description = "ID del usuario a actualizar")
        @PathVariable Long userId,
        @Parameter(description = "Datos del usuario a actualizar")
        @RequestBody UserDTO userDTO
    ) {
        try {
            User existingUser = userService.getUserById(userId);
            existingUser.setFullName(userDTO.getFullName());
            existingUser.setIdentification(userDTO.getIdentification());
            existingUser.setBirthDate(userDTO.getBirthDate());
            existingUser.setGender(userDTO.getGender());
            existingUser.setAddress(userDTO.getAddress());
            existingUser.setEmail(userDTO.getEmail());

            userService.updateUser(existingUser);
            User updatedUser = userService.getUserById(userId);
            UserDTO updatedUserDTO = userMapper.toUserDTO(updatedUser);
            return ResponseEntity.ok(updatedUserDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Deletes a user.
     *
     * @param userId the ID of the user to delete.
     * @return a success or error response.
     */
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado para eliminar usuarios")
    })
    @PreAuthorize("hasAuthority('DELETE_USERS')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
        @Parameter(description = "ID del usuario a eliminar")
        @PathVariable Long userId
    ) {
        try {
            User user = userService.getUserById(userId);
            userService.deleteUser(user);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Adds a role to a user.
     *
     * @param userId the ID of the user.
     * @param roleId the ID of the role to add.
     * @return a success or error response.
     */
    @Operation(summary = "Asignar rol a usuario", description = "Asigna un rol a un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol asignado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserWithRolesDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario o rol no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado para asignar roles")
    })
    @PreAuthorize("hasAuthority('EDIT_USERS')")
    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<UserWithRolesDTO> assignRoleToUser(
        @Parameter(description = "ID del usuario")
        @PathVariable Long userId,
        @Parameter(description = "ID del rol")
        @PathVariable Long roleId
    ) {
        try {
            assignedRolesService.assignRoleToUser(roleId, userId);
            UserWithRolesDTO userWithRolesDTO = userMapper.toUserWithRolesDTO(userService.getUserById(userId));
            return ResponseEntity.ok(userWithRolesDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Removes a role from a user.
     *
     * @param userId the ID of the user.
     * @param roleId the ID of the role to remove.
     * @return a success or error response.
     */
    @Operation(summary = "Eliminar rol de usuario", description = "Elimina un rol de un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol eliminado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserWithRolesDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuario o rol no encontrado"),
        @ApiResponse(responseCode = "403", description = "No autorizado para eliminar roles")
    })
    @PreAuthorize("hasAuthority('EDIT_USERS')")
    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<UserWithRolesDTO> removeRoleFromUser(
        @Parameter(description = "ID del usuario")
        @PathVariable Long userId,
        @Parameter(description = "ID del rol")
        @PathVariable Long roleId
    ) {
        try {
            assignedRolesService.removeRoleFromUser(roleId, userId);
            UserWithRolesDTO userWithRolesDTO = userMapper.toUserWithRolesDTO(userService.getUserById(userId));
            return ResponseEntity.ok(userWithRolesDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}