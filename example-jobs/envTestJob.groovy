
import static jenkins.automation.utils.EnvironmentUtils.isDev


def env=${ENVIRONMENT}
if (isDev(env)){
    //do something
}



job('test') {
    steps {
        shell """echo $env
      """
    }
}