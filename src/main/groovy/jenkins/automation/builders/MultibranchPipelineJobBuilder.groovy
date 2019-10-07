package jenkins.automation.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.jobs.MultibranchWorkflowJob


/*
    NB: if you are polling via the GitHub API (github.com or GHE) and
        want to use webhooks with a multibranch pipeline, but the webhooks
        are not being created, it may be because you need to update Jenkins
        plugins; ensure that the 'Branch API Plugin' and the 'GitHub Branch
        Source Plugins' are both up to date
*/


/**
 * Multibranch Pipeline Job builder creates a set of Pipeline projects
 * according to detected branches in a Git repository
 * @param name job name
 * @param description description for the folder
 * @param branchSource the method used to access the git repo; see
 * <code>BranchSourceType</code> for options
 * @param gitCredentials Jenkins git credentials GUID used to scan branches, etc
 * @param ghOwner name of the organization or user when branchSource is 'GitHub'
 * @param ghRepo name of the git repo when branchSource is 'GitHub'
 * @param ghApiEndpoint (optional) the URL of the GitHub API endpoint
 * when branchSource is 'GitHub'; when pointing to a GitHub Enterprise API URL,
 * you will first need to make the corresponding configurations in your Jenkins's
 * 'GitHub' and 'GitHub Enterprise Servers' sections on the 'Configure System'
 * page (otherwise a custom non-github.com API endpoint will not work)
 * @param gitRemote the URL of the git repo when branchSource is 'git'
 * @param oldNumToKeep (optional) Number of builds to keep after a git branch
 * has been removed
 */
class MultibranchPipelineJobBuilder {
    String name
    String description
    BranchSourceType branchSource
    String gitCredentials
    String ghOwner
    String ghRepo
    String ghApiEndpoint
    String gitRemote
    int oldNumToKeep = 10

    enum BranchSourceType {
        /**
         * use git, instead of the GitHub API, to scan for branches, etc
         */
        GIT,

        /**
         * use a GitHub API (GitHub Enterprise or github.com) to scan for branches, etc
         */
        GITHUB,
    }

    MultibranchWorkflowJob build(DslFactory factory) {
        factory.multibranchPipelineJob(name) {
            it.description this.description

            branchSources {
                switch (branchSource) {
                    case BranchSourceType.GIT:
                        git {
                            id(name)
                            remote(gitRemote)
                            credentialsId(gitCredentials)
                        }
                        break
                    case BranchSourceType.GITHUB:
                        github {
                            id(name)
                            scanCredentialsId(gitCredentials)
                            repoOwner(ghOwner)
                            repository(ghRepo)
                            apiUri(ghApiEndpoint)
                        }
                        break
                    default:
                        throw new Exception("Unhandled BranchSource type")
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
