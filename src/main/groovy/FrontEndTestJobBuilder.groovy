import JsJobBuilder
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
    def repos;

    Job build(DslFactory factory) {

        def job = new JsJobBuilder(
                name: this.name,
                description: this.description,
                repos: this.repos,
                emails: this.emails,
                use_versions: true
        ).build(factory)

//        job.with {
//
//            steps {
//                shell(
//                        '''
//                              cd $DIR_UNDER_TEST
//                              ./frontendtest.sh
//
//                              '''
//                )
//            }
//        }
    }
}
