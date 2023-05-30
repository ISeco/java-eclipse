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
                archiveArtifacts artifacts: "**/target/*.jar", fingerprint: true
            }
        }
        stage('Test') {
            steps {
                // Requerimiento 4: Ejecutar los tests de Maven
                sh "mvn test"
                junit '**/target/surefire-reports/TEST-*.xml'
            }
        }
        stage('Sonar Scanner') {
            steps {
                script {
                    def sonarqubeScannerHome = tool name: 'sonar', type: 'hudson.plugins.sonar.SonarRunnerInstallation'
                    withCredentials([string(credentialsId: 'sonar', variable: 'sonarLogin')]) {
                        sh "${sonarqubeScannerHome}/bin/sonar-scanner -e -Dsonar.host.url=http://SonarQube:9000 -Dsonar.login=${sonarLogin} -Dsonar.projectName=gs-gradle -Dsonar.projectVersion=${env.BUILD_NUMBER} -Dsonar.projectKey=GS -Dsonar.sources=src/main/java/com/kibernumacademy/miapp -Dsonar.tests=src/test/java/com/kibernumacademy/miapp -Dsonar.language=java -Dsonar.java.binaries=."
                    }
                }
            }
        }
        // stage('Nexus Upload') {
        //     steps {
        //         nexusArtifactUploader(
        //             nexusVersion: 'nexus3',
        //             protocol: 'http',
        //             nexusUrl: 'nexus:8081',
        //             groupId: 'cl.awakelab.junitapp',
        //             version: '0.0.1-SNAPSHOT',
        //             repository: 'maven-snapshots',
        //             credentialsId: 'nex',
        //             artifacts: [
        //                 [artifactId: 'proyectoJunit',
        //                 classifier: '',
        //                 file: 'target/proyectoJunit-0.0.1.jar',
        //                 type: 'jar'],
        //                 [artifactId: 'proyectoJunit',
        //                 classifier: '',
        //                 file: 'pom.xml',
        //                 type: 'pom']
        //             ]
        //         )
        //     }
        // }
        stage('Validate') {
            steps {
                // Requerimiento 5: Ejecutar el pipeline desde el repositorio
                sh "echo Pipeline finalizado con sonarqube"
            }
        }
    }
}