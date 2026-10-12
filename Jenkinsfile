pipeline {
    agent any

    options {
        timestamps()
        timeout(time: 45, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '15'))
    }

    environment {
        APP_NAME = 'java-bloggingapp'
        MAVEN_OPTS = '-Djava.awt.headless=true'
        // Credentials and registry values belong in Jenkins credential store / job parameters.
        DOCKER_REGISTRY = "${params.DOCKER_REGISTRY ?: 'docker.io'}"
        DOCKER_REPO = "${params.DOCKER_REPO ?: 'java-bloggingapp'}"
        IMAGE_TAG = "${env.BUILD_NUMBER ?: 'local'}"
        DOCKER_IMAGE = "${DOCKER_REGISTRY}/${DOCKER_REPO}:${IMAGE_TAG}"
        K8S_MANIFEST = "${params.K8S_MANIFEST ?: 'deployment-service.yml'}"
    }

    parameters {
        string(name: 'DOCKER_REGISTRY', defaultValue: 'docker.io', description: 'Container registry host')
        string(name: 'DOCKER_REPO', defaultValue: 'java-bloggingapp', description: 'Repository name under the registry')
        choice(name: 'K8S_MANIFEST', choices: ['deployment-service.yml', 'deployment-service-canary.yml'], description: 'Manifest to apply')
        booleanParam(name: 'RUN_TRIVY', defaultValue: false, description: 'Run Trivy filesystem and image scans when tools are installed on the agent')
        booleanParam(name: 'RUN_SONAR', defaultValue: false, description: 'Run SonarQube analysis when SONAR_TOKEN is configured')
        booleanParam(name: 'PUBLISH_TO_NEXUS', defaultValue: false, description: 'Deploy artifacts to Nexus when credentials are configured')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Compile and Unit Tests') {
            steps {
                sh './mvnw --batch-mode --no-transfer-progress clean verify'
            }
        }

        stage('Trivy Filesystem Scan') {
            when {
                expression { return params.RUN_TRIVY }
            }
            steps {
                sh 'trivy fs --exit-code 1 --severity HIGH,CRITICAL --skip-dirs target .'
            }
        }

        stage('Build and Integration Test') {
            steps {
                sh './mvnw --batch-mode --no-transfer-progress -DskipTests=false test'
            }
        }

        stage('SonarQube Analysis') {
            when {
                allOf {
                    expression { return params.RUN_SONAR }
                    expression { return env.SONAR_TOKEN?.trim() }
                }
            }
            steps {
                withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                    sh '''
                        ./mvnw --batch-mode --no-transfer-progress \
                          -Dsonar.projectKey=twitter-app \
                          -Dsonar.host.url="${SONAR_HOST_URL}" \
                          org.sonarsource.scanner.maven:sonar-maven-plugin:sonar
                    '''
                }
            }
        }

        stage('Nexus Publish') {
            when {
                expression { return params.PUBLISH_TO_NEXUS }
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                    sh './mvnw --batch-mode --no-transfer-progress deploy -DskipTests'
                }
            }
        }

        stage('Docker Build and Tag') {
            steps {
                sh './mvnw --batch-mode --no-transfer-progress -DskipTests package'
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Trivy Image Scan') {
            when {
                expression { return params.RUN_TRIVY }
            }
            steps {
                sh "trivy image --exit-code 1 --severity HIGH,CRITICAL ${DOCKER_IMAGE}"
            }
        }

        stage('Push Image') {
            when {
                expression { return env.DOCKER_PUSH == 'true' }
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-registry-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login "$DOCKER_REGISTRY" -u "$DOCKER_USER" --password-stdin
                        docker push "$DOCKER_IMAGE"
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            when {
                expression { return env.K8S_DEPLOY == 'true' }
            }
            steps {
                withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                    sh '''
                        kubectl apply --dry-run=server -f "$K8S_MANIFEST"
                        kubectl apply -f "$K8S_MANIFEST"
                        kubectl rollout status deployment/bloggingapp-deployment
                    '''
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, allowEmptyArchive: true
        }
        failure {
            echo 'Pipeline failed — inspect stage logs and archived Surefire reports.'
        }
    }
}
