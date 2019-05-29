package jenkins.automation.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import jenkins.automation.builders.BaseJobBuilder

/**
 * @author  Mark Esher - you need to add docs and an example
 */
class SiteMonitorJobBuilder {

    String name
    String description
    List<String> emails
    String cronSchedule = 'H/15 * * * *'

    List<String> urls

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

            configure { project ->
                project / publishers << 'hudson.plugins.sitemonitor.SiteMonitorRecorder' {
                    mSites {
                        urls.each { url ->
                            'hudson.plugins.sitemonitor.model.Site' {
                                mUrl url
                                admitInsecureSslCerts false
                            }
                        }
                    }
                }
            }

        }

        return baseJob
    }
}

