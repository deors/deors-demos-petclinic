#!groovy

pipeline {
    agent {
        docker {
            image 'maven:3.5.4-jdk-8'
            args '--network ci --mount type=volume,source=ci-maven-home,target=/root/.m2'
        }
    }

    stages {
        stage('Compile') {
            steps {
                echo "-=- compiling project -=-"
                sh "mvn clean compile"
            }
        }

        stage('Unit tests') {
            steps {
                echo "-=- execute unit tests -=-"
                sh "mvn test"
                junit 'target/surefire-reports/*.xml'
                jacoco execPattern: 'target/jacoco.exec'
            }
        }

        stage('Mutation tests') {
            steps {
                echo "-=- execute mutation tests -=-"
                sh "mvn org.pitest:pitest-maven:mutationCoverage"
            }
        }

        stage('Package') {
            steps {
                echo "-=- packaging project -=-"
                sh "mvn package -DskipTests"
                archiveArtifacts artifacts: 'target/*.war', fingerprint: true
            }
        }

        stage('Build Docker image') {
            steps {
                echo "-=- build Docker image -=-"
                sh "mvn docker:build"
            }
        }

        stage('Run Docker image') {
            steps {
                echo "-=- run Docker image -=-"
                sh "docker run --name ci-deors-demos-petclinic --detach --rm --network ci --expose 6300 --env JAVA_OPTS='-javaagent:/usr/local/tomcat/jacocoagent.jar=output=tcpserver,address=*,port=6300' deors/deors-demos-petclinic:latest"
            }
        }

        stage('Integration tests') {
            steps {
                echo "-=- execute integration tests -=-"
                sh "mvn failsafe:integration-test failsafe:verify -DargLine=\"-Dtest.selenium.hub.url=http://selenium-hub:4444/wd/hub -Dtest.target.server.url=http://ci-deors-demos-petclinic:8080/petclinic\""
                sh "java -jar target/dependency/jacococli.jar dump --address ci-deors-demos-petclinic --port 6300 --destfile target/jacoco-it.exec"
                junit 'target/failsafe-reports/*.xml'
                jacoco execPattern: 'target/jacoco-it.exec'
            }
        }

        stage('Performance tests') {
            steps {
                echo "-=- execute performance tests -=-"
                sh "mvn jmeter:jmeter jmeter:results -Djmeter.target.host=ci-deors-demos-petclinic -Djmeter.target.port=8080 -Djmeter.target.root=petclinic"
                perfReport sourceDataFiles: 'target/jmeter/results/petclinic.csv', errorUnstableThreshold: 0, errorFailedThreshold: 5, errorUnstableResponseTimeThreshold: 'petclinic.jtl:100'
            }
        }

        stage('Dependency vulnerability tests') {
            steps {
                echo "-=- run dependency vulnerability tests -=-"
                sh "mvn dependency-check:check"
                dependencyCheckPublisher failedTotalHigh: '30', unstableTotalHigh: '25', failedTotalNormal: '110', unstableTotalNormal: '100'
            }
        }

        stage('Code inspection & quality gate') {
            steps {
                echo "-=- run code inspection & check quality gate -=-"
                withSonarQubeEnv('ci-sonarqube') {
                    sh "mvn sonar:sonar"
                }
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Push Docker image') {
            steps {
                echo "-=- push Docker image -=-"
                sh "mvn docker:push"
            }
        }
    }

    post {
        always {
            echo "-=- remove deployment -=-"
            sh "docker stop ci-deors-demos-petclinic"
        }
    }
}
