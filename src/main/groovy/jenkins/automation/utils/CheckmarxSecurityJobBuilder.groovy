package jenkins.automation.utils

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job


class CheckmarxSecurityJobBuilder {

    String name
    String description
    String baseUrl
    String scanRepo
    String checkmarxComment
    Boolean vulnerabilityThresholdEnabled
    String highThreshold
    String mediumThreshold
    String lowThreshold

    Job build(DslFactory factory){
        factory.job(name){
            it.description this.description
            
            scm {
                git {
                    remote {
                        name('Repo to Scan')
                        url(scanRepo)
                    }
                    
                    branch('*/master')
                    wipeOutWorkspace(true)
                }
            }

            configure { project -> 
                project / builders / 'com.checkmarx.jenkins.CxScanBuilder' (plugin:"checkmarx@7.2.3-31") {
                    'useOwnServerCredentials'('false')
                    'serverUrl'('http://awe-codescan-w-d01')
                    'projectName'('CheckmarxBuilder')
                    'groupId'('ac43cb0d-034d-4b1e-9bf5-e7c1c46f71d2')  // Team
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
    }
}
