node{
    try{
        def credentials=''
        def url =''
        stage('Checkout'){
            git branch: env.BRANCH_NAME,changelog:true, credentialsId:credentials, poll: false, url:url
        }
        stage('build project'){
            sh 'mvn clean'
            sh 'mvn install'
        }
        stage('archiving artifact'){
            archiveArtifacts "command-response-mapper/target/maven-archiver/pom.properties"

        }
        stage('build docker image'){
        }

    }catch(Exception ee){
        currentBuild.result='FAILURE'
    }
}
