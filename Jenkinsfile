pipeline {
  tools {
    maven "M3"
  }
  agent any
  stages {
  stage("Create jar") {
    steps {
      sh "cd /home/ais/j.noguerales-b.estebana ; mvn package -DskipTests"
    }
   }
   
   stage("Start app") {
    steps {
      sh "cd /home/ais/j.noguerales-b.estebana/target;java -jar j.noguerales-b.estebana-0.0.1-SNAPSHOT.jar > out.log & echo \$! > pid"
    }
   }
   
   stage("Test") {
    steps {
      sh "cd /home/ais/j.noguerales-b.estebana ; mvn test"
    }
   }
  } 
  post {
    always {
      junit "**/target/surefire-reports/TEST-*.xml"
      sh "kill \$(cat /home/ais/j.noguerales-b.estebana/target/pid)"
      archive "/home/ais/j.noguerales-b.estebana/target/out.log"
    }
    success {
      archive "/home/ais/j.noguerales-b.estebana/target/*.jar"
    }
  }
}
