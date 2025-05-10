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
import org.thevoids.oncologic.dto.custom.ApiResponse;
import org.thevoids.oncologic.dto.entity.UserDTO;
import org.thevoids.oncologic.dto.entity.UserWithRolesDTO;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.mapper.UserMapper;
import org.thevoids.oncologic.service.UserService;
import org.thevoids.oncologic.service.AssignedRoles;

@RestController
@RequestMapping("/api/v1/users")
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
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            List<UserDTO> userDTOs = users.stream()
                    .map(userMapper::toUserDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ApiResponse.exito("Usuarios recuperados con éxito", userDTOs));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al recuperar los usuarios: " + e.getMessage()));
        }
    }

    /**
     * Retrieves a specific user by its ID.
     *
     * @param userId the ID of the user to retrieve.
     * @return the user with the specified ID as a DTO.
     */
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserWithRolesDTO>> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserWithRolesDTO userDTO = userMapper.toUserWithRolesDTO(user);
            return ResponseEntity.ok(ApiResponse.exito("Usuario recuperado con éxito", userDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Error al recuperar el usuario: " + e.getMessage()));
        }
    }

    /**
     * Creates a new user.
     *
     * @param userDTO the user to create as a DTO.
     * @return the created user as a DTO.
     */
    @PreAuthorize("hasAuthority('ADD_USERS')")
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody UserDTO userDTO) {
        try {
            User user = userMapper.toUser(userDTO);
            User createdUser = userService.createUser(user);
            UserDTO createdUserDTO = userMapper.toUserDTO(createdUser);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.exito("Usuario creado con éxito", createdUserDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error al crear el usuario: " + e.getMessage()));
        }
    }

    /**
     * Updates an existing user.
     *
     * @param userId the ID of the user to update.
     * @param userDTO the updated user data.
     * @return the updated user as a DTO.
     */
    @PreAuthorize("hasAuthority('EDIT_USERS')")
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Long userId, @RequestBody UserDTO userDTO) {
        try {
            User existingUser = userService.getUserById(userId);
            if (existingUser == null) {
                throw new Exception("Usuario no encontrado");
            }
            
            existingUser.setFullName(userDTO.getFullName());
            existingUser.setIdentification(userDTO.getIdentification());
            existingUser.setBirthDate(userDTO.getBirthDate());
            existingUser.setGender(userDTO.getGender());
            existingUser.setAddress(userDTO.getAddress());
            existingUser.setEmail(userDTO.getEmail());
            
            userService.updateUser(existingUser);
            User updatedUser = userService.getUserById(userId);
            UserDTO updatedUserDTO = userMapper.toUserDTO(updatedUser);
            return ResponseEntity.ok(ApiResponse.exito("Usuario actualizado con éxito", updatedUserDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error al actualizar el usuario: " + e.getMessage()));
        }
    }

    /**
     * Deletes a user.
     *
     * @param userId the ID of the user to delete.
     * @return a success or error response.
     */
    @PreAuthorize("hasAuthority('DELETE_USERS')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            userService.deleteUser(user);
            return ResponseEntity.ok(ApiResponse.exito("Usuario eliminado con éxito", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error al eliminar el usuario: " + e.getMessage()));
        }
    }
    
    /**
     * Adds a role to a user.
     *
     * @param userId the ID of the user.
     * @param roleId the ID of the role to add.
     * @return a success or error response.
     */
    @PreAuthorize("hasAuthority('EDIT_USERS')")
    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<ApiResponse<UserWithRolesDTO>> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        try {
            assignedRolesService.assignRoleToUser(roleId, userId);
            UserWithRolesDTO userWithRolesDTO = userMapper.toUserWithRolesDTO(userService.getUserById(userId));
            return ResponseEntity.ok(ApiResponse.exito("Rol asignado al usuario con éxito", userWithRolesDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error al añadir rol al usuario: " + e.getMessage()));
        }
    }

    /**
     * Removes a role from a user.
     *
     * @param userId the ID of the user.
     * @param roleId the ID of the role to remove.
     * @return a success or error response.
     */
    @PreAuthorize("hasAuthority('EDIT_USERS')")
    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<ApiResponse<UserWithRolesDTO>> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        try {
            assignedRolesService.removeRoleFromUser(roleId, userId);
            UserWithRolesDTO userWithRolesDTO = userMapper.toUserWithRolesDTO(userService.getUserById(userId));
            return ResponseEntity.ok(ApiResponse.exito("Rol eliminado del usuario con éxito", userWithRolesDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error al eliminar rol del usuario: " + e.getMessage()));
        }
    }
}