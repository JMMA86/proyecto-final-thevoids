package org.thevoids.oncologic.controller.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thevoids.oncologic.dto.ErrorResponse;
import org.thevoids.oncologic.dto.UserResponseDTO;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.service.AssignedRoles;
import org.thevoids.oncologic.service.RoleService;
import org.thevoids.oncologic.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AssignedRoles assignedRolesService;
    @Autowired
    private RoleService roleService;

    /**
     * Retrieves a list of all users in the system.
     *
     * @return A {@link ResponseEntity} containing a list of {@link UserResponseDTO} objects.
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        List<UserResponseDTO> userResponseDTOList = userList.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponseDTOList);
    }

    /**
     * Registers a new user in the system.
     *
     * @param user The {@link User} object containing the user's details.
     * @return A {@link ResponseEntity} containing the created {@link UserResponseDTO} object or an error response.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok().body(convertToResponseDTO(createdUser));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ErrorResponse("Bad Request", e.getMessage()));
        }
    }

        /**
     * Assigns a role to a user.
     *
     * @param userId     The ID of the user to whom the role will be assigned.
     * @param roleId The ID of the role to be assigned.
     * @return A {@link ResponseEntity} containing the updated {@link UserResponseDTO} object or an error response.
     */
    @PostMapping("/{id}/roles/{roleId}")
    public ResponseEntity<?> assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        try {
            Role role = roleService.getRole(roleId);
            if (role == null) {
                return ResponseEntity.status(404).body(new ErrorResponse("Not Found", "Role not found"));
            }
            assignedRolesService.assignRoleToUser(roleId, userId);
            User user = userService.getUserById(userId);
            return ResponseEntity.ok().body(convertToResponseDTO(user));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ErrorResponse("Bad Request", e.getMessage()));
        }
    }

    /**
     * Removes a role from a user.
     *
     * @param userId     The ID of the user from whom the role will be removed.
     * @param roleId The ID of the role to be removed.
     * @return A {@link ResponseEntity} containing the updated {@link UserResponseDTO} object or an error response.
     */
    @DeleteMapping("/{id}/roles/{roleId}")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        try {
            assignedRolesService.removeRoleFromUser(roleId, userId);
            User user = userService.getUserById(userId);
            return ResponseEntity.ok().body(convertToResponseDTO(user));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ErrorResponse("Bad Request", e.getMessage()));
        }
    }

    /**
     * Converts a {@link User} entity to a {@link UserResponseDTO} object.
     *
     * @param user The {@link User} entity to be converted.
     * @return A {@link UserResponseDTO} object containing the user's details.
     */
    public UserResponseDTO convertToResponseDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setIdentification(user.getIdentification());
        userResponseDTO.setFullName(user.getFullName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setRoleName(user.getRole().getRoleName());
        return userResponseDTO;
    }
}