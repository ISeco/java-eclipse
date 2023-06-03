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
        }
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Archive') {
            steps {
                archiveArtifacts artifacts: "**/target/*.jar", fingerprint: true
            }
        }
        stage('Test') {
            steps {
                sh "mvn test"
                junit '**/target/surefire-reports/TEST-*.xml'
            }
        }
        // stage('Sonar Scanner') {
        //     steps {
        //         script {
        //             def sonarqubeScannerHome = tool name: 'sonar', type: 'hudson.plugins.sonar.SonarRunnerInstallation'
        //             withCredentials([string(credentialsId: 'sonar', variable: 'sonarLogin')]) {
        //                 sh "${sonarqubeScannerHome}/bin/sonar-scanner -e -Dsonar.host.url=http://SonarQube:9000 -Dsonar.login=${sonarLogin} -Dsonar.projectName=gs-gradle -Dsonar.projectVersion=${env.BUILD_NUMBER} -Dsonar.projectKey=GS -Dsonar.sources=src/main/java/com/kibernumacademy/miapp -Dsonar.tests=src/test/java/com/kibernumacademy/miapp -Dsonar.language=java -Dsonar.java.binaries=."
        //             }
        //         }
        //     }
        // }
        // stage('Quality Gate'){
        //     steps{
        //         timeout(time:1, unit:'HOURS'){
        //             waitForQualityGate abortPipeline:true
        //         }
        //     }
        // }
        stage('Sonar Scanner') {
            steps {
                withSonarQubeEnv('SonarQube') { 
                    sh 'mvn sonar:sonar -Dsonar.projectKey=GS -Dsonar.sources=src/main/java/com/kibernumacademy/miapp -Dsonar.tests=src/test/java/com/kibernumacademy/miapp -Dsonar.java.binaries=.'
                }
            }
        }
        stage('Quality Gate'){
            steps{
                timeout(time:1, unit:'HOURS'){
                    waitForQualityGate abortPipeline:true
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
        }
        stage('Slack Notification') {
            def COLOR_MAP = [
                ‘SUCCESS’: ‘good’,
                ‘FAILURE’:’danger’,
            ]
            post {
                always {
                    echo ‘Slack Notification’
                    slackSend channer: ‘#time-tracker-ci’,
                    color: COLOR_MAP[currentBuild.currentResult],
                    message: “*${currentBuild.currentResult}: Job ${env.JOB_
                    NAME} build ${env.BUILD_NUMBER}\n More Info at: ${env.BUILD_
                    URL}”
                }
            }
        }
        stage('Validate') {
            steps {
                sh "echo Pipeline finalizado con nexus"
            }
        }
    }
}