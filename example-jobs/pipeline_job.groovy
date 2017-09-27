import jenkins.automation.builders.PipelineJobBuilder

def script = """

node { stage 'Export latest task'
build job: 'cf.gov-ansible-data-tasks',
 parameters: [ [ \$class: 'StringParameterValue', name: 'EXPORT_ENV', value: EXPORT_ENV ],
    [ \$class: 'StringParameterValue', name: 'IMPORT_ENV', value: IMPORT_ENV ],
    [ \$class: 'StringParameterValue', name: 'TRANSFER_TASK', value: TRANSFER_TASK ] ]}

"""
def pipelineJob = new PipelineJobBuilder(
        name: 'Pipeline builder',
        description: 'this is a simple pipeline job',
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
        pipelineScript: script,
        stages: [[
                     jobName: "'cf.gov-ansible-data-tasks'",
                     parameters: """[
                             [\$class: 'StringParameterValue', name: 'EXPORT_ENV', value: EXPORT_ENV ],
                                   [\$class: 'StringParameterValue', name: 'IMPORT_ENV', value: IMPORT_ENV ],
                                   [\$class: 'StringParameterValue', name: 'TRANSFER_TASK', value: TRANSFER_TASK ]
                     ]""",
                     stageName: "'Export latest task'"
                 ]],
                 emails: ['jane@example.com', 'joe@example.com']
                ).build(this);

                 pipelineJobStages.with {
                     logRotator {
                         numToKeep(365)
                     }
                 }

