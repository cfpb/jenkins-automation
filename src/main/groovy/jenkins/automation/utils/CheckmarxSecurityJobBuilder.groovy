package jenkins.automation.utils

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job


class CheckmarxSecurityJobBuilder {

    String name
    String description
    String baseUrl
    String username
    String scanRepo
    String checkmarxComment
    Boolean useOwnServerCredentials
    Boolean vulnerabilityThresholdEnabled
    String highThreshold
    String mediumThreshold
    String lowThreshold
    String serverUrl
    String groupId

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
                        clean()
                    }
                }
        }

        baseJob.with{
            configure { project -> 
                project / builders / 'com.checkmarx.jenkins.CxScanBuilder' {
                    'useOwnServerCredentials'(useOwnServerCredentials)
                    'serverUrl'(serverUrl)
                    'username'(username)
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
