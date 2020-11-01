pipeline {
    agent any
    tools {
        maven 'Maven 3'
        jdk 'Java 8'
    }
    options {
        buildDiscarder(logRotator(artifactNumToKeepStr: '1'))
    }
    stages {
        stage ('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Snapshot') {
            when {
                branch "protocol"
            }
            steps {
                sh 'mvn source:jar deploy -DskipTests'
            }
        }

    }
}