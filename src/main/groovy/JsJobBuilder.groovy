import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

class JsJobBuilder {

    String name
    String description
    String ownerAndProject
    String gitBranch = 'master'
    String pollScmSchedule = '@daily'
    String tasks
    String switches
    Boolean useWrapper = true
    String junitResults = '**/build/test-results/*.xml'
    String artifacts = 'dist/'
    def repos =[];


    Job build(DslFactory dslFactory) {
        dslFactory.job(name) {
            it.description this.description
            wrappers {
                colorizeOutput()
                nodejs('Node 0.12')// pass in the version?
            }

            multiscm {
                ScmUtils.project_repos(delegate, this.repos)
            }

            triggers {
                scm pollScmSchedule
            }
            steps {
                shell( //we can potentially pass those in as well - $DIR_TO_BUILD and build script name
                        '''
                            cd $DIR_TO_BUILD
                             ./frontendbuild.sh
                        '''
                )
            }
            publishers {
                archiveArtifacts artifacts
                if (emails) {
                    mailer emails.join(' ')
                }
            }
        }
    }
}