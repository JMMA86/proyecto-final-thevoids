package org.thevoids.oncologic.controller.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thevoids.oncologic.dto.PermissionDTO;
import org.thevoids.oncologic.entity.Permission;
import org.thevoids.oncologic.mapper.PermissionMapper;
import org.thevoids.oncologic.service.PermissionService;

@Controller
@RequestMapping("/web/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private PermissionMapper permissionMapper;

    @PreAuthorize("hasAuthority('VIEW_PERMISSIONS')")
    @GetMapping
    public String listPermissions(Model model) {
        model.addAttribute("permissions", permissionService.getAllPermissions());
        return "permissions/list";
    }

    @PreAuthorize("hasAuthority('ADD_PERMISSIONS')")
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("permissionDTO", new PermissionDTO());
        return "permissions/create";
    }

    @PreAuthorize("hasAuthority('ADD_PERMISSIONS')")
    @PostMapping("/create")
    public String createPermission(@ModelAttribute PermissionDTO permissionDTO, Model model) {
        try {
            Permission permission = permissionMapper.toPermission(permissionDTO);
            permissionService.createPermission(permission);
            return "redirect:/web/permissions";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "permissions/create";
        }
    }

    @PreAuthorize("hasAuthority('EDIT_PERMISSIONS')")
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Permission> permission = permissionService.getPermission(id);
        if (permission.isPresent()) {
            PermissionDTO permissionDTO = permissionMapper.toPermissionDTO(permission.get());
            model.addAttribute("permissionDTO", permissionDTO); // Ensure the DTO is added to the model
            return "permissions/edit";
        } else {
            model.addAttribute("error", "Permission not found");
            return "redirect:/web/permissions";
        }
    }

    @PreAuthorize("hasAuthority('EDIT_PERMISSIONS')")
    @PostMapping("/{id}/edit")
    public String updatePermission(@PathVariable Long id, @ModelAttribute PermissionDTO permissionDTO, Model model) {
        try {
            Permission permission = permissionService.getPermission(id).orElse(null);
            permission.setPermissionName(permissionDTO.getPermissionName());
            permissionService.updatePermission(permission);
            return "redirect:/web/permissions";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "permissions/edit";
        }
    }

    @PreAuthorize("hasAuthority('DELETE_PERMISSIONS')")
    @PostMapping("/{id}/delete")
    public String deletePermission(@PathVariable Long id) {
        try {
            permissionService.deletePermission(id);
            return "redirect:/web/permissions";
        } catch (Exception e) {
            return "redirect:/web/permissions?error=" + e.getMessage();
        }
    }
}