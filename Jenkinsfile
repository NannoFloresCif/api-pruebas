pipeline {
    agent any

    environment {
        NOMBRE_PROYECTO = 'api-pruebas'
        VERSION_APP = '1.0.0'
        VERSION_ROLLBACK = '1.0.0'
    }

    stages {

        stage('Checkout del repositorio') {
            steps {
                checkout scm
            }
        }

        stage('Preparar Maven Wrapper') {
            steps {
                sh 'chmod +x mvnw'
            }
        }

        stage('Verificar entorno') {
            steps {
                sh 'java -version'
                sh './mvnw -version'
            }
        }

        stage('Build del proyecto') {
            steps {
                sh './mvnw clean compile'
            }
        }

        stage('Pruebas unitarias') {
            steps {
                sh './mvnw test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Pruebas de integracion') {
            steps {
                sh './mvnw verify'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Acceptance tests') {
            steps {
                /*
                 * En esta etapa se valida que los endpoints principales estén cubiertos
                 * por pruebas de integración antes de construir la imagen Docker.
                 */
                sh './mvnw verify'
            }
        }

        stage('Construir imagen Docker') {
            steps {
                sh 'docker build -t ${NOMBRE_PROYECTO}:${VERSION_APP} .'
                sh 'docker images ${NOMBRE_PROYECTO}'
            }
        }

        stage('Desplegar en ambiente de pruebas') {
            steps {
                sh 'chmod +x scripts/desplegar.sh'
                sh './scripts/desplegar.sh ${VERSION_APP}'
            }
        }

        stage('Verificar despliegue') {
            steps {
                sh 'curl -f http://localhost:8080/salud'
                sh 'curl -f http://localhost:8080/api/version'
            }
        }

        stage('Generar evidencia') {
            steps {
                sh 'mkdir -p evidencia'
                sh 'cp -r target/surefire-reports evidencia/ || true'
                sh 'docker ps > evidencia/docker-ps.txt'
                sh 'docker images ${NOMBRE_PROYECTO} > evidencia/docker-images.txt'
                sh 'curl -s http://localhost:8080/salud > evidencia/endpoint-salud.txt'
                sh 'curl -s http://localhost:8080/api/version > evidencia/endpoint-version.txt'
                sh 'ls -R evidencia || true'
            }
        }
    }

    post {
        success {
            echo 'Deployment pipeline ejecutado correctamente.'
            archiveArtifacts artifacts: 'evidencia/**', allowEmptyArchive: true
        }

        failure {
            echo 'El pipeline fallo. Se intentara rollback a la version estable.'
            sh 'chmod +x scripts/rollback.sh || true'
            sh './scripts/rollback.sh ${VERSION_ROLLBACK} || true'
            archiveArtifacts artifacts: 'target/surefire-reports/**, evidencia/**', allowEmptyArchive: true
        }

        always {
            echo 'Fin de ejecucion del deployment pipeline.'
        }
    }
}