import jenkins.automation.builders.PipelineJobBuilder


def script = """
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
        pipelineScript: script,
        sandboxFlag: false
).build(this).with {
    logRotator {
        numToKeep(365)
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
).build(this)
.with {
    logRotator {
        numToKeep(365)
    }
}

