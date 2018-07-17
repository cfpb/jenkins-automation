package jenkins.automation.utils

import java.util.Map

/**
 * Utility class for internally hosted GitHub Enterprise interaction
 */

class GhUtils {

/**
 *
 * Utility method to watch a GitHub Enterprise or GitHub.com project and trigger on PR creation.<br/>
 * This is a wrapper for the <a href="https://wiki.jenkins.io/display/JENKINS/GitHub+pull+request+builder+plugin">GitHub Pull Request Builder Plugin</a>.
 * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#using-gh-pr-watcher" target="_blank">example</a>
 * @param  context delegate
 * @param  ghPrConfig A <code>Map</code> of configuration for the PR builder.
                      Valid keys:
 * <br/>&#x2014;  <code>ghProject</code> The GitHub project name, e.g. username/reponame
 * <br/>&#x2014;  <code>ghHostname</code> The GitHub hostname, i.e. github.org.tld,
 *                or github.com
 * <br/>&#x2014;  <code>ghAuthId</code> the Jenkins credential ID for GitHub authentication
 *                see <a href="https://github.com/jenkinsci/ghprb-plugin/blob/master/README.md#credentials">the plugin docs</a>
 * <br/>&#x2014;  <code>ghPermitAll</code>  optional Whether or not to allow all
 *                contributors to build a PR. Defaults to false.
 * <br/>&#x2014;  <code>ghPrHooks</code>  optional Whether or not to install webhooks
 *                in the remote repo. Defaults to true.
 * <br/>&#x2014;  <code>ghPrCron</code>  optional For use when ghPrHooks is false.
 *                Uses standard cron syntax.
 * <br/>&#x2014;  <code>ghPrOrgList</code>  optional A string of organizations to whitelist for
 *                PRs. Defaults to 'jenkins'.
 * <br/>&#x2014;  <code>ghPrStatusContext</code>  optional A context for this job's tests as it
 *                will appear on PR issues in GH's UI. Defaults to 'Tests from GitHub PR Builder.'
 * <br/>&#x2014;  <code>ghPrResultMessage</code>  optional A <code>Map</code> whose keys are the build
 *                result (SUCCESS, FAILURE, or ERROR) and value is the message posted to GH as a result.
 */
    static void ghPrWatcher(context, Map ghPrConfig) {
        def defaults = ghPrConfigDefaults
        context.with {
            scm {
                git {
                    remote {
                        github(
                            ghPrConfig.get('ghProject'),
                            'https',
                            ghPrConfig.get('ghHostname')
                        )
                        refspec('+refs/pull/*:refs/remotes/origin/pr/*')
                    }
                    branch('${sha1}')
                }
            }
            configure { node ->
                node / 'triggers' / 'org.jenkinsci.plugins.ghprb.GhprbTrigger'( plugin: 'ghprb') {
                    // Avoid 'null' in these blocks
                    spec(ghPrConfig.get('ghPrCron', ''))
                    cron(ghPrConfig.get('ghPrCron', ''))
                    
                    allowMembersOfWhitelistedOrgsAsAdmin(true)
                    orgslist(ghPrConfig.get('ghPrOrgsList', defaults.ghPrOrgsList))
                    permitAll(ghPrConfig.get('ghPermitAll', defaults.ghPermitAll))
                    useGitHubHooks(ghPrConfig.get('ghPrHooks', defaults.ghPrHooks))
                    gitHubAuthId(ghPrConfig.get('ghAuthId'))
                    extensions {
                        'org.jenkinsci.plugins.ghprb.extensions.status.GhprbSimpleStatus' {
                            commitStatusContext(
                                ghPrConfig.get(
                                    'ghPrStatusContext',
                                    defaults.ghPrStatusContext
                                )
                            )
                            triggeredStatus()
                            startedStatus()
                            statusUrl()
                            addTestResults(true)
                            def ghPrMessage = ghPrConfig.get(
                                'ghPrResultMessage',
                                defaults.ghPrResultMessage
                            )
                            def confCompletedStatus = completedStatus()
                            ghPrMessage.each{ buildStatus, ghMessage ->
                                confCompletedStatus << 'org.jenkinsci.plugins.ghprb.extensions.comments.GhprbBuildResultMessage' {
                                    delegate.message(ghMessage)
                                    delegate.result(buildStatus)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

  static Map ghPrConfigDefaults = [
      ghPermitAll: false,
      ghPrHooks: true,
      ghPrOrgsList: 'jenkins',
      ghPrStatusContext: 'Tests from GitHub PR Builder',
      ghPrResultMessage: [ 'SUCCESS': 'Tests from PR builder completed successfully' ],
  ]
}
