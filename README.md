# API Pruebas

Proyecto desarrollado para la asignatura **Automatización de Pruebas**.

# Introducción

Este proyecto implementa una API Java con Maven y Spring Boot para demostrar un flujo profesional de automatización de pruebas, integración continua y despliegue.

Primero se creó el proyecto base con estructura organizada en controladores y servicios. Luego se implementaron pruebas unitarias con JUnit para validar la lógica de negocio y pruebas de integración con Spring Boot Test para validar los endpoints.

El código fue versionado en GitHub usando Trunk-Based Development simplificado, trabajando con ramas `feature/*` y Pull Requests hacia `main`.

Posteriormente se configuró Jenkins en Docker para ejecutar un pipeline de integración continua definido como código mediante un `Jenkinsfile`. El pipeline realiza checkout del repositorio, prepara Maven Wrapper, verifica el entorno, compila el proyecto, ejecuta pruebas unitarias, pruebas de integración y genera evidencia.


Finalmente se implementó un despliegue en ambiente de pruebas usando Docker y Docker Compose. La aplicación se empaquetó como imagen Docker versionada y se validó mediante los endpoints `/salud` y `/api/version`. También se incorporó una estrategia de rollback manual asistido, permitiendo volver a levantar una versión estable anterior de la aplicación.

---

## Repositorio del proyecto

El código fuente del proyecto se encuentra disponible en el siguiente repositorio público de GitHub:

https://github.com/NannoFloresCif/api-pruebas


---

# Actividad 1: Proyecto Maven, GitHub y pruebas base

## Objetivo de la actividad

Crear un proyecto Java Maven con Spring Boot, configurar dependencias de pruebas, definir estructura base, implementar endpoints simples, ejecutar pruebas automatizadas y subir el proyecto a GitHub con flujo de ramas definido.

## Estrategia de ramas utilizada

Se utilizó **Trunk-Based Development simplificado**.

## Ramas utilizadas

| Rama | Propósito |
|---|---|
| `main` | Rama principal estable |
| `feature/*` | Ramas cortas para cambios específicos |

## Flujo aplicado

1. Crear rama desde `main`.
2. Implementar cambios.
3. Ejecutar pruebas localmente.
4. Crear commit.
5. Subir rama a GitHub.
6. Crear Pull Request.
7. Fusionar a `main` solo si el cambio está validado.

## Justificación de Trunk-Based Development

Se seleccionó Trunk-Based Development simplificado porque el proyecto es académico, individual y orientado a integración continua. GitFlow completo es más útil en equipos grandes con múltiples ambientes y ciclos de release más largos. Para este caso, ramas cortas `feature/*` permiten mantener el flujo simple, ordenado y fácil de defender.

---

## Configuración Maven

El archivo `pom.xml` define las dependencias principales del proyecto:

- `spring-boot-starter-web`: permite crear endpoints REST.
- `spring-boot-starter-actuator`: permite monitoreo básico de la aplicación.
- `spring-boot-starter-test`: incluye JUnit 5 y herramientas de prueba de Spring Boot.

El proyecto compila apuntando a Java 21:

```xml
<properties>
    <java.version>21</java.version>
    <maven.compiler.release>21</maven.compiler.release>
</properties>
```

---

## Estrategia de pruebas implementada

Se implementaron dos tipos de pruebas:

| Tipo de prueba | Herramienta | Objetivo |
|---|---|---|
| Pruebas unitarias | JUnit 5 | Validar lógica aislada del servicio |
| Pruebas de integración | Spring Boot Test + MockMvc | Validar integración entre controladores, servicios y contexto Spring |

---

## Pruebas unitarias

La prueba unitaria principal valida la clase:

```text
ServicioCalculadora.java
```

Archivo de prueba:

```text
src/test/java/cl/hernan/apipruebas/servicio/ServicioCalculadoraTest.java
```

Casos validados:

- Suma de números positivos.
- Suma de números negativos.
- Suma de número positivo con número negativo.

Ejemplo:

