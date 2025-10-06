terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~>3.0"
    }
  }
}

provider "azurerm" {
  features {}
  subscription_id = var.subscription_id
}

# Resource Group
resource "azurerm_resource_group" "main" {
  name     = var.resource_group_name
  location = var.location

  tags = {
    Environment = var.environment
    Purpose     = "SonarQube"
  }
}

# Virtual Network
resource "azurerm_virtual_network" "main" {
  name                = "vnet-sonarqube"
  address_space       = ["10.0.0.0/16"]
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name

  tags = {
    Environment = var.environment
  }
}

# Subnet
resource "azurerm_subnet" "internal" {
  name                 = "internal"
  resource_group_name  = azurerm_resource_group.main.name
  virtual_network_name = azurerm_virtual_network.main.name
  address_prefixes     = ["10.0.2.0/24"]
}

# Network Security Group y reglas
resource "azurerm_network_security_group" "main" {
  name                = "nsg-sonarqube"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name

  security_rule {
    name                       = "SSH"
    priority                   = 1001
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "22"
    source_address_prefix      = var.allowed_cidr
    destination_address_prefix = "*"
  }

  security_rule {
    name                       = "SonarQube"
    priority                   = 1002
    direction                  = "Inbound"
    access                     = "Allow"
    protocol                   = "Tcp"
    source_port_range          = "*"
    destination_port_range     = "9000"
    source_address_prefix      = var.allowed_cidr
    destination_address_prefix = "*"
  }

  tags = {
    Environment = var.environment
  }
}

# IP pública
resource "azurerm_public_ip" "main" {
  name                = "pip-sonarqube"
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  allocation_method   = "Static"
  sku                 = "Standard"

  tags = {
    Environment = var.environment
  }
}

# Network Interface
resource "azurerm_network_interface" "main" {
  name                = "nic-sonarqube"
  location            = azurerm_resource_group.main.location
  resource_group_name = azurerm_resource_group.main.name

  ip_configuration {
    name                          = "internal"
    subnet_id                     = azurerm_subnet.internal.id
    private_ip_address_allocation = "Dynamic"
    public_ip_address_id          = azurerm_public_ip.main.id
  }

  tags = {
    Environment = var.environment
  }
}

# Asociar Network Security Group al Network Interface
resource "azurerm_network_interface_security_group_association" "main" {
  network_interface_id      = azurerm_network_interface.main.id
  network_security_group_id = azurerm_network_security_group.main.id
}

# Generar una clave SSH
resource "tls_private_key" "ssh" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

# Virtual Machine
resource "azurerm_linux_virtual_machine" "main" {
  name                = "vm-sonarqube"
  resource_group_name = azurerm_resource_group.main.name
  location            = azurerm_resource_group.main.location
  size                = var.vm_size
  admin_username      = var.admin_username

  disable_password_authentication = false
  admin_password                  = var.admin_password

  network_interface_ids = [
    azurerm_network_interface.main.id,
  ]

  admin_ssh_key {
    username   = var.admin_username
    public_key = tls_private_key.ssh.public_key_openssh
  }

  os_disk {
    caching              = "ReadWrite"
    storage_account_type = "Standard_LRS"
  }

  source_image_reference {
    publisher = "Canonical"
    offer     = "0001-com-ubuntu-server-jammy"
    sku       = "22_04-lts-gen2"
    version   = "latest"
  }

  custom_data = base64encode(local.cloud_init_script)

  tags = {
    Environment = var.environment
  }
}

# Script de inicialización cloud-init
locals {
  cloud_init_script = <<-EOF
    #cloud-config
    
    package_update: true
    package_upgrade: true
    
    packages:
      - apt-transport-https
      - ca-certificates
      - curl
      - gnupg
      - lsb-release
      - docker.io
      - docker-compose
    
    runcmd:
      # Configurar Docker
      - systemctl start docker
      - systemctl enable docker
      - usermod -aG docker ${var.admin_username}
      
      # Configurar límites del sistema para SonarQube
      - echo 'vm.max_map_count=524288' >> /etc/sysctl.conf
      - echo 'fs.file-max=131072' >> /etc/sysctl.conf
      - sysctl -p
      
      # Configurar límites de usuario
      - echo '${var.admin_username} soft nofile 131072' >> /etc/security/limits.conf
      - echo '${var.admin_username} hard nofile 131072' >> /etc/security/limits.conf
      - echo '${var.admin_username} soft nproc 8192' >> /etc/security/limits.conf
      - echo '${var.admin_username} hard nproc 8192' >> /etc/security/limits.conf
      
      # Crear directorio para SonarQube
      - mkdir -p /opt/sonarqube
      - cd /opt/sonarqube
      
      # Crear docker-compose.yml
      - |
        cat > docker-compose.yml << 'COMPOSE_EOF'
        version: '3.8'
        
        services:
          sonarqube:
            image: sonarqube:latest
            ports:
              - "9000:9000"
            networks:
              - sonarnet
            environment:
              - SONARQUBE_JDBC_URL=jdbc:postgresql://db:5432/sonar
              - SONARQUBE_JDBC_USERNAME=sonar
              - SONARQUBE_JDBC_PASSWORD=sonar
            volumes:
              - sonarqube_conf:/opt/sonarqube/conf
              - sonarqube_data:/opt/sonarqube/data
              - sonarqube_extensions:/opt/sonarqube/extensions
              - sonarqube_bundled-plugins:/opt/sonarqube/lib/bundled-plugins
            depends_on:
              - db
        
          db:
            image: postgres:13
            networks:
              - sonarnet
            environment:
              - POSTGRES_USER=sonar
              - POSTGRES_PASSWORD=sonar
              - POSTGRES_DB=sonar
            volumes:
              - postgresql:/var/lib/postgresql
              - postgresql_data:/var/lib/postgresql/data
        
        networks:
          sonarnet:
            driver: bridge
        
        volumes:
          sonarqube_conf:
          sonarqube_data:
          sonarqube_extensions:
          sonarqube_bundled-plugins:
          postgresql:
          postgresql_data:
        COMPOSE_EOF
      
      # Dar permisos correctos
      - chown -R ${var.admin_username}:${var.admin_username} /opt/sonarqube
      
      # Iniciar SonarQube
      - cd /opt/sonarqube
      - docker-compose up -d
      
      # Crear script de reinicio
      - |
        cat > /opt/sonarqube/restart-sonarqube.sh << 'SCRIPT_EOF'
        #!/bin/bash
        cd /opt/sonarqube
        docker-compose down
        docker-compose up -d
        SCRIPT_EOF
      
      - chmod +x /opt/sonarqube/restart-sonarqube.sh
      - chown ${var.admin_username}:${var.admin_username} /opt/sonarqube/restart-sonarqube.sh
    
    write_files:
      - path: /etc/systemd/system/sonarqube.service
        content: |
          [Unit]
          Description=SonarQube service
          Requires=docker.service
          After=docker.service
          
          [Service]
          Type=oneshot
          RemainAfterExit=yes
          WorkingDirectory=/opt/sonarqube
          ExecStart=/usr/bin/docker-compose up -d
          ExecStop=/usr/bin/docker-compose down
          
          [Install]
          WantedBy=multi-user.target
    
    final_message: |
      SonarQube installation completed!
      Access SonarQube at: http://$(curl -s ifconfig.me):9000
      Default credentials: admin/admin
  EOF
}