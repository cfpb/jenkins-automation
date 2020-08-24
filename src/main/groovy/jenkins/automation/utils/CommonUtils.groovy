package jenkins.automation.utils

/**
 * Utils class when most reused common properties should live.
 *  Adds a minimum base functionality required -build claiming, notifications and log.
 * @param context delegate passed in context
 */
class CommonUtils {

    /**
     * Adds bare minimum defaults
     */

    static void addDefaults(context) {
        context.with {
            wrappers {
                colorizeOutput()
                timestamps()
            }
            logRotator {
                numToKeep(30)
            }
            publishers {
                allowBrokenBuildClaiming()
            }
            configure { Node project ->
                project / 'properties' / 'com.sonyericsson.jenkins.plugins.bfa.model.ScannerJobProperty'(plugin: "build-failure-analyzer") {
                    doNotScan 'false'
                }
            }
        }
    }

    /** Utility function to add extended email
     *
     * @param List emails List of email string to make it seamlessly compatible with builders
     * @param triggersList List<String> triggers E.g failure, fixed etc...
     * @param sendToDevelopers Default false,
     * @param sendToRequester Default true,
     * @param includeCulprits Default false,
     * @param sendToRecipientList Default true
     * @param preSendScript Default $DEFAULT_PRESEND_SCRIPT
     * @param content Default is $DEFAULT_CONTENT
     *
     * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">Common utils</a>
     */

    static void addExtendedEmail(context, List<String> emails, List<String> triggerList = ["failure", "unstable", "fixed"], sendToDevelopers = false, sendToRequester = true, includeCulprits = false, sendToRecipientList = true, preSendScript = "\$DEFAULT_PRESEND_SCRIPT", attachmentPattern = "", content="\$DEFAULT_CONTENT", subject = "\$DEFAULT_SUBJECT") {
        addExtendedEmail(context, emails.join(","), triggerList, sendToDevelopers, sendToRequester, includeCulprits, sendToRecipientList, preSendScript, attachmentPattern, content, subject )
    }

    /**
     * Utility function to add extended email
     * @param String emails Comma separated string of emails
     * @param triggerList List<String> triggers E.g failure, fixed etc...
     * @param sendToDevelopers Default false,
     * @param sendToRequester Default true,
     * @param includeCulprits Default false,
     * @param sendToRecipientList Default true
     * @param preSendScript Default $DEFAULT_PRESEND_SCRIPT
     * @param attachmentPattern Ant style pattern matching for attachments
     * @param content Default is $DEFAULT_CONTENT
     * @param subject Default is $DEFAULT_SUBJECT
     *
     * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">Common utils</a>
     */

    static void addExtendedEmail(context, String emails, List<String> triggerList = ["failure", "unstable", "fixed"], sendToDevelopers = false, sendToRequester = true, includeCulprits = false, sendToRecipientList = true, preSendScript = "\$DEFAULT_PRESEND_SCRIPT", attachmentPattern = "", content = "\$DEFAULT_CONTENT", subject = "\$DEFAULT_SUBJECT") {

        context.with {
            extendedEmail {
                delegate.recipientList(emails)
                delegate.preSendScript(preSendScript)
                delegate.attachmentPatterns(attachmentPattern)
                delegate.defaultContent(content)
                delegate.defaultSubject(subject)

                triggers {
                    triggerList.each {
                        "${it}" {
                            sendTo {
                                if (sendToDevelopers) developers()
                                if (sendToRequester) requester()
                                if (includeCulprits) culprits()
                                if (sendToRecipientList) recipientList()
                            }
                        }
                    }
                }
            }

        }
    }

    /**
     *
     * @param context Closure context, i.e delegate
     * @param params Maps of params
     * emails: <String>, please note it does not support ArrayList
     * triggers: <Array> ["failure", "unstable", "fixed"],
     * sendToDevs:<Boolean>,
     * sendToRequester:<Boolean>,
     * includeCulprits:<Boolean>,
     * endToRecipient:<Boolean>,
     * preSendScript = <String>,
     * attachmentPattern = <String>,
     * content = <String>,
     * subject = <String>
     *
     * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">Common utils</a>

     */
    static void addExtendedEmail(Map params, context) {

        params.triggerList = params.triggerList ?: ["failure", "unstable", "fixed"]
        params.sendToDevelopers = params.sendToDevelopers ?: false
        params.sendToRequester = params.sendToRequester ?: true
        params.includeCulprits = params.includeCulprits ?: false
        params.sendToRecipientList = params.sendToRecipientList ?: true
        params.preSendScript = params.preSendScript ?: "\$DEFAULT_PRESEND_SCRIPT"
        params.attachmentPattern = params.attachmentPattern ?: ""
        params.content = params.content ?: "\$DEFAULT_CONTENT"
        params.subject = params.subject ?: "\$DEFAULT_SUBJECT"

        def emails = params.emails

        context.with {
            extendedEmail {
                recipientList(emails)
                preSendScript(params.preSendScript)
                attachmentPatterns(params.attachmentPattern)
                defaultContent(params.content)
                defaultSubject(params.subject)

                triggers {
                    params.triggerList.each {
                        "${it}" {
                            sendTo {
                                if (params.sendToDevelopers) developers()
                                if (params.sendToRequester) requester()
                                if (params.includeCulprits) culprits()
                                if (params.sendToRecipientList) recipientList()
                            }
                        }
                    }
                }
            }

        }
    }

    /**
     * Utility function to add injectGlobalPasswords
     *
     * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">Common utils</a>
     */