```java
@Test
void deberiaSumarDosNumerosEnterosPositivos() {
    ServicioCalculadora servicioCalculadora = new ServicioCalculadora();

    int resultado = servicioCalculadora.sumar(2, 3);

    assertEquals(5, resultado);
}
```

---

## Pruebas de integración

Se crearon pruebas de integración para validar endpoints reales mediante `MockMvc`.

Archivos principales:

```text
ControladorCalculadoraIntegracionTest.java
ControladorSaludIntegracionTest.java
ControladorVersionIntegracionTest.java
```

Estas pruebas validan que la API responda correctamente sin necesidad de iniciar manualmente el servidor.

---

## Por qué JUnit es suficiente y Selenium no es necesario

JUnit es suficiente para esta etapa porque el proyecto corresponde a una API REST sin interfaz gráfica.

Selenium se usa principalmente para automatizar pruebas sobre navegadores e interfaces visuales. Como este proyecto no tiene frontend, Selenium agregaría complejidad innecesaria sin aportar valor directo.

---

## Cómo ejecutar pruebas localmente

Desde la raíz del proyecto, donde se encuentra `pom.xml`, ejecutar:

```powershell
mvn clean test
```

Para compilar, probar y empaquetar:

```powershell
mvn clean package
```

Resultado esperado:

```text
BUILD SUCCESS
```

En la ejecución validada, Maven ejecutó correctamente 7 pruebas:

```text
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
```

---

## Cómo ejecutar la API localmente

```powershell
mvn spring-boot:run
```

Luego abrir en navegador:

```text
http://localhost:8080/salud
```

Resultado esperado:

```text
API funcionando correctamente
```

También se puede probar:

```text
http://localhost:8080/api/calculadora/sumar?a=2&b=3
```

Resultado esperado:

```text
5
```

Y:

```text
http://localhost:8080/api/version
```

Resultado esperado:

```text
v1.0.0
```

---

## Evidencias Actividad 1

Se encuentran en documento Word enviado.

---

# Actividad 2: Integración Continua con Jenkins

## Objetivo de la actividad

Diseñar e implementar un pipeline de integración continua usando Jenkins, conectado al repositorio GitHub, capaz de compilar el proyecto, ejecutar pruebas automatizadas y generar evidencia de resultados.

---

## Herramienta seleccionada

Se seleccionó **Jenkins** como herramienta principal de CI/CD.

## Justificación

Jenkins fue seleccionado porque:

- Se integra correctamente con GitHub.
- Permite definir pipelines como código mediante `Jenkinsfile`.
- Puede ejecutarse localmente usando Docker.
- Permite visualizar stages, logs, resultados de pruebas y artefactos.
- Es una herramienta ampliamente usada en entornos laborales reales.


---

## Ejecución de Jenkins con Docker

Jenkins se ejecutó localmente mediante Docker con el siguiente comando:

```powershell
docker run -d --name jenkins-api-pruebas -p 8181:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts-jdk21
```

## Explicación del comando

| Parte | Explicación |
|---|---|
| `docker run -d` | Ejecuta el contenedor en segundo plano |
| `--name jenkins-api-pruebas` | Define el nombre del contenedor |
| `-p 8181:8080` | Publica Jenkins en `http://localhost:8181` |
| `-p 50000:50000` | Habilita puerto para agentes Jenkins |
| `-v jenkins_home:/var/jenkins_home` | Mantiene datos de Jenkins en un volumen persistente |
| `jenkins/jenkins:lts-jdk21` | Usa Jenkins LTS con Java 21 |

La interfaz web queda disponible en:

```text
http://localhost:8181
```

El puerto `50000` no corresponde a la interfaz web, sino a la comunicación con agentes Jenkins.

---

## Pipeline como código

El pipeline fue definido en el archivo:

```text
Jenkinsfile
```

Este archivo está versionado en GitHub junto con el código del proyecto.

Usar pipeline como código permite:

