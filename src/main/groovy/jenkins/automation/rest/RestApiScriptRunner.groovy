// Credit: https://github.com/sheehan/job-dsl-gradle-example

package jenkins.automation.rest

import javaposse.jobdsl.dsl.DslScriptLoader

String pattern = System.getProperty('pattern')
String baseUrl = System.getProperty('baseUrl')
String username = System.getProperty('username')
String password = System.getProperty('password') // password or token

if (!pattern || !baseUrl) {
    println 'usage: -Dpattern=<pattern> -DbaseUrl=<baseUrl> [-Dusername=<username>] [-Dpassword=<password>]'
    System.exit 1
}

RestApiJobManagement jm = new RestApiJobManagement(baseUrl)
if (username && password) {
    println "Setting credentials to ${username}:${password}"
    jm.setCredentials username, password
}

//simulate our required Jenkins Environment Variables
params = jm.getParameters()
params['JAC_ENVIRONMENT'] = System.getProperty('JAC_ENVIRONMENT') ?: 'dev'
params['JAC_HOST'] = System.getProperty('JAC_HOST') ?: 'aws'
params['JENKINS_URL'] = baseUrl


new FileNameFinder().getFileNames('.', pattern).each { String fileName ->
    println "\nprocessing file: $fileName"
    File file = new File(fileName)
    new DslScriptLoader(jm).runScript(file.text)
}
