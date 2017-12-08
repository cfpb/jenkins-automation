package jenkins.automation.utils

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
 * @param  ghProject A reference to the GHE project as 'username/reponame'
 * @param  ghHostname The GitHub hostname, i.e. github.org.tld, or github.com
 * @param  ghAuthId the Jenkins credential ID for GitHub authentication
 *         see <a href="https://github.com/jenkinsci/ghprb-plugin/blob/master/README.md#credentials">the plugin docs</a>
 * @param  ghPermitAll  optional Whether or not to allow all contributors to build a PR
 * @param  ghPrHooks  optional Whether or not to install webhooks in the remote repo
 * @param  ghPrCron  optional For use when ghPrHooks is false. Uses standard cron syntax
 * @param  ghPrOrgList  optional A string of organizations to whitelist for PRs
 */
    static void ghPrWatcher(context,
                            String ghProject,
                            String ghHostname,
                            String ghAuthId,
                            Boolean ghPermitAll = false,
                            Boolean ghPrHooks = true,
                            String ghPrCron = '',
                            String ghPrOrgsList = 'jenkins'
    ) {
        context.with {
            scm {
                git {
                    remote {
                        github(ghProject, 'https', ghHostname)
                        refspec('+refs/pull/*:refs/remotes/origin/pr/*')
                    }
                    branch('${sha1}')
                }
            }
            configure { node ->
                node / 'triggers' / 'org.jenkinsci.plugins.ghprb.GhprbTrigger'( plugin: 'ghprb') {
                    if (ghPrCron) {
                        spec(ghPrCron)
                        cron(ghPrCron)
                    }
                    else {
                        spec()
                        cron()
                    }
                    allowMembersOfWhitelistedOrgsAsAdmin(true)
                    orgslist(ghPrOrgsList)
                    permitAll(ghPermitAll)
                    useGitHubHooks(ghPrHooks)
                    gitHubAuthId(ghAuthId)
                }
            }
        }
    }
}
