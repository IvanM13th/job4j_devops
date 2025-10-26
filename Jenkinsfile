pipeline {
    agent { label 'agent-devops' }

    tools {
        git 'Default'
    }

    environment {
        }

    stages {
        stage('Prepare Environment') {
            steps {
                sh 'chmod +x ./gradlew'
            }
        }

        stage('Load Environment') {
                    steps {
                        script {
                            // Считываем .env файл и устанавливаем переменные
                            def envProps = readProperties file: '/var/agent-jdk21/env/.env.develop'
                            env.DB_URL = envProps['DB_URL']
                            env.DB_USERNAME = envProps['DB_USERNAME']
                            env.DB_PASSWORD = envProps['DB_PASSWORD']
                        }
                    }
                }

        stage('Check Environment') {
            steps {
                script {
                    sh './gradlew check'
                }
            }
        }
        stage('Update DB') {
            steps {
                script {
                    sh './gradlew update'
                }
            }
        }
        stage('Package') {
            steps {
             sh './gradlew build'
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
