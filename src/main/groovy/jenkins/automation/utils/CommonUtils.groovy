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
            }
            logRotator {
                numToKeep(30)
            }
            publishers {
                allowBrokenBuildClaiming()
            }
        }
    }

/** Utility function to add extended email
 *
 * @param triggers List<String> triggers E.g Failure, Fixed etc...
 * @param List emails List of email string to make it seamlessly compatible with builders
 * @param sendToDevelopers Default false,
 * @param sendToRequester Default true,
 * @param includeCulprits Default false,
 * @param sendToRecipientList Default true
 *
 * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">BDD job Example</a>
 * */
    static void extendedEmail(context, List<String> triggers = ["Failure", "Fixed"], ArrayList<String> emails, sendToDevelopers = false,  sendToRequester=true, includeCulprits = false, sendToRecipientList = true) {
        context.with {
            extendedEmail(emails.join(",")) {
                triggers.each {
                    trigger(triggerName: it,
                            sendToDevelopers: sendToDevelopers, sendToRequester: sendToRequester, includeCulprits: includeCulprits, sendToRecipientList: sendToRecipientList)

                }
            }
        }

    }

/**
 * Utility function to add extended email
 *
 * @param triggers List<String> triggers E.g Failure, Fixed etc...
 * @param String emails Comma separated string of emails
 * @param sendToDevelopers Default false,
 * @param sendToRequester Default true,
 * @param includeCulprits Default false,
 * @param sendToRecipientList Default true
 *
 * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#common-utils" target="_blank">BDD job Example</a>
 */

    static void extendedEmail(context, List<String> triggers = ["Failure", "Fixed"], String emails, sendToDevelopers = false,  sendToRequester =true,includeCulprits = false, sendToRecipientList = true) {
        context.with {
            extendedEmail(emails) {
                triggers.each {
                    trigger(triggerName: it,
                            sendToDevelopers: sendToDevelopers, sendToRequester: sendToRequester, includeCulprits: includeCulprits, sendToRecipientList: sendToRecipientList)

                }
            }
        }

    }

}

