/**
 * Created by muchniki on 10/27/15.
 */
import javaposse.jobdsl.dsl.*
import javaposse.jobdsl.dsl.DslContext
import spock.lang.Specification

@Mixin(SpecUtilsMixin)
class cfGovMigrationsTests extends Specification {
    JobParent jobParent = createJobParent()
    FlowJobBuilder builder

    def setup() {
        builder = new FlowJobBuilder(
                name: 'test-job',
                description: 'testing'
        )
    }

//    void 'test XML output'() {
//        when:
//        jobParent
//        Job job = builder.build(jobParent)
//        print job
//        then:
//        job.name == builder.name
//
//        with(job.node) {
//            print it
//            name() == 'project'
//            triggers.'hudson.triggers.SCMTrigger'.spec.text() == '*/5 * * * *'
//            !publishers.'hudson.tasks.Mailer'.recipients
//        }
//    }

    void 'test cf.gov flow build job'() {

        given:
        JobManagement jm = new MemoryJobManagement()

        Closure testjob = { new File('src/test/migrated-jobs/prod-cfgov-buildFlow.groovy').text.toString() }
        def ch = new ContextHelper()

        when:
//        //def result=DslScriptLoader.runDslEngine testjob.text, jm
//       def result=ch.executeInContext(testjob, DslFactory.hashCode())
        testjob.resolveStrategy = Closure.DELEGATE_FIRST
        def result=testjob.call()

        then:

        println result

    }


}