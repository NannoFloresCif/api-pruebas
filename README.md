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

## Integración Continua con Jenkins

El proyecto implementa un pipeline de Integración Continua utilizando Jenkins ejecutado en un contenedor Docker.

La integración continua permite validar automáticamente el código cada vez que se realizan cambios en el repositorio. Esto ayuda a detectar errores de forma temprana, asegurar que el proyecto compile correctamente y verificar que las pruebas automatizadas sigan funcionando.

## Herramienta seleccionada

Se seleccionó Jenkins como herramienta principal de CI/CD debido a que:

- Se integra correctamente con repositorios GitHub.
- Permite definir pipelines como código mediante un archivo `Jenkinsfile`.
- Puede ejecutarse localmente usando Docker.
- Permite visualizar logs, stages, resultados de pruebas y artefactos.
- Es una herramienta ampliamente utilizada en entornos laborales reales.

Aunque GitLab CI/CD también es una alternativa válida, Jenkins resulta más adecuado para este proyecto porque el repositorio está alojado en GitHub y permite demostrar de forma clara la configuración de un servidor de integración continua externo al repositorio.

## Ejecución de Jenkins con Docker

Jenkins fue ejecutado localmente mediante Docker con el siguiente comando:

```bash
docker run -d --name jenkins-api-pruebas -p 8181:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts-jdk21

```

Durante la configuración del pipeline se detectó que el contenedor Jenkins tenía Java disponible, pero no tenía Maven instalado globalmente.

El error observado fue:

```
mvn: not found
```

Para solucionar esto, se utilizó Maven Wrapper mediante el archivo:

mvnw

Por este motivo, en el Jenkinsfile se usan comandos como:

./mvnw clean compile
./mvnw test
./mvnw verify

Esto permite que el proyecto no dependa de una instalación global de Maven en Jenkins, haciendo el pipeline más reproducible y portable.

Consideración sobre comandos Linux

Aunque el proyecto se desarrolla en Windows, Jenkins se ejecuta dentro de un contenedor Linux. Por esa razón, el Jenkinsfile utiliza comandos sh y no comandos bat.

Ejemplo:

sh './mvnw test'

En una instalación Jenkins sobre Windows se podrían usar comandos bat, pero en este proyecto corresponde usar sh porque el agente Jenkins corre en Linux.