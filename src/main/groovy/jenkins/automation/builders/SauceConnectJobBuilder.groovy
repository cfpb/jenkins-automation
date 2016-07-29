package jenkins.automation.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job


/**
 * Sauce Connect Job builder creates a default Sauce Connect OnDemand plugin configuration

 *
 * @param name job name
 * @param description job description
 * @param webDriverBrowser browser to use with Sauce Connect
 * @param sauceCredentialId jenkins id to use for the sauce plugin
 *
 */
class SauceConnectJobBuilder {

    String name
    String description
    List<String> emails
    Boolean use_versions = false
    String webDriverBrowser = 'Linuxchrome44'
    String sauceCredentialId

    def artifacts = {
        pattern("dist/")
        fingerprint()
        defaultExcludes()
    }

    /**
     * The main job-dsl script that build job configuration xml
     * @param DslFactory
     * @return Job
     */
    Job build(DslFactory factory) {
        def baseJob = new JsJobBuilder(
                name: this.name,
                description: this.description,
                emails: this.emails,
                use_versions: use_versions,
                artifacts: artifacts
        ).build(factory)

        baseJob.with {
            wrappers {
                sauceOnDemand {
                    enableSauceConnect(true)
                    webDriverBrowsers(webDriverBrowser)
                    verboseLogging(true)
                    useGeneratedTunnelIdentifier(true)
                }
            }
        }

        baseJob.with {
            configure { project ->
                project / buildWrappers / 'hudson.plugins.sauce__ondemand.SauceOnDemandBuildWrapper' << 'credentialId'(sauceCredentialId)
            }
        }

        return baseJob
    }
}
