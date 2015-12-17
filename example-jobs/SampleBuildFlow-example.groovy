import jenkins.automation.builders.FlowJobBuilder

def flowJob= new FlowJobBuilder(
        name: 'GeneratedFlowJob',
        description: 'this our first stab at it',
        jobs:['job1', 'job2']
).build(this);

flowJob.with{
    logRotator{
        numToKeep(365)
    }

}

def customFlowJob= new FlowJobBuilder(
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


