package jenkins.automation.builders
import jenkins.automation.utils.CommonUtils

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.jobs.MultibranchWorkflowJob


/**
 * TODO
 */
class MultibranchPipelineJobBuilder {
    String name
    String description
    List<String> emails

    MultibranchWorkflowJob build(DslFactory factory) {
        factory.multibranchPipelineJob(name) {
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
