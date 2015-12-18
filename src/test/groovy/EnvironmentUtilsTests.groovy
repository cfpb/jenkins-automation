import spock.lang.Specification

import static jenkins.automation.utils.EnvironmentUtils.getEnv
import static jenkins.automation.utils.EnvironmentUtils.isProd
import static jenkins.automation.utils.EnvironmentUtils.isDev
import static jenkins.automation.utils.EnvironmentUtils.isStage

class EnvironmentUtilsTests extends Specification  {

    void 'Should Correctly Return Env'() {

        given:

        def ENVIRONMENT = 'dev'

        when:
        def prod = isProd(ENVIRONMENT)
        def dev = isDev(ENVIRONMENT)
        def stage = isStage(ENVIRONMENT)



        then:
        assert  dev
        assert !prod
        assert !stage
        assert getEnv(ENVIRONMENT) as String == ENVIRONMENT.toUpperCase() ;
    }
}
