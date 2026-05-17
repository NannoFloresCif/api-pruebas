param(
    [string]$VersionApp = "1.0.0"
)

Write-Host "Desplegando api-pruebas version $VersionApp"

$env:VERSION_APP = $VersionApp

docker compose down
docker compose up -d

Write-Host "Esperando inicio de la API..."
Start-Sleep -Seconds 10

Write-Host "Verificando endpoint /salud"
curl.exe -f http://localhost:8080/salud

Write-Host "Verificando endpoint /api/version"
curl.exe -f http://localhost:8080/api/version
Write-Host ""

Write-Host "Despliegue finalizado correctamente"