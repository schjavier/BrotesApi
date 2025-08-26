pipeline{
    agent {
        label 'brotesBuilder'

        }

    environment{
        CODEBASE_DIR = "/home/javier/apps/brotes"
        DOCKER_COMPOSE_STAGING_DIR = "/home/javier/apps/brotes-staging/"
        DOCKER_COMPOSE_STAGING_FILE = "/home/javier/apps/brotes-staging/docker-compose-staging.yml"
        DOCKER_COMPOSE_PROD_DIR = "/home/javier/apps/brotes-production/"
        DOCKER_COMPOSE_PROD_FILE = "/home/javier/apps/brotes-production/docker-compose-prod.yml"
        STAGING_URL = "https://staging-api-brotes.lotorojo.com.ar"
        PROD_URL = "https://api-brotes.lotorojo.com.ar"
    }
    stages {

        stage('Checkout'){
            steps {
                git branch: 'main',
                url: 'https://github.com/schjavier/BrotesApi.git',
                poll: false
            }
        }

        stage('Build & Test'){
            steps {
                script{
                    withCredentials([string(credentialsId:'BROTES_SECRET_KEY', variable:'BROTES_SECRET_KEY')]){
                        sh 'mvn clean package -DskipTests=false'
                    }
                }

            }
            post {
                success{
                    archiveArtifacts artifacts: 'target/*.jar', onlyIfSuccessful: true
                }
            }
        }

        stage('Copy Build Context to VPS'){
            steps{
                script{
                    sshagent(credentials: ['vps-ssh-key']){
                        sh "scp -r * .mvn target ${env.CODEBASE_DIR}"
                    }
                }
            }

        }

        stage('Deploy to Staging'){
            steps{
                script{
                    echo "Desplegando en Staging (${STAGING_URL})..."
                    dir("${DOCKER_COMPOSE_STAGING_DIR}"){
                        sshagent(credentials: ['vps-ssh-key']){
                            sh """
                                docker compose -f ${DOCKER_COMPOSE_STAGING_FILE} down --remove-orphans
                                docker compose -f ${DOCKER_COMPOSE_STAGING_FILE} up -d --build
                             """
                        }
                    }
                }
            }
        }

        stage('Approve Production'){
            steps {
                timeout(time:1, unit: 'HOURS'){
                    input(
                        message: "Staging OK. Desplegamos en Producción? (${PROD_URL})",
                        ok: "Deploy to Prod"
                    )
                }
            }
        }
        stage('Deploy to Production') {
            steps {
                script {
                    echo "Desplegando en Producción (${PROD_URL})"

                    dir("${DOCKER_COMPOSE_PROD_DIR}"){
                        sshagent(credentials: ['vps-ssh-key']){
                            sh """
                                docker compose -f ${DOCKER_COMPOSE_PROD_FILE} down --remove-orphans
                                docker compose -f ${DOCKER_COMPOSE_PROD_FILE} up -d --build
                               """
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            emailext (
                subject: "[Jenkins] ${currentBuild.currentResult}: Job ${env.JOB_NAME}",
                body: """
                    Estado: ${currentBuild.currentResult}
                    Job: ${env.JOB_NAME}
                    Build: ${env.BUILD_NUMBER}
                    URL Staging: ${env.STAGING_URL}
                    URL Producción: ${env.PROD_URL}
                """,
                to: 'schjavier@gmail.com',
                recipientProviders: [[$class: 'DevelopersRecipientProvider']]
            )
        }
    }
}