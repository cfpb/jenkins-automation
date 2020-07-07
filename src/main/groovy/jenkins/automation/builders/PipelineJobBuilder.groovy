package jenkins.automation.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import jenkins.automation.utils.CommonUtils

/**
 * The basic PipelineJob builder
 *
 * @param stages list of maps describing the stages; see example linked below for the shape of the map
 * @param name job name
 * @param sandboxFlag, boolean to enable/disable sandbox, default true.
 * @param description job description
 * @param pipelineScript optional string containing the raw pipeline script (use instead of `stages`)
 * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#pipeline-builder" target="_blank">Pipeline job builder example</a>
 */

class PipelineJobBuilder {
    List stages
    String name
    String description
    String pipelineScript = ""
    Boolean sandboxFlag = true

    /**
     * @param DSL factory class,  provided by Jenkins when executed from build context
     * @return pipeline job
     */

    Job build(DslFactory factory) {
        factory.pipelineJob(name) {
            it.description this.description
            logRotator {
                numToKeep(30)
            }
            def warningText = ''
            if (stages && pipelineScript) {
                warningText += """
                    ansiColor('xterm') {
                        echo '\\033[31m! `pipelineScript` parameter has no effect because ' +
                        '`stages` was also supplied; choose one or the other\\033[0m'
                    }
                """
            }

            if (stages) {
                def stagesStr = stages.collect {
                    """
                        stage("${it.stageName}") {
                            build job: "${it.jobName}"${
                                it.parameters ? ', parameters: ' + it.parameters : ''
                            }
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
                    sandbox(sandboxFlag)
                    script(pipelineScript)
                }
            }
        }
    }
}
