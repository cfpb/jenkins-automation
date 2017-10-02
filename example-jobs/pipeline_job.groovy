import jenkins.automation.builders.PipelineJobBuilder

def script = """
             node {
                stage('First stage') {
                    build job: 'Job 1',
                    parameters: [
                        [\$class: 'StringParameterValue', name: 'foo', value: 'bar'],
                    ]
                }
            }
"""

def pipelineJob = new PipelineJobBuilder(
    name: 'Pipeline builder',
    description: 'This is a simple pipeline job',
    pipelineScript: script,
    emails: ['jane@example.com', 'joe@example.com']

).build(this);

pipelineJob.with {
    logRotator {
        numToKeep(365)
    }
}

def pipelineJobStages = new PipelineJobBuilder(
        name: 'Pipeline builder',
        description: 'this is a simple pipeline job',
        stages: [[
                         stageName: 'First stage',
                         jobName: 'Job 1',
                         parameters: "[[\$class: 'StringParameterValue', name: 'foo', value: 'bar']]"
                 ],
                 [
                         stageName: 'Second stage',
                         jobName: 'Job 2',
                 ]],
        emails: ['jane@example.com', 'joe@example.com']
).build(this);

ppipelineJobStages.with {
    logRotator {
        numToKeep(365)
    }
}

