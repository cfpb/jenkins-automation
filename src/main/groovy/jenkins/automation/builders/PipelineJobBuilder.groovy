package jenkins.automation.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import jenkins.automation.utils.CommonUtils


/**
 * The basic PipelineJob builder
 *
 * @param stages list of maps describing the stages; see example linked below for the shape of the map
 * @param name job name
 * @param description job description
 * @param pipelineScript optional string containing the raw pipeline script (use instead of `stages`)
 * @param pollScmSchedule optional string in cron format to trigger builds on a scheduled interval
 * @param emails list of email addresses to receive notifications
 * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#TODOXXXXXXXXXXXX" target="_blank">Pipeline job builder example</a>
 */

class PipelineJobBuilder {
    List<Map> stages
    String name
    String description
    String pipelineScript = ""
    String pollScmSchedule
    List<String> emails

    /**
     * @param DSL factory class, provided by Jenkins when executed from build context
     * @return pipeline job
     */

    Job build(DslFactory factory) {
        factory.pipelineJob(name) {

            def warningText = ''
            if (stages && pipelineScript) {
                warningText += """
                    ansiColor('xterm') {
                        echo '\\033[31m! `pipelineScript` param has no effect because ' +
                        '`stages` was also supplied; choose one or the other\\033[0m'
                    }
                """
            }

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

            if (stages) {
                def stagesStr = stages.collect {
                    """
                        stage("${it.stageName}") {
                            build job: "${it.jobName}"
                            ${it.parameters ? 'parameters: ' + it.parameters : ''}
                        }
                    """
                }.join("\n")
                pipelineScript = """
                    node {
                        ${warningText}
                        ${stagesStr}
                    }
                """.stripIndent()
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
