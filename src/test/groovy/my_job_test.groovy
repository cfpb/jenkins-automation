
import javaposse.jobdsl.dsl.*
import spock.lang.*
import jenkins.automation.utils.XmlParserHelper


@Ignore
class my_job_test extends Specification {

    void 'test my flow build job'() {

        given:
        JobManagement jm = new MemoryJobManagement()

        def testjob = new File('jobs/mother-seed-job-example.groovy').text
        def expected = new File('path-to-xml-file.xml').text

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