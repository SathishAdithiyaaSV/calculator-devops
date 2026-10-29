pipeline {
    agent any

    triggers {
        githubPush()
    }

    environment {
        IMAGE_NAME = 'calculator-api'
        DOCKERHUB = credentials('dockerhub-creds')
        EMAIL_RECIPIENT = 'sathishsv2068@gmail.com'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t $DOCKERHUB_USR/$IMAGE_NAME:$BUILD_NUMBER -t $DOCKERHUB_USR/$IMAGE_NAME:latest .'
            }
        }

        stage('Push') {
            steps {
                sh '''
                    echo "$DOCKERHUB_PSW" | docker login -u "$DOCKERHUB_USR" --password-stdin
                    docker push "$DOCKERHUB_USR/$IMAGE_NAME:$BUILD_NUMBER"
                    docker push "$DOCKERHUB_USR/$IMAGE_NAME:latest"
                '''
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    DOCKERHUB_USERNAME="$DOCKERHUB_USR" IMAGE_TAG="$BUILD_NUMBER" docker compose up -d --no-build
                    for attempt in $(seq 1 12); do
                        if curl --fail --silent http://localhost:8080/actuator/health; then exit 0; fi
                        sleep 5
                    done
                    exit 1
                '''
            }
        }
    }

    post {
        success {
            emailext to: "$EMAIL_RECIPIENT", subject: "Calculator pipeline #$BUILD_NUMBER succeeded", body: "The calculator API was built, tested, published, and deployed successfully."
        }
        failure {
            emailext to: "$EMAIL_RECIPIENT", subject: "Calculator pipeline #$BUILD_NUMBER failed", body: "Review the Jenkins console log for the failed stage."
        }
        always {
            sh 'docker logout || true'
        }
    }
}
