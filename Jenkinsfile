#!/usr/bin/env groovy

node {
  try {
    def mvnHome
    stage('\u27A1 Preparation') {
      git 'git@github.com:openwms/org.openwms.wms.receiving.git'
      mvnHome = tool 'M3'
    }
    stage('\u27A1 Build') {
      configFileProvider(
          [configFile(fileId: 'maven-local-settings', variable: 'MAVEN_SETTINGS')]) {
            sh "'${mvnHome}/bin/mvn' -s $MAVEN_SETTINGS clean install -Dci.buildNumber=${BUILD_NUMBER} -Ddocumentation.dir=${WORKSPACE}/target -Prelease,sonatype -U"
      }
    }
    stage('\u27A1 Heroku Staging') {
      sh '''
        if git remote | grep heroku > /dev/null; then
           git remote rm heroku
        fi
        git remote add heroku https://:${HEROKU_API_KEY}@git.heroku.com/openwms-wms-receiving.git
        git push heroku master -f
      '''
    }
    stage('\u27A1 Results') {
      archive '**/target/*.jar'
    }
    stage('\u27A1 Documentation') {
      configFileProvider(
          [configFile(fileId: 'maven-local-settings', variable: 'MAVEN_SETTINGS')]) {
            sh "'${mvnHome}/bin/mvn' -s $MAVEN_SETTINGS install site site:deploy -Dci.buildNumber=${BUILD_NUMBER} -Ddocumentation.dir=${WORKSPACE}/target -Psonatype"
      }
    }
    stage('\u27A1 Sonar') {
      sh "'${mvnHome}/bin/mvn' clean org.jacoco:jacoco-maven-plugin:prepare-agent verify -Dci.buildNumber=${BUILD_NUMBER} -Ddocumentation.dir=${WORKSPACE}/target -Pjenkins"
      sh "'${mvnHome}/bin/mvn' sonar:sonar -Pjenkins"
    }
  } finally {
    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/TEST-*.xml'
  }
}

