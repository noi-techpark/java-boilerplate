pipeline {
    agent any

    environment {
        DOCKER_PROJECT_NAME = "project-api"
        DOCKER_IMAGE = '755952719952.dkr.ecr.eu-west-1.amazonaws.com/project-api'
        DOCKER_TAG = "test-$BUILD_NUMBER"

        SERVER_PORT = "1000"

        POSTGRES_URL = "jdbc:postgresql://test-pg-bdp.co90ybcr8iim.eu-west-1.rds.amazonaws.com:5432/project"
        POSTGRES_USERNAME = credentials('project-api-test-postgres-username')
        POSTGRES_PASSWORD = credentials('project-api-test-postgres-password')
    }

    stages {
        stage('Configure') {
            steps {
                sh """
                    rm -f .env
                    cp .env.example .env
                    echo 'COMPOSE_PROJECT_NAME=${DOCKER_PROJECT_NAME}' >> .env
                    echo 'DOCKER_IMAGE=${DOCKER_IMAGE}' >> .env
                    echo 'DOCKER_TAG=${DOCKER_TAG}' >> .env

                    echo 'SERVER_PORT=${SERVER_PORT}' >> .env

                    echo 'POSTGRES_URL=${POSTGRES_URL}' >> .env
                    echo 'POSTGRES_USERNAME=${POSTGRES_USERNAME}' >> .env
                    echo 'POSTGRES_PASSWORD=${POSTGRES_PASSWORD}' >> .env
                """
            }
        }

        stage('Test') {
            steps {
                sh '''
                    docker-compose --no-ansi build --pull --build-arg JENKINS_USER_ID=$(id -u jenkins) --build-arg JENKINS_GROUP_ID=$(id -g jenkins)
                    docker-compose --no-ansi run --rm --no-deps -u $(id -u jenkins):$(id -g jenkins) app mvn clean test
                '''
            }
        }
        stage('Build') {
            steps {
                sh '''
                    aws ecr get-login --region eu-west-1 --no-include-email | bash
                    docker-compose --no-ansi -f docker-compose.build.yml build --pull
                    docker-compose --no-ansi -f docker-compose.build.yml push
                '''
            }
        }
        stage('Deploy') {
            steps {
               sshagent(['jenkins-ssh-key']) {
                    sh """
                        ansible-galaxy install --force -r ansible/requirements.yml
                        ansible-playbook --limit=test ansible/deploy.yml --extra-vars "build_number=${BUILD_NUMBER}"
                    """
                }
            }
        }
    }
}
