package jenkins.automation.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

/**
 * The basic Flow Job builder
 *
 * @param name  job name
 * @param description  job description
 * @param jobs  optional comma separated list of jobs to include in the build flow
 * @param jobFlow optional string that can contain arbitrary text for the Flow field
 * @see <a href="https://github.com/imuchnik/jenkins-automation/blob/gh-pages/docs/examples.md#flow-job-job-builder" target="_blank">Flow job builder example</a>

 *
 */
class FlowJobBuilder {
    List<String> jobs
    String name
    String description
    String jobFlow = null

    /**
     * @param DLS factory class,  provided by Jenkins when executed from build context
     * @return flow job
     */
    Job build(DslFactory factory){
        factory.buildFlowJob(name){
            it.description this.description

            String jobsToBuild =""

            jobs.each { jobName ->
              jobsToBuild += "build('${jobName}') \r\n"
            }
            jobsToBuild += jobFlow

            buildFlow(jobsToBuild)
        }
    }


}
