## Estrategia de ramas

El proyecto utiliza Trunk-Based Development simplificado.

### Ramas utilizadas

- `main`: rama principal y estable del proyecto.
- `feature/*`: ramas temporales para cambios específicos.

### Flujo de trabajo

1. Crear una rama `feature/*` desde `main`.
2. Implementar el cambio.
3. Ejecutar pruebas localmente con `mvn clean test`.
4. Realizar commit con mensaje descriptivo.
5. Subir la rama a GitHub.
6. Crear Pull Request hacia `main`.
7. Fusionar solo si las pruebas pasan correctamente.

### Justificación

Se utiliza Trunk-Based evita complejidad innecesaria y permite mantener una rama principal estable, integrando cambios pequeños mediante ramas cortas.

---

## Descripción

Se agrega Jenkinsfile para automatizar el pipeline de integración continua del proyecto.

## Stages incluidos

- Checkout del repositorio.
- Verificación de entorno.
- Build del proyecto.
- Pruebas unitarias.
- Pruebas de integración.
- Generación de evidencia.

## Validaciones locales

- Se ejecutó `mvn clean verify`.
- El proyecto compila correctamente.
- Las pruebas unitarias e integración finalizan exitosamente.

## Evidencia

El pipeline queda definido como código y versionado en el repositorio.