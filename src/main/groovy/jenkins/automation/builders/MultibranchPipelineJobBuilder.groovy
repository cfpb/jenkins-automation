package jenkins.automation.builders
import jenkins.automation.utils.CommonUtils

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.jobs.MultibranchWorkflowJob


/**
 * Multibranch Pipeline Job builder creates a set of Pipeline projects according to detected branches in one SCM repository
 * @param name Sets the job name
 * @param description Sets a description for the folder
 * @param branchSourceGithub if true, provide  ghOrganizationName, ghRepositoryName, ghScanCredentialsId and ghEndpoint
 * @param ghOrganizationName Sets the name of the GitHub Organization or GitHub User Account
 * @param ghRepositoryName Sets the name of the GitHub repository
 * @param ghScanCredentialsId Sets scan credentials for authentication with GitHub
 * @param ghEndpoint Sets the GitHub API URI
 * @param branchSourceGit if true, provide ghRemote and ghCredentialsId
 * @param ghRemote Sets the Git remote repository URL
 * @param ghCredentialsId Sets credentials for authentication with the remote repository 
 */
class MultibranchPipelineJobBuilder {
    String name
    String description
    Boolean branchSourceGithub
    String ghOrganizationName
    String ghRepositoryName
    String ghScanCredentialsId
    String ghEndpoint
    Boolean branchSourceGit
    String ghRemote
    String ghCredentialsId
    Boolean discardOldItems
    int oldNumToKeep

    MultibranchWorkflowJob build(DslFactory factory) {
        factory.multibranchPipelineJob(name) {
            it.description this.description
            branchSources {
                if(branchSourceGithub){
                    github {
                        scanCredentialsId(ghScanCredentialsId)
                        repoOwner(ghOrganizationName)
                        repository(ghRepositoryName)
                        apiUri(ghEndpoint)
                    }
                }
                if (branchSourceGit){
                    git{
                        remote(ghRemote)
                        credentialsId(ghCredentialsId)
                    }
                }
            }
            if(discardOldItems){
                orphanedItemStrategy {
                    discardOldItems {
                        numToKeep(oldNumToKeep)
                    }
                }
            }
        }
    }
}
