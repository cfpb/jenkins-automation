
import static jenkins.automation.utils.EnvironmentUtils.getEnv

def env = getEnv();

def configuration = new HashMap()
def binding = getBinding()
configuration.putAll(binding.getVariables())

String shoo = configuration["ENVIRONMENT"]

job('test') {
    steps {
        shell """echo $env
            echo soo $shoo

    """
    }


}