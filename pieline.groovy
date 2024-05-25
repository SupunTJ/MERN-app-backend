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
                    sh 'docker build -t Supun3998/server-app-image .'
                }
            }
        }
        stage('Run Docker Container') {
            steps {
                script {
                    // Stop any running containers with the same name
                    
                    // Run the new container
                    sh 'docker run -d -p 3000:3000 Supun3998/server-app-image'
                }
            }
        }
        stage('Show Running Containers') {
            steps {
                sh 'docker ps'
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
