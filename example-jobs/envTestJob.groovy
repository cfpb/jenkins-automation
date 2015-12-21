
import static jenkins.automation.utils.EnvironmentUtils


def env=EnvironmentUtils.getInstance("${ENVIRONMENT}")

if (env.isDev()){
    //do something
}

job('test') {
    steps {
        shell """echo $env
      """
    }
}