import jenkins.automation.utils.EnvironmentUtils

import static jenkins.automation.utils.EnvironmentUtils.isDev

def env = isDev();

job('test') {
    steps {
        shell """echo $env
                echo ${ENVIRONMENT}
    """
    }


}