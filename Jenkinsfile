node {
    checkout scm

    stage('Install bundle') {
        sh 'gem install bundle --user-install --no-ri --no-rdoc'
        sh 'bundle install --path vendor/bundle'
    }

    stage('Build') {
        sh './gradlew clean check assembleDebug'
        junit '**/test-results/**/*.xml'
    }

    stage('Archive') {
        archiveArtifacts 'app/build/outputs/apk/**'
        archiveArtifacts 'app/build/reports/**'
    }
 }