- Mantener trazabilidad.
- Revisar cambios mediante Pull Request.
- Replicar el pipeline en otros ambientes.
- Evitar configuraciones manuales difíciles de auditar.

---

## Stages del pipeline CI

| Stage | Propósito |
|---|---|
| `Checkout del repositorio` | Descarga el código desde GitHub |
| `Preparar Maven Wrapper` | Otorga permisos de ejecución a `mvnw` |
| `Verificar entorno` | Verifica Java y Maven Wrapper |
| `Build del proyecto` | Compila el proyecto |
| `Pruebas unitarias` | Ejecuta pruebas JUnit |
| `Pruebas de integracion` | Ejecuta pruebas con Spring Boot Test |
| `Generar evidencia` | Copia reportes Surefire y los archiva |

---

## Uso de Maven Wrapper

Durante la configuración se detectó que Jenkins tenía Java, pero no Maven global instalado.

Error observado:

```text
mvn: not found
```

Por eso se usó Maven Wrapper:

```bash
./mvnw clean compile
./mvnw test
./mvnw verify
```

Esto permite que el pipeline no dependa de una instalación global de Maven en Jenkins.

---

## Consideración sobre Linux y Windows

Aunque el desarrollo se realizó en Windows, Jenkins se ejecutó dentro de un contenedor Linux. Por eso el `Jenkinsfile` utiliza comandos `sh` y no `bat`.

Ejemplo:

```groovy
sh './mvnw test'
```

---

## Cómo ejecutar el pipeline Jenkins

1. Levantar Jenkins con Docker.
2. Ingresar a:

```text
http://localhost:8181
```

3. Crear un job tipo `Pipeline`.
4. Configurar:

| Campo | Valor |
|---|---|
| Definition | Pipeline script from SCM |
| SCM | Git |
| Repository URL | URL del repositorio GitHub |
| Branch | `*/main` |
| Script Path | `Jenkinsfile` |

5. Ejecutar:

```text
Build Now
```

Resultado esperado:

```text
Finished: SUCCESS
```

---

## Evidencia generada por Jenkins

Jenkins genera:

- Console Output.
- Stage View.
- Resultados de pruebas JUnit.
- Artefactos archivados.
- Reportes Surefire.

Los reportes se generan en:

```text
target/surefire-reports
```

---

## Evidencias Actividad 2

Capturas en documento Word enviado.

---

# Actividad 3: Deployment Pipeline con Docker y rollback

## Objetivo de la actividad

Implementar un flujo de despliegue hacia un ambiente de pruebas usando Docker, validar el despliegue mediante endpoints y definir una estrategia de rollback simplificada.

---

## Estrategia de deployment implementada

Se implementó la siguiente estrategia:

```text
Build → Tests → Acceptance Tests → Docker Build → Deploy Test → Verify Deploy → Rollback
```

Esta estrategia permite validar el proyecto antes de desplegarlo y comprobar que la aplicación responde correctamente en un ambiente controlado.

---

## Archivos creados

| Archivo | Propósito |
|---|---|
| `Dockerfile` | Construye la imagen Docker de la API |
| `docker-compose.yml` | Levanta la API en ambiente de pruebas |
| `scripts/desplegar.ps1` | Despliegue local desde Windows |
| `scripts/rollback.ps1` | Rollback local desde Windows |
| `scripts/desplegar.sh` | Despliegue desde Linux/Jenkins |
| `scripts/rollback.sh` | Rollback desde Linux/Jenkins |
| `Jenkinsfile` | Pipeline extendido con etapas de deployment |

---

## Dockerfile

```dockerfile
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/api-pruebas-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

## Explicación

| Línea | Explicación |
|---|---|
| `FROM eclipse-temurin:21-jre` | Usa una imagen base con Java 21 |
| `WORKDIR /app` | Define la carpeta interna del contenedor |
| `COPY target/api-pruebas-0.0.1-SNAPSHOT.jar app.jar` | Copia el `.jar` generado por Maven |
| `EXPOSE 8080` | Documenta el puerto usado por la aplicación |
| `ENTRYPOINT` | Ejecuta la aplicación al iniciar el contenedor |

---

## docker-compose.yml

```yaml
services:
  api-pruebas:
    image: api-pruebas:${VERSION_APP:-1.0.0}
    container_name: api-pruebas-test
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=test
    restart: unless-stopped
