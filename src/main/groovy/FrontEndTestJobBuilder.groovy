import  JsJobBuilder
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

class FrontEndTestJobBuilder {

    String name
    String description
    String gitBranch = 'master'
    String pollScmSchedule = '@daily'
    String tasks
    String junitResults = '**/build/test-results/*.xml'
    String artifacts = 'dist/'
    List<String> emails
    Boolean use_versions


    Job build(DslFactory factory ){

        Job job= new JsJobBuilder(
                name: "$basePath/SampleJob1",
                description: 'An example using a job builder for a Javascript build jobs project.',
                repos: repos,
                emails: developers,
                use_versions: true
        ).build(this)

        job.with{

//            passing test_stuff
        }
    }
}
