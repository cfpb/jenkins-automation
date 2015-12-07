import jenkins.automation.utils.EnvironmentUtils

import static jenkins.automation.utils.EnvironmentUtils.getEnv
import static jenkins.automation.utils.EnvironmentUtils.isDev

def env = getEnv();

job('test') {
    steps {
        shell """echo $env
                echo ${ENVIRONMENT}
    """
    }


}