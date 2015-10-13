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

        Job job = new JsJobBuilder(
                name: "$basePath/${this.name}",
                description: this.description,
                repos: this.repos,
                emails: this.emails,
                use_versions: true
        ).build(this)

        job.with {

            steps {
                shell(
                        '''
                              cd $DIR_UNDER_TEST
                              ./frontendtest.sh

                              '''
                )
            }
      }
        }
    }
