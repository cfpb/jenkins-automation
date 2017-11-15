package jenkins.automation.builders


import java.util.Map
import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import jenkins.automation.utils.CheckmarxUtils
import jenkins.automation.utils.ScmUtils


/**
 * Checkmarx Security builder creates a default Checkmarx security build configuration
 *
 * @param name job name
 * @param description job description
 * @param emails list of recipients to get notifications
 * @param scanRepo a collection of Github repos to scan with Checkmarx
 * @param checkmarxConfig a Map to configure the Checkmarx project; see `CheckmarxUtils.checkmarxScan` for a list of valid options
 * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#checkmarx-security-job-builder" target="_blank">Checkmarx job Example</a>
 *
 */

class CheckmarxSecurityJobBuilder {

    String name
    String description
    List<String> emails
    List scanRepo = []
    Map checkmarxConfig

    Job build(DslFactory factory) {

        def baseJob = new BaseJobBuilder(
            name: this.name,
            description: this.description,
            emails: this.emails
        ).build(factory)

        baseJob.with {
            multiscm {
                ScmUtils.project_repos(delegate, this.scanRepo, false)
            }
            CheckmarxUtils.checkmarxScan(delegate, this.checkmarxConfig)
        }

        return baseJob
    }
}
