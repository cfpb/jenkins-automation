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
     *
     * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">Common utils</a>
     */

    static void addExtendedEmail(context, List<String> emails, List<String> triggerList = ["failure", "unstable", "fixed"], sendToDevelopers = false, sendToRequester = true, includeCulprits = false, sendToRecipientList = true, preSendScript = "\$DEFAULT_PRESEND_SCRIPT", attachmentPattern = "") {
        addExtendedEmail(context, emails.join(","), triggerList, sendToDevelopers, sendToRequester, includeCulprits, sendToRecipientList, preSendScript, attachmentPattern)
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
     *
     * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">Common utils</a>
     */

    static void addExtendedEmail(context, String emails, List<String> triggerList = ["failure", "unstable", "fixed"], sendToDevelopers = false, sendToRequester = true, includeCulprits = false, sendToRecipientList = true, preSendScript = "\$DEFAULT_PRESEND_SCRIPT", attachmentPattern = "") {
        context.with {
            extendedEmail {
                delegate.recipientList(emails)
                delegate.preSendScript(preSendScript)
                delegate.attachmentPatterns(attachmentPattern)

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
     *  @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">Common utils</a>
     */
    static void addExtendedEmail(Map params, context) {

        params.triggerList ?: ["failure", "unstable", "fixed"]
        params.sendToDevelopers ?: false
        params.sendToRequester ?: true
        params.includeCulprits ?: false
        params.sendToRecipientList ?: true
        params.preSendScript ?: "\$DEFAULT_PRESEND_SCRIPT"
        params.attachmentPattern ?: ""
        def  emails = params.emails
        print emails
        context.with {
            extendedEmail {
                recipientList(emails)
                preSendScript(params.preSendScript)
                attachmentPatterns(params.attachmentPattern)

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
     * Common string for creating and activating a python 2.7 virtualenv in a shell block
     *
     * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">Common utils</a>
     */
    static String python27Virtualenv = """
        if [ -d ".env" ]; then
          echo "**> virtualenv exists"
        else
          echo "**> creating virtualenv"
          virtualenv -p /usr/local/bin/python2.7 .env
        fi

        . .env/bin/activate

        if [ -f "requirements.txt" ]; then
            pip install -r requirements.txt
        fi
        """.stripIndent()
}
