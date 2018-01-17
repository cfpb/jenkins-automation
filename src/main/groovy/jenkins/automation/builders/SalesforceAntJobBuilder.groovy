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
 * @param antTasks List of ant target to execute
 * @param repoUrl Git repository to clone
 * @param antInstallerName Name of ant installer on the Jenkins server
 *
 * <p>
 *
 * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#salesforce-job-builder"
 *      target="_blank">Salesforce job builder example</a>

 * </p>
 */
class SalesforceAntJobBuilder {

    String name
    String description
    List emails
    def antTasks =[]
    String repoUrl
    String antInstallerName

    Job build(DslFactory factory) {

        def job = new BaseJobBuilder(
                name: this.name,
                description: this.description,
                emails: this.emails
        ).build(factory);

        job.with {

            if(this.repoUrl){
                scm {
                    git {
                        remote {
                            url(this.repoUrl)
                        }
                    }
                }
            }

            steps {
                ant {
                    targets(this.antTasks)
                    if(this.antInstallerName){
                        antInstallation(this.antInstallerName)
                    }
                }
            }
        }

    }
}