```

## Explicación

| Configuración | Explicación |
|---|---|
| `image` | Usa una imagen versionada |
| `container_name` | Define el nombre del contenedor |
| `ports` | Expone la API en `localhost:8080` |
| `SPRING_PROFILES_ACTIVE=test` | Ejecuta la API en perfil de pruebas |
| `restart` | Reinicia el contenedor si se detiene |

---

## Comandos de despliegue local

Primero se compila y empaqueta la aplicación:

```powershell
mvn clean package
```

Luego se construye la imagen Docker:

```powershell
docker build -t api-pruebas:1.0.0 .
```

Finalmente se despliega:

```powershell
.\scripts\desplegar.ps1 1.0.0
```

---

## Resultado de validación

La validación realizada mostró:

```text
BUILD SUCCESS
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
```

También se generó correctamente el archivo:

```text
target/api-pruebas-0.0.1-SNAPSHOT.jar
```

Docker construyó la imagen:

```text
api-pruebas:1.0.0
```

Y el despliegue validó correctamente:

```text
API funcionando correctamente
v1.0.0
```

---

## Verificación del despliegue

Endpoints usados como smoke tests:

| Endpoint | Resultado esperado |
|---|---|
| `http://localhost:8080/salud` | `API funcionando correctamente` |
| `http://localhost:8080/api/version` | `v1.0.0` |

---

## Rollback simplificado

Se implementó un rollback manual asistido.

La idea es construir imágenes Docker versionadas:

```text
api-pruebas:1.0.0
api-pruebas:1.1.0
```

Si una versión nueva falla, se puede volver a levantar una versión estable anterior.

Rollback en Windows:

```powershell
.\scripts\rollback.ps1 1.0.0
```

Rollback en Linux/Jenkins:

```bash
./scripts/rollback.sh 1.0.0
```

---

## Explicación del rollback

El rollback realiza:

1. Recibe la versión estable a restaurar.
2. Define la variable `VERSION_APP`.
3. Detiene el contenedor actual.
4. Levanta nuevamente la aplicación con Docker Compose.
5. Verifica `/salud`.
6. Verifica `/api/version`.
7. Confirma que la versión restaurada responde correctamente.

---

## Consideración sobre Jenkins y Docker

Jenkins fue ejecutado dentro de un contenedor Docker. Para que Jenkins pueda construir imágenes Docker directamente se requiere configurar acceso al Docker daemon del host o instalar Docker CLI dentro del contenedor Jenkins.

Por ese motivo, el despliegue Docker fue validado localmente mediante PowerShell, dejando el `Jenkinsfile` preparado y documentado para un deployment pipeline completo.


---

## Evidencias Actividad 3

Capturas en documento Word enviado.

---

# Comandos principales utilizados

## Maven

```powershell
mvn clean test
mvn clean package
mvn spring-boot:run
```

## Git y GitHub

```powershell
git init
git branch -M main
git add .
git commit -m "mensaje"
git remote add origin https://github.com/NannoFloresCif/api-pruebas.git
git push -u origin main
git checkout -b feature/nombre-rama
git push -u origin feature/nombre-rama
git checkout main
git pull origin main
```

## Jenkins

```powershell
docker run -d --name jenkins-api-pruebas -p 8181:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts-jdk21
docker ps
docker exec jenkins-api-pruebas cat /var/jenkins_home/secrets/initialAdminPassword
```

## Docker

```powershell
docker build -t api-pruebas:1.0.0 .
docker images api-pruebas
docker compose up -d
docker compose down
docker ps
```

## Deployment y rollback

```powershell
.\scripts\desplegar.ps1 1.0.0
.\scripts\rollback.ps1 1.0.0
```


