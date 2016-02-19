import javaposse.jobdsl.dsl.*
import spock.lang.*
import jenkins.automation.utils.XmlParserHelper



class my_job_test extends Specification {

    void 'test my flow build job'() {

        given:
        JobManagement jm = new MemoryJobManagement()

        def testjob = new File('example-jobs/SampleJsBuildJob-example.groovy').text


        when:

        ScriptRequest scriptRequest = new ScriptRequest(null, testjob, new File('.').toURI().toURL())
        DslScriptLoader.runDslEngine(scriptRequest, jm)


        then:

        noExceptionThrown()

    }


}


