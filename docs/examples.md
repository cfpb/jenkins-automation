#Examples

## BDD Security Job Builder

```
import jenkins.automation.utils.BddSecurityJobBuilder
   
   def projectName ='foo'
   new BddSecurityJobBuilder(
           name: "${projectName}bdd_security_job",
           description: "Sample bdd security job",
           baseUrl: "http://google.com"
   ).build(this);
   

```

## Base Job Builder


```
import jenkins.automation.utils.BaseJobBuilder
   
   def baseJob = new BaseJobBuilder(
                   name: this.name,
                   description: this.description,
                   emails: this.emails 
   ).build(factory)

```

## Flow Job Builder


```
import jenkins.automation.utils.FlowJobBuilder

    def oahMaster= new FlowJobBuilder(
            name: 'GeneratedFlowJob',
            description: 'this our first stab at it',
            jobs:['job1', 'job2']
    ).build(this);
    
    oahMaster.with{
      logRotator{
          numToKeep(365)
      }
    
    }

```

## JS Build Job


```

import jenkins.automation.utils.JsJobBuilder


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
import jenkins.automation.utils.BaseJobBuilder
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