import javaposse.jobdsl.dsl.*
import spock.lang.*


class my_job_test extends Specification {

    void 'test my flow build job'() {

        given:
        JobManagement jm = new MemoryJobManagement()

        def testjob = new File('example-jobs/new_relic_notifier_job.groovy').text


        when:
        ScriptRequest scriptRequest = new ScriptRequest(null, testjob, new File('.').toURI().toURL())
        DslScriptLoader.runDslEngine(scriptRequest, jm)
        def result = jm.savedConfigs.collect { [name: it.key, xml: it.value] }


        then:
        print result
        noExceptionThrown()

    }


}


