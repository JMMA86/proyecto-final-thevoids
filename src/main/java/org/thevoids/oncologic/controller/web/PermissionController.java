package org.thevoids.oncologic.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thevoids.oncologic.dto.PermissionDTO;
import org.thevoids.oncologic.entity.Permission;
import org.thevoids.oncologic.service.PermissionService;

import java.util.Optional;

@Controller
@RequestMapping("/web/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public String listPermissions(Model model) {
        model.addAttribute("permissions", permissionService.getAllPermissions());
        return "permissions/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("permission", new PermissionDTO());
        return "permissions/create";
    }

    @PostMapping("/create")
    public String createPermission(@ModelAttribute PermissionDTO permissionDTO, Model model) {
        try {
            Permission permission = new Permission();
            permission.setPermissionName(permissionDTO.getPermissionName());
            permissionService.createPermission(permission);
            return "redirect:/web/permissions";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "permissions/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Permission> permission = permissionService.getPermission(id);
        if (permission.isPresent()) {
            PermissionDTO permissionDTO = new PermissionDTO(permission.get().getPermissionId(), permission.get().getPermissionName());
            model.addAttribute("permission", permissionDTO);
            return "permissions/edit";
        } else {
            model.addAttribute("error", "Permission not found");
            return "redirect:/web/permissions";
        }
    }

    @PostMapping("/{id}/edit")
    public String updatePermission(@PathVariable Long id, @ModelAttribute PermissionDTO permissionDTO, Model model) {
        try {
            Permission permission = new Permission();
            permission.setPermissionId(id);
            permission.setPermissionName(permissionDTO.getPermissionName());
            permissionService.updatePermission(permission);
            return "redirect:/web/permissions";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "permissions/edit";
        }
    }

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