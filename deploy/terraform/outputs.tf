output "resource_group_name" {
  value = azurerm_resource_group.main.name
}

output "public_ip_address" {
  value = azurerm_public_ip.main.ip_address
}

output "sonarqube_url" {
  value = "http://${azurerm_public_ip.main.ip_address}:9000"
}

output "ssh_connection_command" {
  value = "ssh ${var.admin_username}@${azurerm_public_ip.main.ip_address}"
}

output "ssh_private_key" {
  value     = tls_private_key.ssh.private_key_pem
  sensitive = true
}

output "vm_name" {
  value = azurerm_linux_virtual_machine.main.name
}

output "vm_size" {
  value = azurerm_linux_virtual_machine.main.size
}

output "location" {
  value = azurerm_resource_group.main.location
}