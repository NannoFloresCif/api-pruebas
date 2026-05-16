pipeline {
    /*
     * Jenkins se ejecuta dentro de un contenedor Linux.
     * Por eso se usan comandos sh.
     *
     * Además, el contenedor Jenkins no tiene Maven instalado globalmente,
     * por lo tanto usamos Maven Wrapper: ./mvnw
     */
    agent any

    environment {
        NOMBRE_PROYECTO = 'api-pruebas'
    }

    stages {

        stage('Checkout del repositorio') {
            steps {
                checkout scm
            }
        }

        stage('Preparar Maven Wrapper') {
            steps {
                /*
                 * En Linux, el archivo mvnw necesita permiso de ejecución.
                 * chmod +x permite ejecutar el wrapper con ./mvnw
                 */
                sh 'chmod +x mvnw'
            }
        }

        stage('Verificar entorno') {
            steps {
                /*
                 * Verifica Java dentro del contenedor Jenkins.
                 * Luego verifica Maven usando el wrapper del proyecto.
                 */
                sh 'java -version'
                sh './mvnw -version'
            }
        }

        stage('Build del proyecto') {
            steps {
                /*
                 * Compila el proyecto sin ejecutar pruebas.
                 */
                sh './mvnw clean compile'
            }
        }

        stage('Pruebas unitarias') {
            steps {
                /*
                 * Ejecuta pruebas unitarias con JUnit.
                 */
                sh './mvnw test'
            }
            post {
                always {
                    /*
                     * Publica resultados de pruebas en Jenkins.
                     */
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Pruebas de integracion') {
            steps {
                /*
                 * Ejecuta la fase verify de Maven.
                 * En este proyecto valida también las pruebas de integración
                 * implementadas con Spring Boot Test.
                 */
                sh './mvnw verify'
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
                 * Crea carpeta de evidencia y copia reportes Surefire.
                 */
                sh 'mkdir -p evidencia'
                sh 'cp -r target/surefire-reports evidencia/ || true'
                sh 'ls -R evidencia || true'
            }
        }
    }

    post {
        success {
            echo 'Pipeline ejecutado correctamente. Build, pruebas unitarias e integracion finalizadas con exito.'
            archiveArtifacts artifacts: 'evidencia/**', allowEmptyArchive: true
        }

        failure {
            echo 'Pipeline fallo. Revisar logs de Jenkins para identificar la etapa con error.'
            archiveArtifacts artifacts: 'target/surefire-reports/**', allowEmptyArchive: true
        }

        always {
            echo 'Fin de ejecucion del pipeline CI.'
        }
    }
}