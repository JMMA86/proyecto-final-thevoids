package org.thevoids.oncologic.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thevoids.oncologic.dto.RoleDTO;
import org.thevoids.oncologic.dto.RoleWithPermissionsDTO;
import org.thevoids.oncologic.entity.Role;
import org.thevoids.oncologic.mapper.RoleMapper;
import org.thevoids.oncologic.service.RoleService;
import org.thevoids.oncologic.service.RolePermissionService;

import java.util.List;

@Controller
@RequestMapping("/web/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private RoleMapper roleMapper;

    /**
     * Displays a list of all roles.
     *
     * @param model the model to populate with roles.
     * @return the view name for the roles list.
     */
    @GetMapping
    public String listRoles(Model model) {
        List<RoleWithPermissionsDTO> roles = roleService.getAllRoles().stream()
                .map(roleMapper::toRoleWithPermissionsDTO)
                .toList();
        model.addAttribute("roles", roles);
        return "roles/list";
    }

    /**
     * Displays the form to create a new role.
     *
     * @param model the model to populate with a new role object.
     * @return the view name for the role creation form.
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("role", new RoleDTO());
        return "roles/create";
    }

    /**
     * Handles the creation of a new role.
     *
     * @param roleDTO the role data submitted by the user.
     * @param model   the model to populate in case of errors.
     * @return a redirect to the roles list or the creation form in case of errors.
     */
    @PostMapping("/create")
    public String createRole(@ModelAttribute RoleDTO roleDTO, Model model) {
        try {
            Role role = new Role();
            role.setRoleName(roleDTO.getRoleName());
            roleService.createRole(role);
            return "redirect:/web/roles";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "roles/create";
        }
    }

    /**
     * Displays the form to edit an existing role.
     *
     * @param id    the ID of the role to edit.
     * @param model the model to populate with the role data.
     * @return the view name for the role edit form.
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Role role = roleService.getRole(id);
            model.addAttribute("role", new RoleDTO(role.getRoleId(), role.getRoleName()));
            return "roles/edit";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/web/roles";
        }
    }

    /**
     * Handles the update of an existing role.
     *
     * @param id      the ID of the role to update.
     * @param roleDTO the updated role data.
     * @param model   the model to populate in case of errors.
     * @return a redirect to the roles list or the edit form in case of errors.
     */
    @PostMapping("/{id}/edit")
    public String updateRole(@PathVariable Long id, @ModelAttribute RoleDTO roleDTO, Model model) {
        try {
            Role role = roleService.getRole(id);
            role.setRoleName(roleDTO.getRoleName());
            roleService.updateRole(role);
            return "redirect:/web/roles";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "roles/edit";
        }
    }

    /**
     * Handles the deletion of a role.
     *
     * @param id the ID of the role to delete.
     * @return a redirect to the roles list.
     */
    @PostMapping("/{id}/delete")
    public String deleteRole(@PathVariable Long id) {
        try {
            Role role = roleService.getRole(id);
            roleService.deleteRole(role);
            return "redirect:/web/roles";
        } catch (Exception e) {
            return "redirect:/web/roles?error=" + e.getMessage();
        }
    }
}
