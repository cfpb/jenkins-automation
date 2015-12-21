package jenkins.automation.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import jenkins.automation.utils.ScmUtils

class JsJobBuilder {

    String name
    String description
    String gitBranch = 'master'
    String pollScmSchedule = '@daily'
    String tasks
    String junitResults = '**/build/test-results/*.xml'
    def artifacts = {
        pattern("dist/")
        fingerprint()
        defaultExcludes()
    }
    List<String> emails
    Boolean use_versions

    def repos = [];

    Job build(DslFactory factory) {

        def baseJob = new BaseJobBuilder(
                name: this.name,
                description: this.description,
                emails: this.emails
        ).build(factory)


        baseJob.with {
            wrappers {
                nodejs('Node 0.12')// pass in the version?
            }

            multiscm {
                ScmUtils.project_repos(delegate, this.repos, use_versions)
            }

            if (pollScmSchedule) {
                triggers {
                    scm pollScmSchedule
                }
            }

            if (artifacts) {
                publishers {
                    archiveArtifacts artifacts
                }
            }

        }

        baseJob
    }
}

