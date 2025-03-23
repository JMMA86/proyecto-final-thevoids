# Nombre del archivo de salida
$outputFile = "CombinedJavaFiles.txt"

# Obtener todos los archivos .java en la carpeta actual
$javaFiles = Get-ChildItem -Filter *.java

# Verificar si hay archivos .java en la carpeta
if ($javaFiles.Count -eq 0) {
    Write-Host "No se encontraron archivos .java en la carpeta actual." -ForegroundColor Red
    exit
}

# Crear o sobrescribir el archivo de salida
Write-Host "Creando archivo combinado: $outputFile" -ForegroundColor Green
Set-Content -Path $outputFile -Value "" # Limpiar el archivo si ya existe

# Iterar sobre cada archivo .java y agregar su contenido al archivo de salida
foreach ($file in $javaFiles) {
    Write-Host "Agregando archivo: $($file.Name)" -ForegroundColor Yellow
    Add-Content -Path $outputFile -Value "// Archivo: $($file.Name)" # Agregar comentario con el nombre del archivo
    Add-Content -Path $outputFile -Value (Get-Content -Path $file.FullName -Raw) # Agregar contenido del archivo
    Add-Content -Path $outputFile -Value "`n" # Agregar una línea en blanco entre archivos
}

Write-Host "Todos los archivos .java han sido combinados en '$outputFile'." -ForegroundColor Green