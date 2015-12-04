package jenkins.automation.builders

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

        Job jsJob = new JsJobBuilder(
                name: this.name,
                description: this.description,
                repos: this.repos,
                emails: this.emails,
                use_versions: true
        ).build(factory)

        print jsJob

        jsJob.steps {

                shell(
                        '''
                              cd $DIR_UNDER_TEST
                              ./frontendtest.sh

                              '''
                )
            }

    }
}
