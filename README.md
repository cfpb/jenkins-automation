jenkins-automation
==================

Repos automated by Jenkins DSL 

This repository contains the automation scripts for dev Jenkins.
We use [Job DSL](https://github.com/jenkinsci/job-dsl-plugin/wiki) to automate.
The `DSL Master` job will watch all listed repositories for changes, and rebuild all jobs defined in those repositories.
To create Jenkins jobs for your project, you must create a `jenkins.groovy` file at the root of your project's primary repository.
Then add your project's primary repo to the list of watched repos in your organization's `jenkins-automation-private` repository.

You should create your jobs using the templates defined in 
[jenkins.groovy](https://github.com/cfpb/jenkins-automation/blob/master/jenkins.groovy).


## Installation 

### Jenkins Plugins

- https://wiki.jenkins-ci.org/display/JENKINS/Job+DSL+Plugin
- https://wiki.jenkins-ci.org/display/JENKINS/Build+Flow+Plugin
- https://wiki.jenkins-ci.org/display/JENKINS/EnvInject+Plugin
- http://wiki.jenkins-ci.org/display/JENKINS/Git+Plugin
- http://wiki.jenkins-ci.org/display/JENKINS/Multiple+SCMs+Plugin
- http://wiki.jenkins-ci.org/display/JENKINS/NodeJS+Plugin
- http://wiki.jenkins-ci.org/display/JENKINS/Clone+Workspace+SCM+Plugin

### Job DSL Bootstrap

You'll need to create a "bootstrap" to actually create the jobs in this repository.
That bootstrap job should look something like this: 

```groovy

JENKINS_AUTOMATION_PRIVATE_GIT_URL = 'https://github.com/owner/jenkins-automation-private'

job('DSL Master Bootstrap') {
  description('This Job bootstraps the DSL Master Job from the github.com/cfpb/jenkins-automation repo.')
  multiscm {
    git {
      remote {
        url('https://github.com/cfpb/jenkins-automation')
      }
      branch('*/master')
      relativeTargetDir('jenkins-automation')
    }
    git {
      remote {
        url(JENKINS_AUTOMATION_PRIVATE_GIT_URL)
      }
      branch('*/master')
      relativeTargetDir('jenkins-automation-private')
    }
  }

  triggers {
    scm('H/3 * * * *')
  }

  steps {
    shell('''
    ./jenkins-automation/lib/getProjectRepos.py
    ''')
    dsl {
      external(['jenkins-automation/jenkins-automation.groovy'])
      removeAction('DISABLE')
    }
  }

  wrappers {
    colorizeOutput()
  }
}
```

You can either copy this script into a `DSL Master Bootstrap Bootstrap` job, or, if that is too ridiculous,
just manually create a job that looks the same as the job
created by the above DSL script.
