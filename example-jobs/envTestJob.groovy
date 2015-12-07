import jenkins.automation.utils.EnvironmentUtils

import static jenkins.automation.utils.EnvironmentUtils.isDev

def env = isDev();

Job('test') {
    steps {
        shell "echo $env"
    }


}