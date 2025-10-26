pipeline {
    agent { label 'agent-devops' }

    tools {
        git 'Default'
    }

    stages {
        stage('Prepare Environment') {
            steps {
                sh 'chmod +x ./gradlew'
            }
        }
        stage('Check Environment') {
            steps {
                script {
                    sh './gradlew check -P"dotenv.filename"="/var/agent-jdk21/env/.env.develop"'
                }
            }
        }
        stage('Update DB') {
            steps {
                script {
                    sh './gradlew update -P"dotenv.filename"="/var/agent-jdk21/env/.env.develop"'
                }
            }
        }
        stage('Package') {
            steps {
             sh './gradlew build -Pdotenv.filename=/var/agent-jdk21/env/.env.develop'
            }
        }
        stage('JaCoCo Report') {
            steps {
                sh './gradlew jacocoTestReport'
            }
        }
        stage('JaCoCo Verification') {
            steps {
                sh './gradlew jacocoTestCoverageVerification'
            }
        }
        stage('Docker Build') {
            steps {
                sh 'docker build -t job4j_devops .'
            }
        }
    }

    post {
        always {
            script {
                def buildInfo = """
                    Build number: ${currentBuild.number}
                    Build status: ${currentBuild.currentResult}
                    Started at: ${new Date(currentBuild.startTimeInMillis)}
                    Duration: ${currentBuild.durationString}
                """
                telegramSend(message: buildInfo)
            }
        }
    }
}
