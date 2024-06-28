pipeline {
    agent any

    stages {
        stage('SCM checkout') {
            steps {
                retry(3) {
                    checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/SupunTJ/MERN-app-backend-dockerizing.git/']])
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t supun3998/server-app-image .'
                }
            }
        }

       
        stage('Run Docker Container') {
            steps {
                script {
                    // Stop any running containers with the same name
                    sh 'docker stop server-container || true'
                    sh 'docker rm server-container || true'
                    // Run the new container
                    sh 'docker run -d -p 3000:3000 --name server-container supun3998/server-app-image'
                }
            }
        }
        stage('Show Running Containers') {
            steps {
                sh 'docker ps'
            }
        }

        stage('Login to Docker Hub') {
            steps {
                withCredentials([string(credentialsId: 'dockerhubpwd', variable: 'Dockerhub')]) {
                    script {
                        sh "docker login -u supun3998 -p ${Dockerhub}"
                    }
                }
            }
        }

        stage('Push Image') {
            steps {
                script {
                    retry(3) {
                        echo 'Pushing image to Docker Hub...'
                        sh 'docker push supun3998/server-app-image'
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}


// pipeline {
//     agent any

//     environment {
//         DOCKER_IMAGE = "Supun3998/server-app-image"
//         DOCKER_REGISTRY_CREDENTIALS = 'your-docker-credentials-id' // If pushing to a private registry
//     }

//     stages {
//         stage('SCM checkout') {
//             steps {
//                 retry(3) {
//                     checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/SupunTJ/MERN-app-backend-dockerizing.git']])
//                 }
//             }
//         }
//         stage('Build Docker Image') {
//             steps {
//                 script {
//                     sh "docker build -t ${DOCKER_IMAGE} ."
//                 }
//             }
//         }
//         stage('Run Docker Container') {
//             steps {
//                 script {
//                     // Stop and remove any running containers with the same image
//                     try {
//                         sh 'docker stop $(docker ps -q --filter ancestor=${DOCKER_IMAGE})'
//                     } catch (Exception e) {
//                         echo 'No containers to stop'
//                     }
//                     try {
//                         sh 'docker rm $(docker ps -aq --filter ancestor=${DOCKER_IMAGE})'
//                     } catch (Exception e) {
//                         echo 'No containers to remove'
//                     }
//                     // Run the new container
//                     sh "docker run -d -p 3000:3000 ${DOCKER_IMAGE}"
//                 }
//             }
//         }
//         stage('Show Running Containers') {
//             steps {
//                 sh 'docker ps'
//             }
//         }
//     }

//     post {
//         always {
//             cleanWs()
//         }
//     }
// }
