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
        println jobMap
        println resultMap
        then:
        println "generated"+result[0].xml
        jobMap.each { k, v ->
            if (!resultMap[k].toString().trim().equals(v.toString().trim())) {
                println "generated"+result[0].xml
                println "actual  :"+ v.toString().trim()
                println "expected:"+resultMap[k].toString().trim()
            }
            assert resultMap[k] == v

        }


    }


}