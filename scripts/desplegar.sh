#!/bin/sh

# Detiene la ejecución si ocurre un error.
set -e

VERSION_APP=${1:-1.0.0}

echo "Desplegando api-pruebas version ${VERSION_APP}"

export VERSION_APP=${VERSION_APP}

docker compose down || true
docker compose up -d

echo "Esperando inicio de la API..."
sleep 10

echo "Verificando endpoint /salud"
curl -f http://localhost:8080/salud

echo "Verificando endpoint /api/version"
curl -f http://localhost:8080/api/version

echo "Despliegue finalizado correctamente"