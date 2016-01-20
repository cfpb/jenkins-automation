import jenkins.automation.builders.FlowJobBuilder

def flowJob = new FlowJobBuilder(
        name: 'GeneratedFlowJob',
        description: 'this our first stab at it',
        jobs:['job1', 'job2'],
        emails:['joe@joe.com', 'jane@jane.com']
).build(this);

flowJob.with{
    logRotator{
        numToKeep(365)
    }
}


def customFlowJob = new FlowJobBuilder(
        name: 'GeneratedCustomFlowJob',
        description: 'this a custom flow job',
        jobFlow: """
                build('job1')
                build('job2')
                parallel(
                 { build('job3') },
                 { build('job4') },
                )
            """
).build(this);


customFlowJob.with{
    logRotator{
        numToKeep(365)
    }
}

def customFlowJobWithWorkspace = new FlowJobBuilder(
        name: 'flow-job-with-workspace',
        description: 'a build flow job with its own workspace',
        jobs:['job1', 'job2']
).build(this)

customFlowJobWithWorkspace.with{
    scm {
        git('https://github.com/cfpb/qu.git', 'master')
    }

    configure { node ->
        node << buildNeedsWorkspace(true)
    }
}
