#!/bin/sh

# Detiene la ejecución si ocurre un error.
set -e

VERSION_ROLLBACK=${1:-1.0.0}

echo "Ejecutando rollback hacia version ${VERSION_ROLLBACK}"

export VERSION_APP=${VERSION_ROLLBACK}

docker compose down || true
docker compose up -d

echo "Esperando inicio de la version restaurada..."
sleep 10

echo "Verificando endpoint /salud despues del rollback"
curl -f http://localhost:8080/salud

echo "Verificando version restaurada"
curl -f http://localhost:8080/api/version

echo "Rollback finalizado correctamente"