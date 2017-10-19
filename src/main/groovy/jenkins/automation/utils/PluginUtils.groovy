package jenkins.automation.utils

/**
 * Common block of reusable configure blocks for various plugins support

 */
class PluginUtils {

    /**
     *
     * @param context delegate
     * @param SqsEndpoint AWS SQS queue url
     * @param buildServerUrlValue jenkins url
     * @param roomId optional (will read from global config if blank) used by tools like https://github.com/catops/hubot-sqs
     * @param customSQSMessageValue Custom message to be posted to AWS SQS queue.
     * @param includeCustomSQSMessageFlag Option to include  custom message. Default value: false
     * @param startNotificationFlag Option to be notified when job starts. Default value: false
     * @param notifySuccessFlag Option to be notified when job succeeds. Default value: false
     * @param notifyAbortedFlag Option to be notified when job is aborted. Default value: false
     * @param notifyNotBuiltFlag Option to be notified when job is not built. Default value: false
     * @param notifyUnstableFlag Option to be notified when job is unstable. Default value: false
     * @param notifyFailureFlag Option to be notified when job fails. Default value: true
     * @param notifyBackToNormalFlag Option to be notified when job returns to SUCCESS state. Default value: false
     * @param notifyRepeatedFailureFlag Option to be notified when job fails repeatedly. Default value: false
     * @param sqsIncludeTestSummaryFlag Option to include test summary. Default value: false

     * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#sqs" target="_blank">SQS Support</a>

     */
    static void addSQSNotification(context,
                                   SqsEndpoint,
                                   buildServerUrlValue,
                                   roomId,
                                   customSQSMessageValue = "",
                                   includeCustomSQSMessageFlag = false,
                                   startNotificationFlag = false,
                                   notifySuccessFlag = false,
                                   notifyAbortedFlag = false,
                                   notifyNotBuiltFlag = false,
                                   notifyUnstableFlag = false,
                                   notifyFailureFlag = true,
                                   notifyBackToNormalFlag = false,
                                   notifyRepeatedFailureFlag = false,
                                   sqsIncludeTestSummaryFlag = false) {

        context.configure { Node project ->
            project / publishers << 'jenkins.plugins.sqs.SQSNotifier'(plugin: "sqs-notification") {
                endpoint(SqsEndpoint)
                buildServerUrl buildServerUrlValue
                room(roomId)
                startNotification startNotificationFlag
                notifySuccess notifySuccessFlag
                notifyAborted notifyAbortedFlag
                notifyNotBuilt notifyNotBuiltFlag
                notifyUnstable notifyUnstableFlag
                notifyFailure notifyFailureFlag
                notifyBackToNormal notifyBackToNormalFlag
                notifyRepeatedFailure notifyRepeatedFailureFlag
                sqsIncludeTestSummary sqsIncludeTestSummaryFlag
                includeCustomSQSMessage includeCustomSQSMessageFlag
                customSQSMessage customSQSMessageValue

            }
        }
    }

    static /**
     *  Utility function for adding newrelic notifier plugin
     * @param context Publishers in this case, passed as a delegate
     * @param api_key newRelic api key
     * @param applicationId Application Id
     * @param jobName job Name
     * @param changeLog Changelog messages
     * @param user Jenkins user
     * @param revision revision number
     *
     *
     * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#new-relics" target="_blank">New Relic</a>
     */
    void addNewRelicSupport(context, api_key, application_Id, jobName, changeLog, userId, rev) {

        context.configure { Node project ->
            project / publishers << 'org.jenkinsci.plugins.newrelicnotifier.NewRelicDeploymentNotifier'(plugin: "newrelic-deployment-notifier") {

                client(class: "org.jenkinsci.plugins.newrelicnotifier.api.NewRelicClientImpl")
                notifications {
                    'org.jenkinsci.plugins.newrelicnotifier.DeploymentNotificationBean' {
                        apiKey api_key
                        applicationId application_Id
                        description jobName
                        revision rev
                        changelog changeLog
                        user userId
                    }
                }
            }
        }
    }

    /**
     *
     * @param context Project passed from root of DSL
     * @param triggerLabel Restricted node label
     * @param triggerQuietPeriod Delay before starting job

     * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#using-a-startup-trigger" target="_blank">example</a>

     */
    static void startupTrigger(context,
                               String triggerLabel = 'master',
                               int triggerQuietPeriod = 0) {
        context.configure { Node project ->
                  project / 'triggers' / 'org.jvnet.hudson.plugins.triggers.startup.HudsonStartupTrigger'(plugin: "startup-trigger-plugin") {
                      spec()
                      label(triggerLabel)
                      quietPeriod(triggerQuietPeriod)
            }
        }
    }
}

