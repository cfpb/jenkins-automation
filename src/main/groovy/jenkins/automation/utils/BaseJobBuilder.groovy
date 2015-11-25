package jenkins.automation.utils

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

/**
 * The very first and basic building block
 *
 * <p>
 *      creates a job with colorized input,
 *      log rotator, email notifications and build claiming
 * </p>
 * @param name  used to name the job
 * @param description   job description
 * @param emails  list of developer to get notifications
 *<p>
 *
 * @see <a href="https://github.com/imuchnik/jenkins-automation/blob/gh-pages/docs/examples.md#base-job-job-builder"
 *      target="_blank">Base job builder example</a>

 * </p>
 */
class BaseJobBuilder {
    String name
    String description
    List<String> emails

    Job build(DslFactory factory){
        factory.job(name){
            it.description this.description
            addBaseStuff(delegate,this.emails)
        }
    }

/**
 *  Adds colorized input plugin support
 * @param context
 */
    static void addColorizeOutput(context){

        context.with{
            colorizeOutput()
        }
    }

/**
 *  Adds a minimum base functionality required -build claiming, notifications and log.
 * @param context  delegate passed in context
 * @param emails  email list of the developers for notification
 */
    static void addBaseStuff(context, emails) {
        context.with{
            wrappers{
                addColorizeOutput(delegate)
            }
            logRotator {
                //TODO: make it parametrized to builder
                numToKeep(10)
            }
            publishers{
                allowBrokenBuildClaiming()

                if(emails){
                    mailer emails.join(' ')  //TODO use extended email
                }
            }
        }

    }
}
