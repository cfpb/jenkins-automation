package jenkins.automation.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import jenkins.automation.utils.ScmUtils

/**
 * Checkmarx Security builder creates a default Checkmarx security build configuration
 *
 * @param name job name
 * @param description job description
 * @param emails list of recipients to get notifications
 * @param scanRepo a collection of Github repos to scan with Checkmarx
 * @param checkmarxComment Additional comment(s) to include in the scan results
 * @param useOwnServerCredentials If set to false then credentials from the Manage Jenkins page are used; serverUrl, username and password parameters are ignored.
 * If set to true then credentials must be specified in serverUrl, username and password parameters
 * @param serverUrl URL of the Checkmarx server
 * @param username Checkmarx user name
 * @param password Checkmarx password
 * @param groupId Checkmarx group ID, which is actually the team ID, and which can be gotten by browser-inspecting the 'Team' select box for an existing Checkmarx job in the Jenkins UI
 * @param incremental perform incremental Checkmarx scans (enabled by default)
 * @param filterPattern files to exclude
 * @param excludeFolders folders to exclude
 * @param preset the ID of the Checkmarx preset configuration to use; this can be gotten by browser-inspecting the 'Preset' select box for an existing Checkmarx job in the Jenkins UI
 * @param vulnerabilityThresholdEnabled Mark the build as unstable if the number of high severity vulnerabilities is above the specified threshold
 * @param highThreshold High severity vulnerabilities threshold
 * @param mediumThreshold Medium severity vulnerabilities threshold
 * @param lowThreshold Low severity vulnerabilities threshold
 *
 * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#checkmarx-security-job-builder" target="_blank">Checkmarx job Example</a>
 *
 */

class CheckmarxSecurityJobBuilder {

    String name
    String description
    List<String> emails
    def scanRepo = []
    String checkmarxComment
    Boolean useOwnServerCredentials = false
    String serverUrl
    String username
    String password
    String groupId
    Boolean incremental = true
    String filterPattern = "!**/_cvs/**/*, !**/.svn/**/*"
    String excludeFolders = "resources, .git"
    String preset = "17"// Default 2014
    String fullScanCycle = "10"
    Boolean vulnerabilityThresholdEnabled = true
    String highThreshold = 1
    String mediumThreshold = 2
    String lowThreshold = 3

    /**
     * The main job-dsl script that build job configuration xml
     * <p>This example uses a single GH repo
     * <p>For multiple GH repo support use ScmUtils 
     *
     * @param DslFactory
     * @return Job
     */
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
        }

        baseJob.with {
            configure { project ->
                project / builders / 'com.checkmarx.jenkins.CxScanBuilder' {
                    'useOwnServerCredentials'(useOwnServerCredentials)
                    'serverUrl'(serverUrl)
                    'username'(username)
                    'password'(password)
                    'projectName'(name) // Checkmarx Project Name
                    'groupId'(groupId)  // Team
                    'preset'(preset)
                    'presetSpecified'(false) // TODO figure out whether this does anything
                    'excludeFolders'(excludeFolders)
                    'filterPattern'(filterPattern)
                    'exclusionsSetting'('job')
                    'incremental' (incremental)
                    'fullScansScheduled'(false)
                    'fullScanCycle'(fullScanCycle)
                    'isThisBuildIncremental'(false) // TODO should we really be setting this?
                    'sourceEncoding'('1') // Default Configuration
                    checkmarxComment ? 'comment'(checkmarxComment) : null
                    'skipSCMTriggers'(false) // Skip scan if triggered by SCM Changes
                    'waitForResultsEnabled'(true) // Enable synchronous mode
                    'vulnerabilityThresholdEnabled'(vulnerabilityThresholdEnabled) // Enable vulnerability threshold
                    'highThreshold'(highThreshold) // High severity vulnerabilities threshold
                    'mediumThreshold'(mediumThreshold) // Medium severity vulnerabilities threshold
                    'lowThreshold'(lowThreshold) // Low severity vulnerabilities threshold
                    'generatePdfReport'(true)
                }
            }
        }

        return baseJob
    }
}
