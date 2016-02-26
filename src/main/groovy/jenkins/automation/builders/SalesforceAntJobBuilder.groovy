package jenkins.automation.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import jenkins.automation.builders.BaseJobBuilder

/**
 * This is a basic builder for Salesforce deployments using Force.com
 * migration tool
 * @param name Job Name
 * @param description Job description
 * @param emails List of emails for build notifications
 * @param antTaskName and target to execute
 * @param repoUrl Git repository to clone
 * @param antInstallerName Name of ant installer on the Jenkins server
 *
 * <p>
 *
 * @see <a href="https://github.com/imuchnik/jenkins-automation/blob/gh-pages/docs/examples.md#salesforce-job-builder"
 *      target="_blank">Salesforce job builder example</a>

 * </p>
 */
class SalesforceAntJobBuilder {


    Job build(DslFactory factory) {

        String name
        String description
        List emails
        String antTaskName
        String repoUrl
        String antInstallerName


        def job = new BaseJobBuilder(
                name: name,
                description: description,
                emails: emails
        ).build(factory);

        job.with {
            logRotator {
                numToKeep(365)
            }
            scm {
                git {
                    remote {
                        url(repoUrl)
                    }
                }
            }
            steps {
                ant {
                    target(antTaskName)
                    antInstallation(antInstallerName)
                }
            }
        }

    }
}
