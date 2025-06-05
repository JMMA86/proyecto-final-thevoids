package org.thevoids.oncologic.dto.custom;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para cambio de contraseña de usuario")
public class ChangePasswordDTO {
    @Schema(description = "Contraseña actual", example = "oldPassword123")
    private String currentPassword;

    @Schema(description = "Nueva contraseña", example = "newPassword456")
    private String newPassword;

    public ChangePasswordDTO() {
    }

    public ChangePasswordDTO(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
