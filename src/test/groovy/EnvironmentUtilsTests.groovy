/**
 * Created by muchniki on 12/13/15.
 */
import jenkins.automation.utils.EnvironmentUtils
import jenkins.automation.utils.Environment
import spock.lang.Specification

import static jenkins.automation.utils.EnvironmentUtils.getEnv
import static jenkins.automation.utils.EnvironmentUtils.isProd
import static jenkins.automation.utils.EnvironmentUtils.isDev

class EnvironmentUtilsTests extends Specification  {

    void 'Should Correctly Return Env'() {

        given:

        def ENVIRONMENT = 'dev'
        def myEnv = EnvironmentUtils.getInstance(ENVIRONMENT)

        when:
        def prod = myEnv.isProd()
        def dev = myEnv.isDev()



        then:
        assert dev
        assert !prod
        assert myEnv.getEnv() as String == ENVIRONMENT;
    }
}
