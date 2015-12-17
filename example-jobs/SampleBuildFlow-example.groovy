import jenkins.automation.builders.FlowJobBuilder

def exampleFlow = new FlowJobBuilder(
        name: 'GeneratedFlowJob',
        description: 'this our first stab at it',
        jobs:['job1', 'job2']
).build(this);

exampleFlow.with{
  logRotator{
      numToKeep(365)
  }
}

def exampleFlowWithWorkspace = new FlowJobBuilder(
        name: 'flow-job-with-workspace',
        description: 'a build flow job with its own workspace',
        jobs:['job1', 'job2']
).build(this)

exampleFlowWithWorkspace.with{
    scm {
        git('https://github.com/cfpb/qu.git', 'master')
    }

    configure { node ->
        node << buildNeedsWorkspace(true)
    }

}

