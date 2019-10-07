import jenkins.automation.builders.PipelineJobBuilder
import jenkins.automation.utils.CommonUtils


def pipelineScript = """
    pipeline {
        agent { label 'master' }
        stages {
            stage('hello') {
                steps {
                    sh 'echo "Hello World"'
                }
            }
        }
    }
"""

new PipelineJobBuilder(
        name: 'Hello Pipeline With Script',
        description: 'This is a simple pipeline job',
        pipelineScript: pipelineScript,
        sandboxFlag: false
).build(this).with {
    logRotator {
        numToKeep(365)
    }
}


new PipelineJobBuilder(
        name: 'Hello non-concurrent Pipeline job with builder',
        description: 'This is a simple pipeline job that disables concurrent builds',
        pipelineScript: pipelineScript,
        sandboxFlag: false
).build(this).with {
    logRotator {
        numToKeep(365)
    }
    CommonUtils.disableConcurrentBuilds(delegate)
}

def simplePipeline = pipelineJob('Hello non-concurrent Pipeline job') {
    description('This is a simple pipeline job using straight job-dsl instead of a builder that disables concurrent builds')
    CommonUtils.disableConcurrentBuilds(delegate)
    definition {
        cps {
            script(pipelineScript)
        }
    }
}

new PipelineJobBuilder(
        name: 'Pipeline builder with stages',
        description: 'this is a simple pipeline job',
        stages: [[
                         stageName : 'First stage',
                         jobName   : 'Job 1',
                         parameters: "[[\$class: 'StringParameterValue', name: 'foo', value: 'bar']]"
                 ],
                 [
                         stageName: 'Second stage',
                         jobName  : 'Job 2',
                 ]]
).build(this).with {
    logRotator {
        numToKeep(365)
    }
}

