package jenkins.automation.utils

/**
 * Common block of reusable configure blocks for various plugins support

 */
class PluginUtils {
/**
 *  Utility function for adding newrelic notifier plugin
 * @param context Publishers in this case, passed as a delegate
 * @param apiKey newRelic api key
 * @param applicationId Application Id
 * @param jobName job Name
 * @param changeLog Changelog messages
 * @param user Jenkins user
 * @param revision revision number
 *
 *
 * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#plugin-utils" target="_blank">Plugin utils</a>
 */

    static void addNewRelicSupport(context, apiKey, application_Id, jobName, changeLog, userId, rev) {

        context.configure { Node project ->
            project / publishers << 'jenkinsci.plugins.newrelicnotifier.NewRelicDeploymentNotifier'(plugin: "newrelic-deployment-notifier@1.3") {
                notifications {
                    goo apiKey
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


