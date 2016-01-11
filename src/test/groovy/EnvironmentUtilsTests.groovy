/**
 * Created by muchniki on 12/13/15.
 */
import jenkins.automation.utils.EnvironmentUtils
import spock.lang.Specification

class EnvironmentUtilsTests extends Specification  {

    void 'Should Correctly Return Env'() {

        given:
        def ENVIRONMENT = 'dev'

        when:
        def myEnv = EnvironmentUtils.getInstance(ENVIRONMENT)

        then:
        assert myEnv.isDev()
        assert !myEnv.isProd()
        assert !myEnv.isStage()
        assert myEnv.getEnv() as String == ENVIRONMENT;
    }
}