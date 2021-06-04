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
                    withSonarQubeEnv("SonarQube") {
                        sh './gradlew clean build'
                    }
                }
            }
        }

        stage("sonar-scan") {
            steps {
                container("gradle") {
                    withSonarQubeEnv("SonarQube") {
                        sh './gradlew sonarqube'
                    }
                }
            }
        }

        stage("quality-gate") {
            steps {
                waitForQualityGate abortPipeline: true
            }
        }

        stage("publish") {
            environment {
                NEXUS_URL = "${env.NEXUS_URL}/repository/maven-snapshots/"
            }
            steps {
                echo "${PWD}"
                container("gradle") {
                    sh 'printenv'
                    echo "${env.NEXUS_URL}"
                    withCredentials([usernamePassword(credentialsId: 'nexus-access-credential', passwordVariable: 'password', usernameVariable: 'username')]) {
                        sh "./gradlew publish -Pnexus.url=${env.NEXUS_URL} -Pnexus.username=${username} -Pnexus.password=${password} --info --stacktrace"
                    }
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