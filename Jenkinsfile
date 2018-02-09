
     step([$class: 'StashNotifier'])
     checkout scm
     stage('Build') {
       try {
         sh './gradlew --refresh-dependencies clean assemble'
         lock('emulator') {
           sh './gradlew connectedCheck'
         }
        currentBuild.result = 'SUCCESS'
       } catch(error) {
        slackSend channel: '#build-failures', color: 'bad', message: "This build is broken ${env.BUILD_URL}", token: 'XXXXXXXXXXX'
         currentBuild.result = 'FAILURE'
      } finally {
         junit '**/test-results/**/*.xml'
       }
     }
     stage('Archive') {
       archiveArtifacts 'app/build/outputs/apk/*'
     }
     step([$class: 'StashNotifier'])
