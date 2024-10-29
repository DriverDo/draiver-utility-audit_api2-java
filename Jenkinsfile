pipeline {
    agent any
    tools {
       maven "maven3"
    }
    options {
       timeout(time: 15, unit: 'MINUTES')
       buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    stages {
        stage('Download Src') {
            steps{
                checkout scm 
            }
        }
        stage('Build and Unit Test') {
                steps {
                    withMaven(maven: 'maven3') {
                        sh 'mvn clean package'
                    }
                }
                post {
                    success {
                        junit 'target/surefire-reports/**/*.xml'
                    }
                }
        }
        stage('Quality Scan') {
                steps {
                    withSonarQubeEnv('sonarqube-scanner') {
                        sh 'mvn sonar:sonar'
                    }
                }
        }
        stage('Deploy to Nexus') { 
            steps {
             sh 'mvn clean deploy -Dmaven.test.skip=true'
            }
        }
    }
}