    static void addInjectGlobalPasswords(context) {
        context.with {
            configure {
                it / buildWrappers / EnvInjectPasswordWrapper {
                    injectGlobalPasswords(true)
                    maskPasswordParameters(true)
                }
            }
        }
    }

    /**
     * Utility function to add log parser publisher
     *
     * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">Common utils</a>
     */

    static void addLogParserPublisher(context, rulesPath = "/var/lib/jenkins/shell_parse_rules.txt") {
        context.with {
            configure {
                it / publishers << 'hudson.plugins.logparser.LogParserPublisher' {
                    unstableOnWarning true
                    failBuildOnError true
                    parsingRulesPath rulesPath
                }
            }
        }
    }

    /**
     * Utility to add a performance publisher block, for use in performance tests
     *
     * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">Common utils</a>
     */

    static void addPerformancePublisher(context, String reportPattern = "**/results/*.jtl", String unstableResponseTimeThreshold = "", int failedThresholdPositive, int failedThresholdNegative, int unstableThresholdPositive, int unstableThresholdNegative) {
        context.with {
            configure {
                it / publishers << 'hudson.plugins.performance.PerformancePublisher' {
                    errorFailedThreshold 2
                    errorUnstableThreshold 1
                    errorUnstableResponseTimeThreshold unstableResponseTimeThreshold
                    relativeFailedThresholdPositive failedThresholdPositive
                    relativeFailedThresholdNegative failedThresholdNegative
                    relativeUnstableThresholdPositive unstableThresholdPositive
                    relativeUnstableThresholdNegative unstableThresholdNegative
                    modeRelativeThresholds false
                    configType 'ART'
                    modeOfThreshold true
                    compareBuildPrevious true
                    modePerformancePerTestCase true
                    modeThroughput true
                    parsers {
                        'hudson.plugins.performance.JMeterParser' {
                            glob reportPattern
                        }
                    }
                }
            }
        }
    }

    /**
    * Utility to disable concurrent builds in Pipeline jobs
    *
    * As of job-dsl plugin version 1.76, the old `concurrentBuilds(false)` syntax is deprecated and replaced with `disableConcurrentBuilds()`
    * The problem is that disableConcurrentBuilds() is a dynamic method, and thus it will not run via gradle and will only work in seed jobs.
    * This breaks any local usage of `gradlew rest` for running jobs against a local or remote Jenkins server, which currently is a key part of our 
    * development workflow.
    *
    * This static disableCurrentBuilds() method retains the local development workflow while preventing developers from needing to litter
    * their job-dsl scripts with `configure` blocks
    * 
    * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">Common utils</a>
    */
    static void disableConcurrentBuilds(context) {
        context.with {
            configure {
                it / 'properties' / 'org.jenkinsci.plugins.workflow.job.properties.DisableConcurrentBuildsJobProperty' {}
            }
        }
    }

    /**
    * Utility to add usernamePassword credentials binding
    *
    * the usernameVariable... style of usernamePassword{} credentials binding is "dynamic", and thus it will not run via gradle and will only work in seed jobs.
    * This breaks any local usage of `gradlew rest` for running jobs against a local or remote Jenkins server, which currently is a key part of our 
    * development workflow.    
    *
    * This addUsernamePasswordCredentials() method retains the local development workflow while preventing developers from needing to litter
    * their job-dsl scripts with `configure` blocks.
    *
    * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#credentials-binding" target="_blank">Common utils Credentials Binding</a>
    */
    static void addUsernamePasswordCredentials(context, String credentialsId, String usernameVariable, String passwordVariable) {
        context.with {
            configure { Node project ->
                project / 'buildWrappers' / 'org.jenkinsci.plugins.credentialsbinding.impl.SecretBuildWrapper' / 'bindings' << 'org.jenkinsci.plugins.credentialsbinding.impl.UsernamePasswordMultiBinding' {
                    delegate.credentialsId credentialsId
                    delegate.usernameVariable usernameVariable
                    delegate.passwordVariable passwordVariable
                }
            }
        }
    }

    /**
    * Utility to add AWS credentials binding
    *
    * amazonWebServicesCredentialsBinding is a "dynamic" method, and thus it will not run via gradle and will only work in seed jobs.
    * This breaks any local usage of `gradlew rest` for running jobs against a local or remote Jenkins server, which currently is a key part of our 
    * development workflow.    
    *
    * This addAmazonWebServicesCredentials() method retains the local development workflow while preventing developers from needing to litter
    * their job-dsl scripts with `configure` blocks.
    *
    * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#credentials-binding" target="_blank">Common utils Credentials Binding</a>
    */
    static void addAmazonWebServicesCredentials(context, String credentialsId, String accessKeyVariable, String secretKeyVariable) {
        context.with {
            configure { Node project ->
                project / 'buildWrappers' / 'org.jenkinsci.plugins.credentialsbinding.impl.SecretBuildWrapper' / 'bindings' << 'com.cloudbees.jenkins.plugins.awscredentials.AmazonWebServicesCredentialsBinding' {
                    delegate.credentialsId credentialsId
                    delegate.accessKeyVariable accessKeyVariable
                    delegate.secretKeyVariable secretKeyVariable
                }
            }
        }
    }

    /**
     * Common string for creating and activating a python 2.7 virtualenv in a shell block
     *
     * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">Common utils</a>
     */
    static String python27Virtualenv = """
        if [ -d ".env" ]; then
          echo "**> virtualenv exists"
        else
          echo "**> creating python 2.7 virtualenv"
          virtualenv -p /usr/local/bin/python2.7 .env
        fi

        . .env/bin/activate

        if [ -f "requirements.txt" ]; then
            pip install -r requirements.txt
        fi
        """.stripIndent()
}
