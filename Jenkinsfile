pipeline {
    // 1. Mandatory section added: tells Jenkins to run on any available worker node
    agent any

    // Parameters that the user will fill when running the job
    parameters {
        string(
            name: 'GIT_BRANCH',
            defaultValue: 'main',
            description: 'Which Git branch to build?'
        )
        string(
            name: 'APP_VERSION',
            defaultValue: '1.0.0',
            description: 'What is the application version?'
        )
    }
        
    // Environment variables - store configuration values
    environment {
        BUILD_DIRECTORY = 'build'
        ARTIFACT_NAME = "myapp-${params.APP_VERSION}.jar"
    }
        
    stages {
        // Stage 1: Get code from Git
        stage('Checkout') {
            steps {
                echo "Checking out branch: ${params.GIT_BRANCH}"
                git branch: params.GIT_BRANCH, 
                    url: 'https://github.com/mdnaseeransari/practice.git'
            }
        }
                
        // Stage 2: Compile the code
        stage('Build') {
            steps {
                echo "Building application version: ${params.APP_VERSION}"
                // For Windows Jenkins setup, use 'bat'. For Linux setup, change back to 'sh'
                bat 'mvn clean compile'
            }
        }
                
        // Stage 3: Run unit tests
        stage('Unit Testing') {
            steps {
                echo "Running unit tests"
                bat 'mvn test'
            }
            post {
                always {
                    // Publish test results even if tests fail
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
                
        // Stage 4: Code quality check
        stage('Code Quality Check') {
            steps {
                echo "Running code quality checks mock step"
                echo "Simulating quality scanning..."
            }
        }
                
        // Stage 5: Package the application
        stage('Artifact Packaging') {
            steps {
                echo "Packaging as: ${ARTIFACT_NAME}"
                bat 'mvn package'
                
                // Save the built JAR file so it shows up in Jenkins UI
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }
    }
        
    // What to show after pipeline finishes
    post {
        success {
            echo "Pipeline completed successfully!"
            echo "Artifact name: ${ARTIFACT_NAME}"
        }
        failure {
            echo "Pipeline failed!"
        }
    }
}