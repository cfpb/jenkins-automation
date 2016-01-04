package jenkins.automation.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import jenkins.automation.builders.BaseJobBuilder

/**
 * Run jobs with a set cron schedule
 *
 * @param name  job name
 * @param description  job description
 * @param emails  list of developers to get notifications
 * @param cronSchedule  cron-style task schedule definition
 * @param repo  github URL to clone into path (optional)
 * @see <a href="https://github.com/imuchnik/jenkins-automation/blob/gh-pages/docs/examples.md#flow-job-job-builder" target="_blank">Flow job builder example</a>

 *
 */
class PeriodicJobBuilder {

    String name
    String description
    List<String> emails
    String cronSchedule = 'H/15 * * * *'
    String repo

    Job build(DslFactory factory) {

        def baseJob = new BaseJobBuilder(
                name: this.name,
                description: this.description,
                emails: this.emails,
        ).build(factory)

        baseJob.with {
            triggers {
                if(cronSchedule) {
                    cron(cronSchedule)
                }
            }
        }

        baseJob.with {
            scm {
                if(repo) {
                    git {
                        remote {
                            url(repo)
                        }
                        branch('*/master')
                    }
                }
            }
        }

        return baseJob
    }
}

