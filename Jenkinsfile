pipeline {
    agent any
    tools { 
        maven 'jenkins-maven' 
        jdk 'Java 11' 
    }
    stages {
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM', 
                    branches: [[name: 'main']],
                    userRemoteConfigs: [[url: 'git@github.com:ToboCodes/portfolioProject.git']]
                ])
            }
            post {
                failure {
                    notifySlack('Checkout')
                }
            }
        }
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
            post {
                failure {
                    notifySlack('Build')
                }
            }
        }
        stage('Archive') {
            steps {
                archiveArtifacts artifacts: "**/target/*.jar", fingerprint: true
            }
            post {
                failure {
                    notifySlack('Archive')
                }
            }
        }
        stage('Test') {
            steps {
                sh "mvn test"
                junit '**/target/surefire-reports/TEST-*.xml'
            }
            post {
                failure {
                    notifySlack('Test')
                }
            }
        }
        
        stage('Sonar Scanner') {
            steps {
                withSonarQubeEnv('SonarQube') { 
                    sh 'mvn sonar:sonar -Dsonar.projectKey=GS -Dsonar.sources=src/main/java/com/kibernumacademy/miapp -Dsonar.tests=src/test/java/com/kibernumacademy/miapp -Dsonar.java.binaries=.'
                }
            }
            post {
                failure {
                    notifySlack('Sonar Scanner')
                }
            }
        }
        stage('Quality Gate'){
            steps{
                timeout(time:1, unit:'HOURS'){
                    waitForQualityGate abortPipeline:true
                }
            }
            post {
                failure {
                    notifySlack('Quality Gate')
                }
            }
        }
        stage('Nexus Upload') {
            steps {
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: 'nexus:8081',
                    groupId: 'cl.awakelab.junitapp',
                    version: '0.0.1-SNAPSHOT',
                    repository: 'maven-activity8',
                    credentialsId: 'nexus',
                    artifacts: [
                        [artifactId: 'proyectoJunit',
                        classifier: '',
                        file: 'target/ControlInventario-0.0.1-SNAPSHOT.jar',
                        type: 'pom']
                    ]
                )
            }
            post {
                success {
                    notifySlack('Pipeline ejecutado correctamente!')
                }
                failure {
                    notifySlack('Nexus Upload')
                }
            }
        }
    }
}

def notifySlack(String stageName) {
    def COLOR_MAP = [
        SUCCESS: 'good',
        FAILURE: 'danger',
    ]

    echo 'Slack Notification'
    slackSend (
        channel: '#integracion-de-slack-a-jenkins',
        color: COLOR_MAP[currentBuild.currentResult],
        message: "*${stageName} - ${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More Info at: ${env.BUILD_URL}"
    )
}
