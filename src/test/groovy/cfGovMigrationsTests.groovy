/**
 * Created by muchniki on 10/27/15.
 */
import javaposse.jobdsl.dsl.*
import spock.lang.Specification

class cfGovMigrationsTests extends Specification {

    void 'test cf.gov flow build job'() {

        given:
        JobManagement jm = new MemoryJobManagement()

        def testjob =  new File('src/test/migrated-jobs/prod-cfgov-buildFlow.groovy').text
        def expected = new File('src/test/resources/prod-cf-gov-build-flow.xml').text

        when:

        ScriptRequest scriptRequest = new ScriptRequest(null, testjob, new File('.').toURI().toURL())
        DslScriptLoader.runDslEngine(scriptRequest, jm)

        def result = jm.savedConfigs.collect { [name: it.key, xml: it.value] }
        result += jm.savedViews.collect { [name: it.key, xml: it.value] }

        then:

        result[0].xml==expected

    }


}