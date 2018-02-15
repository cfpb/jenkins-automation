package jenkins.automation.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job


/**
 * Sauce Connect Job builder creates a default Sauce Connect OnDemand plugin configuration

 *
 * @param name job name
 * @param description job description
 * @param webDriverBrowser (Optional) browser to use with Sauce Connect
 * @param sauceCredentialId SauceCredential to use for the sauce plugin. Note that when using Gradle build this parameter does not get populated.
 * However, this parameter is correctly populated when built inside a Jenkins environment.
 * @param additionalOptions (Optional) additional option to use e.g '-v'
 *
 */
class SauceConnectJobBuilder {

    String name
    String description
    List<String> emails
    String webDriverBrowser = 'Linuxchrome44'
    String sauceCredentialId
    String additionalOptions

    /**
     * The main job-dsl script that build job configuration xml
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
            wrappers {
                sauceOnDemand {
                    enableSauceConnect(true)
                    webDriverBrowsers(webDriverBrowser)
                    verboseLogging(true)
                    useGeneratedTunnelIdentifier(true)
                    credentials(sauceCredentialId)
                    additionalOptions ? options (additionalOptions) : null
                }
            }
        }
        return baseJob
    }
}
