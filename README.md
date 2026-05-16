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