package jenkins.automation.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import javaposse.jobdsl.dsl.*
import jenkins.automation.utils.CommonUtils

/**
 * The very first and basic building block
 *
 * <p>
 *      creates a job with colorized input,
 *      log rotator, email notifications and build claiming
 * </p>
 * @param name used to name the job
 * @param description job description
 * @param emails list of developer to get notifications
 * @param preSendScript a groovy script to modify default email before sending it
 * <p>
 *
 * @see <a href="https://github.com/imuchnik/jenkins-automation/blob/gh-pages/docs/examples.md#base-job-job-builder"
 *      target="_blank">Base job builder example</a>

 * </p>
 */
class BaseJobBuilder {
    String name
    String description
    List<String> emails
    String preSendScript

    Job build(DslFactory factory) {
        factory.job(name) {
            it.description this.description
            CommonUtils.addDefaults(delegate)
            publishers {
                if (emails) {
                    if (preSendScript) {
                        publishers {
                            CommonUtils.addExtendedEmail(delegate, emails, false, true, false, true, preSendScript)
                        }
                    } else {
                        publishers {
                            CommonUtils.addExtendedEmail(delegate, emails)
                        }
                    }
                }
            }
        }
    }
}



