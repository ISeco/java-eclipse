pipeline {
    agent any
    tools { 
        maven 'jenkins-maven' 
        jdk 'Java 11' 
    }
    stages {
        stage('Checkout') {
            steps {
                // Requerimiento 1: Obtener el código desde GitHub
                checkout([
                    $class: 'GitSCM', 
                    branches: [[name: 'main']],
                    userRemoteConfigs: [[url: 'git@github.com:ToboCodes/portfolioProject.git']]
                ])
            }
        }
        stage('Build') {
            steps {
                // Requerimiento 2: Generar los artefactos
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Archive') {
            steps {
                // Requerimiento 3: Archivar los artefactos
                archiveArtifacts artifacts: "*/target/.jar", fingerprint: true
            }
        }
        stage('Test') {
            steps {
                // Requerimiento 4: Ejecutar los tests de Maven
                sh "mvn test"
                junit '*/target/surefire-reports/TEST-.xml'
            }
        }
        stage('Validate') {
            steps {
                // Requerimiento 5: Ejecutar el pipeline desde el repositorio
                sh "echo "
            }
        }
    }
}