pipeline {
    /*
     * Ejecuta el pipeline en cualquier agente disponible.
     * Como levantaremos Jenkins en Docker, Jenkins usará su propio entorno de ejecución.
     */
    agent any

    /*
     * Herramientas necesarias para el pipeline.
     * En esta primera versión usaremos Maven Wrapper si existe.
     * Si falla, usaremos Maven instalado dentro del contenedor Jenkins en una etapa posterior.
     */
    environment {
        NOMBRE_PROYECTO = 'api-pruebas'
    }

    stages {

        stage('Checkout del repositorio') {
            steps {
                /*
                 * Descarga el código fuente desde GitHub.
                 * En un Pipeline creado desde SCM, Jenkins hace checkout automático.
                 */
                checkout scm
            }
        }

        stage('Verificar entorno') {
            steps {
                /*
                 * Muestra versiones disponibles dentro del agente Jenkins.
                 * Esto ayuda a diagnosticar errores de Java o Maven.
                 */
                sh 'java -version'
                sh 'mvn -version'
            }
        }

        stage('Build del proyecto') {
            steps {
                /*
                 * Compila el proyecto sin ejecutar pruebas.
                 * Esto permite separar claramente la etapa de build de la etapa de testing.
                 */
                sh 'mvn clean compile'
            }
        }

        stage('Pruebas unitarias') {
            steps {
                /*
                 * Ejecuta las pruebas unitarias.
                 * Por ahora Maven ejecutará todos los tests que coincidan con el patrón *Test.
                 */
                sh 'mvn test'
            }
            post {
                always {
                    /*
                     * Publica reportes JUnit generados por Maven Surefire.
                     * Jenkins mostrará resultados de tests en la interfaz.
                     */
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Pruebas de integración') {
            steps {
                /*
                 * Ejecuta verify, fase de Maven comúnmente usada para validar el proyecto completo.
                 * En este proyecto, las pruebas de integración están implementadas con Spring Boot Test.
                 */
                sh 'mvn verify'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Generar evidencia') {
            steps {
                /*
                 * Crea una carpeta de evidencia y copia los reportes de pruebas.
                 */
                sh 'mkdir -p evidencia'
                sh 'cp -r target/surefire-reports evidencia/ || true'
                sh 'ls -R evidencia || true'
            }
        }
    }

    post {
        success {
            echo 'Pipeline ejecutado correctamente. Build, pruebas unitarias e integración finalizadas con éxito.'
            archiveArtifacts artifacts: 'evidencia/**', allowEmptyArchive: true
        }

        failure {
            echo 'Pipeline falló. Revisar logs de Jenkins para identificar la etapa con error.'
            archiveArtifacts artifacts: 'target/surefire-reports/**', allowEmptyArchive: true
        }

        always {
            echo 'Fin de ejecución del pipeline CI.'
        }
    }
}