package jenkins.automation.builders
import jenkins.automation.utils.CommonUtils

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.jobs.MultibranchWorkflowJob


/**
 * Multibranch Pipeline Job builder creates a set of Pipeline projects according to detected branches in a Git repository
 * @param name job name
 * @param description description for the folder
 * @param branchSource set to "Github" for Github enterprise or "Git" for public Github repos
 * @param gitCredentials credentials used to scan branches
 * @param gitOwner name of the GitHub Organization or GitHub User Account when branchSource is "Github"
 * @param gitRepository name of the GitHub repository when branchSource is "Github"
 * @param gitEndpoint GitHub API endpoint when branchSource is "Github"
 * @param gitRemote Git remote project repository URL when branchSource is "Git"
 * @param oldNumToKeep (optional) Number of builds to keep after a git branch has been removed
 */
class MultibranchPipelineJobBuilder {
    String name
    String description
    String branchSource
    String gitCredentials
    String gitOwner
    String gitRepository
    String gitEndpoint
    String gitRemote
    int oldNumToKeep = 10

    MultibranchWorkflowJob build(DslFactory factory) {
        factory.multibranchPipelineJob(name) {
            it.description this.description
            branchSources {
                if(branchSource == "Github"){
                    github {
                        scanCredentialsId(gitCredentials)
                        repoOwner(gitOwner)
                        repository(gitRepository)
                        apiUri(gitEndpoint)
                    }
                }
                else if (branchSource == "Git"){
                    git{
                        remote(gitRemote)
                        credentialsId(credentialsId)
                    }
                }
            }
            orphanedItemStrategy {
                discardOldItems {
                    numToKeep(oldNumToKeep)
                }
            }
        }
    }
}
