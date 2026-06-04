pipeline {
    // Define parameters user will provide at runtime
    parameters {
        string(
            name: 'GIT_BRANCH',
            defaultValue: 'main',
            description: 'Enter the Git branch name to build'
        )
        string(
            name: 'APP_VERSION',
            defaultValue: '1.0.0',
            description: 'Enter application version number'
        )
        choice(
            name: 'ENVIRONMENT',
            choices: ['dev', 'staging', 'production'],
            description: 'Select deployment environment'
        )
    }
    
    // Environment variables for configuration
    environment {
        BUILD_DIR = 'build'
        ARTIFACT_NAME = "myapp-${params.APP_VERSION}.jar"
        MAVEN_OPTS = '-Xmx2048m'
        JAVA_HOME = tool name: 'JDK11', type: 'jdk'
        MAVEN_HOME = tool name: 'Maven-3', type: 'maven'
    }
    
    // Options for pipeline behavior
    options {
        timeout(time: 1, unit: 'HOURS')
        buildDiscarder(logRotator(numToKeepStr: '10'))
        ansiColor('xterm')
    }
    
    // Stages of the pipeline
    stages {
        stage('Source Code Checkout') {
            steps {
                echo "Checking out code from branch: ${params.GIT_BRANCH}"
                echo "Application version: ${params.APP_VERSION}"
                echo "Target environment: ${params.ENVIRONMENT}"
                
                // Clone the repository
                git branch: params.GIT_BRANCH,
                    url: 'https://github.com/YOUR_USERNAME/my-java-app.git',
                    credentialsId: 'github-credentials'
                
                // Display current directory contents
                sh 'ls -la'
            }
        }
        
        stage('Dependency Installation') {
            steps {
                echo "Installing Maven dependencies..."
                sh 'mvn dependency:resolve'
                echo "Dependencies installed successfully"
            }
        }
        
        stage('Compile/Build') {
            steps {
                echo "Compiling source code..."
                sh 'mvn clean compile'
                echo "Compilation completed"
            }
            post {
                success {
                    echo "Build successful!"
                }
                failure {
                    echo "Build failed. Check compilation errors."
                }
            }
        }
        
        stage('Unit Testing') {
            steps {
                echo "Running unit tests..."
                sh 'mvn test'
            }
            post {
                always {
                    // Publish test results even if tests fail
                    junit 'target/surefire-reports/*.xml'
                }
                success {
                    echo "All tests passed!"
                }
                failure {
                    echo "Some tests failed. Check test reports."
                }
            }
        }
        
        stage('Code Quality Check') {
            steps {
                echo "Running code quality checks with SonarQube..."
                // This requires SonarQube server setup
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
                echo "Code quality check completed"
            }
        }
        
        stage('Artifact Packaging') {
            steps {
                echo "Packaging application as: ${ARTIFACT_NAME}"
                sh 'mvn package'
                echo "Artifact created at: target/my-java-app-1.0-SNAPSHOT.jar"
            }
            post {
                success {
                    // Archive the artifact for later download
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                    
                    // Copy to specific location with version name
                    sh """
                        cp target/my-java-app-1.0-SNAPSHOT.jar ${BUILD_DIR}/${ARTIFACT_NAME}
                        echo "Artifact saved as: ${BUILD_DIR}/${ARTIFACT_NAME}"
                    """
                }
            }
        }
    }
    
    // Post-pipeline actions
    post {
        always {
            echo "Pipeline execution completed for version: ${params.APP_VERSION}"
            cleanWs() // Clean workspace
        }
        success {
            echo "✅ PIPELINE SUCCESSFUL! All stages passed."
            echo "📦 Artifact: ${ARTIFACT_NAME}"
            echo "🌍 Environment: ${params.ENVIRONMENT}"
            echo "🔀 Branch: ${params.GIT_BRANCH}"
        }
        failure {
            echo "❌ PIPELINE FAILED! Check console output for details."
        }
        aborted {
            echo "⚠️ Pipeline was aborted by user."
        }
    }
}