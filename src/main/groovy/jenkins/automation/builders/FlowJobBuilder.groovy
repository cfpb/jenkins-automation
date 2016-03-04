package jenkins.automation.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import jenkins.automation.utils.CommonUtils

/**
 * The basic Flow Job builder
 *
 * @param name job name
 * @param description job description
 * @param jobs optional comma separated list of jobs to include in the build flow
 * @param jobFlow optional string that can contain arbitrary text for the Flow field
 * @param pollScmSchedule optional string in cron format to trigger builds on a scheduled interval
 * @see <a href="https://github.com/imuchnik/jenkins-automation/blob/gh-pages/docs/examples.md#flow-job-job-builder" target="_blank">Flow job builder example</a>
 *
 */

class FlowJobBuilder {
    List<String> jobs
    String name
    String description
    String jobFlow = ""
    String pollScmSchedule
    List<String> emails

    /**
     * @param DLS factory class,  provided by Jenkins when executed from build context
     * @return flow job
     */
    Job build(DslFactory factory) {
        factory.buildFlowJob(name) {
            it.description this.description
            buildNeedsWorkspace()
            CommonUtils.addDefaults(delegate)
            publishers {
                if (emails) {
                    publishers {
                        CommonUtils.addExtendedEmail(delegate, emails)
                    }
                }
            }

            if (pollScmSchedule) {
                triggers {
                    scm pollScmSchedule
                }
            }

            String jobsToBuild = ""

            jobs.each { jobName ->
                jobsToBuild += "build('${jobName}') \r\n"
            }
            jobsToBuild += jobFlow

            buildFlow(jobsToBuild)

            configure {
                it / publishers << 'org.zeroturnaround.jenkins.flowbuildtestaggregator.FlowTestAggregator' {
                    showTestResultTrend true
                }
            }
        }
    }
}
