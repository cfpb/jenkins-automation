import jenkins.automation.utils.EnvironmentUtils

//in our Jenkinses, we have a global variable named "JAC_ENVIRONMENT",
//with values of "dev", "stage", or "prod"
def env = EnvironmentUtils.getInstance("${JAC_ENVIRONMENT}")
println "Environment is " + env.getEnv()

if(env.isDev()) {
    //do something
}

job('test') {
    steps {
        shell "echo $env"
    }
}