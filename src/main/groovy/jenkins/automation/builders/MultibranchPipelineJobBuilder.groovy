package jenkins.automation.builders
import jenkins.automation.utils.CommonUtils

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job


/**
 * Sauce Connect Job builder creates a default Sauce Connect OnDemand plugin configuration

 *
 * @param name job name
 * @param description job description
 * @param webDriverBrowser browser to use with Sauce Connect
 * @param sauceCredentialId SauceCredential to use for the sauce plugin
 * @param additionalOptions (Optional) additional option to use e.g '-v'
 *
 */
class MultibranchPipelineJobBuilder {
    String name
    String description
    List<String> emails

    /**
     * The main job-dsl script that build job configuration xml
     * @param DslFactory
     */
    Job build(DslFactory factory) {
        factory.multibranchPipelineJob('example') {
            branchSources {
                github {
                    scanCredentialsId('8fbdbaa0-d5ff-4acb-9e8f-27e49b77048b') // Gitbot GHE credentials
                    repoOwner('testprbuilder')
                    repository('pr_tester')
                    apiUri('https://github.cfpb.gov/api/v3')
                }
            }
        }
    }
}
