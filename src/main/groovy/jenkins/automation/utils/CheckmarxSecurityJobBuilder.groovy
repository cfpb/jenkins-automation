package jenkins.automation.utils

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

/**
 * Checkmarx Security builder creates a default Checkmarx security build configuration

 *
 * @param name  job name
 * @param description  job description
 * @param scanRepo  Github repo to scan
 * @param checkmarxComment Additional comment(s) to include in the scan results
 * @param useOwnServerCredentials If set to false then credentials from the Manage Jenkins page are used; serverUrl, username and password parameters are ignored. 
 * If set to true then credentials must be specified in serverUrl, username and password parameters
 *
 * @see <a href="https://github.com/imuchnik/jenkins-automation/blob/gh-pages/docs/examples.md#bdd-security-job-builder" target="_blank">TODO Checkmarx job Example</a>
 *
 */

class CheckmarxSecurityJobBuilder {

    String name
    String description
    String scanRepo
    String checkmarxComment
    Boolean useOwnServerCredentials
    String serverUrl
    String username
    String password
    String groupId
    Boolean vulnerabilityThresholdEnabled
    Boolean cleanWorkspace
    String highThreshold
    String mediumThreshold
    String lowThreshold

    Job build(DslFactory factory){

        def baseJob = new BaseJobBuilder(
                name: this.name,
                description: this.description,
        ).build(factory)

        baseJob.with{
                scm {
                    git{
                        remote{
                            name('Repo to Scan')
                            url(scanRepo)
                        }
                        branch('*/master')
                        if (cleanWorkspace){
                            clean()   
                        }
                        
                    }
                }
        }

        baseJob.with{
            configure { project -> 
                project / builders / 'com.checkmarx.jenkins.CxScanBuilder' {
                    'useOwnServerCredentials'(useOwnServerCredentials)
                    'serverUrl'(serverUrl)
                    'username'(username)
                    'password'(password)
                    'projectName'(name) // Checkmarx Project Name
                    'groupId'(groupId)  // Team
                    'preset'('17') // Default 2014
                    'presetSpecified'('false')
                    'excludeFolders'('resources')
                    'filterPattern'('!**/_cvs/**/*, !**/.svn/**/*')
                    'incremental'(true)
                    'fullScansScheduled'(false)
                    'fullScanCycle'('10')
                    'isThisBuildIncremental'(false)
                    'sourceEncoding'('1') // Default Configuration
                    'comment'(checkmarxComment)
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
