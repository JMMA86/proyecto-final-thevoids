package org.thevoids.oncologic.controller.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thevoids.oncologic.dto.RoleDTO;
import org.thevoids.oncologic.dto.UserDTO;
import org.thevoids.oncologic.dto.UserWithRolesDTO;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.mapper.RoleMapper;
import org.thevoids.oncologic.mapper.UserMapper;
import org.thevoids.oncologic.service.AssignedRoles;
import org.thevoids.oncologic.service.RoleService;
import org.thevoids.oncologic.service.UserService;

@Controller
@RequestMapping("/web/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AssignedRoles assignedRolesService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @GetMapping
    public String listUsers(Model model) {
        List<UserWithRolesDTO> userDTOs = userService.getAllUsers().stream()
                .map(userMapper::toUserWithRolesDTO)
                .toList();
        model.addAttribute("users", userDTOs);
        return "users/list";
    }

    @PreAuthorize("hasAuthority('ADD_USERS')")
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        model.addAttribute("roles", roleService.getAllRoles());
        return "users/register";
    }

    @PreAuthorize("hasAuthority('ADD_USERS')")
    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserDTO userDTO, @RequestParam Long roleId, Model model) {
        try {
            User user = userMapper.toUser(userDTO);
            User createdUser = userService.createUser(user);
            assignedRolesService.assignRoleToUser(roleId, createdUser.getUserId());
            return "redirect:/web/users";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("roles", roleService.getAllRoles());
            return "users/register";
        }
    }

    @PreAuthorize("hasAuthority('MANAGE_USER_ROLES')")
    @GetMapping("/{id}/roles")
    public String manageRoles(@PathVariable Long id, Model model) {
        UserWithRolesDTO userWithRolesDTO = userMapper.toUserWithRolesDTO(userService.getUserById(id));
        List<RoleDTO> roles = roleService.getAllRoles().stream()
                .map(roleMapper::toRoleDTO)
                .filter(r -> !userWithRolesDTO.hasRole(r.getRoleId()))
                .toList();
        model.addAttribute("user", userWithRolesDTO);
        model.addAttribute("roles", roles);
        return "users/manage_roles";
    }

    @PreAuthorize("hasAuthority('MANAGE_USER_ROLES')")
    @PostMapping("/{id}/roles/assign")
    public String assignRole(@PathVariable Long id, @RequestParam Long roleId, Model model) {
        try {
            assignedRolesService.assignRoleToUser(roleId, id);
            return "redirect:/web/users/" + id + "/roles";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/web/users/" + id + "/roles";
        }
    }

    @PreAuthorize("hasAuthority('MANAGE_USER_ROLES')")
    @PostMapping("/{id}/roles/remove")
    public String removeRole(@PathVariable Long id, @RequestParam Long roleId, Model model) {
        try {
            assignedRolesService.removeRoleFromUser(roleId, id);
            return "redirect:/web/users/" + id + "/roles";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/web/users/" + id + "/roles";
        }
    }
}