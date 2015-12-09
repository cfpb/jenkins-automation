
import static jenkins.automation.utils.EnvironmentUtils.isDev


def env
if (isDev(${ENVIRONMENT})){
    env = 'DEV'
}



job('test') {
    steps {
        shell """echo $env
      """
    }
}