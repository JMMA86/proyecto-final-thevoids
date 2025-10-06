variable "subscription_id" {
  description = "ID de la suscripción de Azure"
  type        = string
}

variable "resource_group_name" {
  description = "Nombre del grupo de recursos"
  type        = string
  default     = "rg-sonarqube-chile"
}

variable "location" {
  description = "Región de Azure donde desplegar los recursos"
  type        = string
  default     = "Chile Central"
}

variable "vm_size" {
  description = "Tamaño de la VM (costos mínimos)"
  type        = string
  default     = "Standard_B1s"
}

variable "admin_username" {
  description = "Usuario administrador de la VM"
  type        = string
  default     = "azureuser"
}

variable "admin_password" {
  description = "Contraseña del usuario administrador"
  type        = string
  sensitive   = true
  default     = "SonarQube2024!"
}

variable "allowed_cidr" {
  description = "CIDR permitido para acceso SSH y SonarQube"
  type        = string
  default     = "0.0.0.0/0"
}

variable "environment" {
  description = "Etiqueta de entorno"
  type        = string
  default     = "dev"
}