/**
 * Created by muchniki on 10/27/15.
 */
import javaposse.jobdsl.dsl.*
import spock.lang.Specification
import XmlParserHelper

class cfGovMigrationsTests extends Specification {

    void 'test cf.gov flow build job'() {

        given:
        JobManagement jm = new MemoryJobManagement()

        def testjob = new File('src/test/migrated-jobs/prod-cfgov-buildFlow.groovy').text
        def expected = new File('src/test/resources/prod-cf-gov-build-flow.xml').text

        when:

        ScriptRequest scriptRequest = new ScriptRequest(null, testjob, new File('.').toURI().toURL())
        DslScriptLoader.runDslEngine(scriptRequest, jm)

        def result = jm.savedConfigs.collect { [name: it.key, xml: it.value] }
        result += jm.savedViews.collect { [name: it.key, xml: it.value] }

        def jobMap = new XmlParserHelper().parse(expected)
        def resultMap = new XmlParserHelper().parse(result[0].xml)

        then:
        jobMap.each { k, v ->
            if (!resultMap[k] == v) {
                //helpful for debugging
                println "actual  :" + v
                println "expected:" + resultMap[k]
            }
                assert resultMap[k] == v

        }


    }


}