package jenkins.automation.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import jenkins.automation.utils.CommonUtils

/**
 * Created by muchniki on 9/27/17.
 */
/**
 * The basic PipelineJob builder
 *
 * @param name job name
 * @param description job description
 * @param stage optional comma separated list of stages to include in the pipeline
 * @param script optional string that can contain arbitrary text for the pipeline script definition
 * @param pollScmSchedule optional string in cron format to trigger builds on a scheduled interval
 * @param config optional string with other configurations
 * @see <a href="https://github.com/imuchnik/jenkins-automation/blob/gh-pages/docs/examples.md#flow-job-job-builder" target="_blank">Flow job builder example</a>
 *
 */

class PipelineJobBuilder {
    List stages
    String name
    String description
    String pipelineScript = ""
    String pollScmSchedule
    List<String> emails

    /**
     * @param DSL factory class,  provided by Jenkins when executed from build context
     * @return pipeline job
     */

    Job build(DslFactory factory) {
        factory.pipelineJob(name) {
            it.description this.description
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
            if (stages){
                def constructedScript =""
                stages.each{stage->
                   def stageString=""" node {
                        stage ${stage.stageName}
                        build job: ${stage.jobName}{
                            parameters: ${stage.parameters}
                    }
                """
                    constructedScript+=stageString+'\n'

                }
                pipelineScript=constructedScript
            }
            definition {
                cps {
                    sandbox(false)
                    script(pipelineScript)
                }
            }
        }
    }
}

//n

