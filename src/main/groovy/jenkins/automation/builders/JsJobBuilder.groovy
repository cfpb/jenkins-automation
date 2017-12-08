package jenkins.automation.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import jenkins.automation.utils.ScmUtils

/**
 * @param name          Job name
 * @param description   Job description
 * @param pollScmSchedule  Optional Configure Jenkins to poll changes in SCM. Cron style schedule string.
 * @param artifacts     Optional Closure defaulted to closure below :
                                  {
                                     pattern("dist/")
                                     fingerprint()
                                     defaultExcludes()

                                     }

 * @param emails  List or String of notification email addresses
 * @param node_version  Version of nodeJs installation to use. Relies on nodejs plugin and corresponds to the installation
 *                      defined in Jenkins.
 * @param repos List of repos to watch
 * @param use_versions flag to check out the repo at a specific tag. Applies only to MultiScm block.
 *          The tag is parsed out from url property appended after '@' sign.

 * <p>
 *
 * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#js-job-builder"
 *      target="_blank">JS job builder example</a>

 * </p>
 */

class JsJobBuilder {
    String name
    String node_version="5.5.0"
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
    def emails
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
                nodejs(node_version)
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

