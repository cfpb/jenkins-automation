import jenkins.automation.utils.EnvironmentUtils

import static jenkins.automation.utils.EnvironmentUtils.getEnv
import static jenkins.automation.utils.EnvironmentUtils.isDev

def env = getEnv();

def configuration = new HashMap()
def binding = getBinding()
configuration.putAll(binding.getVariables())

String shoo = configuration["ENVIRONMENT"]

job('test') {
    steps {
        shell """echo $env
            echo $shoo

    """
    }


}