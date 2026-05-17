param(
    [string]$VersionRollback = "1.0.0"
)

Write-Host "Ejecutando rollback hacia version $VersionRollback"

$env:VERSION_APP = $VersionRollback

docker compose down
docker compose up -d

Write-Host "Esperando inicio de la version restaurada..."
Start-Sleep -Seconds 10

Write-Host "Verificando endpoint /salud despues del rollback"
curl.exe -f http://localhost:8080/salud
Write-Host ""

Write-Host "Verificando version restaurada"
curl.exe -f http://localhost:8080/api/version
Write-Host ""

Write-Host "Rollback finalizado correctamente"