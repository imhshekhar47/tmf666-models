pipeline {
    agent {
        label 'jenkins-slave'
    }

    environment {
        BRANCH = "develop"
    }

    stages {

        stage("checkout") {
            steps {
                git url: 'https://github.com/imhshekhar47/tmf666-models.git', branch: "${env.BRANCH}",  credentialsId: 'github-imhshekhar47'
            }
        }

        stage("build") {
            steps {
                container("gradle") {
                    sh './gradlew clean build'
                }
            }
        }
    }

    post {
        success {
            echo "Notify success"
        }

        failure {
            echo "notify failure"
        }
    }
}