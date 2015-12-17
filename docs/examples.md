#Examples

## BDD Security Job Builder

```
import jenkins.automation.builders.BddSecurityJobBuilder
   
   def projectName ='foo'
   new BddSecurityJobBuilder(
           name: "${projectName}bdd_security_job",
           description: "Sample bdd security job",
           baseUrl: "http://google.com"
   ).build(this);
   

```

## Base Job Builder


```
import jenkins.automation.builders.BaseJobBuilder
   
   def baseJob = new BaseJobBuilder(
                   name: this.name,
                   description: this.description,
                   emails: this.emails 
   ).build(factory)

```

## Flow Job Builder


```
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
            description: 'this a custom flow it',
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

```

## JS Build Job


```

import jenkins.automation.builders.JsJobBuilder


String basePath = 'JsJobSamples'
List developers = ['irina.muchnik@cfpb.gov', 'daniel.davis@cfpb.gov']

def repos = [
        [name: 'jenkins-automation', url: "https://github.com/cfpb/jenkins-automation@2.0"],
        [name: 'collab', url: "https://github.com/cfpb/jenkins-automation"]
]
folder(basePath) {
    description 'This example shows how to create jobs using Job builders.'
}


new JsJobBuilder(
        name: "$basePath/BuilderVsBuilders",
        description: 'An example using a job builder for a Javascript build jobs project.',
        repos: repos,
        emails: developers,
        use_versions: true
).build(this)

```
## Using MultiScm Utility 

```

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job
import jenkins.automation.builders.BaseJobBuilder
import jenkins.automation.utils.ScmUtils

 def baseJob = new BaseJobBuilder(
                name: this.name,
                description: this.description,
                emails: this.emails
        ).build(factory)


 baseJob.with {
    multiscm {
        ScmUtils.project_repos(delegate, this.repos, use_versions)
        }
    }

  baseJob
 }

```