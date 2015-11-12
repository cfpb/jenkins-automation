jenkins-automation
==================

Repos automated by Jenkins DSL 

Jenkins automation is a library of helper functions to make continuous integration and deploment easy. 

We use [Job DSL](https://github.com/jenkinsci/job-dsl-plugin/wiki) to automate.
The `DSL Master` job will watch all listed repositories for changes, and rebuild all jobs defined in those repositories.
To create Jenkins jobs for your project, you must create a `jenkins.groovy` file at the root of your project's primary repository.
Then add your project's primary repo to the list of watched repos in your organization's `jenkins-automation-private` repository.

You should create your jobs using the templates defined in 
[jenkins.groovy](https://github.com/cfpb/jenkins-automation/blob/master/jenkins.groovy).

## How it works  -DEPRECATED
### Good place to start learning about job-dsl API:
  http://sheehan.github.io/job-dsl-plugin
  
### Once you are little more comfortable, try the sandbox:
  
  http://job-dsl.herokuapp.com/
  Here is some code that uses our utils:

```

   static void addMyFeature(def job) {
        def repoList=[
          [name: "jenkins-automation", url: "https://github.com/cfpb/jenkins-automation@2.0"],
          [name: "collab", url: "https://github.com/cfpb/jenkins-automation"]
   ]
          job.with {
              description("Arbitrary feature")
            multiscm{
              repoList.each { repo ->
              def parsed_out_url = repo.url.tokenize('@')
              def parsed_url = parsed_out_url[0]
                             
                git {
                  remote{
                    url(parsed_url)
                  }
                }
              }
            }
          }
      }
      
  
      def myJob = job('example')
      addMyFeature(myJob)
      
```
### We have significantly pivoted the direction of this project. The docs below are outdated and we are working on updating them 

### If you would like to get started on it for your project please talk to @imuchnik. 

This repository defines two types of jobs in jenkins-automation.groovy:

- A `DSL Master` job: watches all your project repositories and builds all the jobs defined in those repositories' `jenkins.groovy` files`
- A number of `template` jobs: these templates should be extended by `jenkins.groovy` files in your project repositories to reduce boilerplate.

## Guiding principles

## Installation 

### Required Jenkins Plugins

- https://wiki.jenkins-ci.org/display/JENKINS/Job+DSL+Plugin
- https://wiki.jenkins-ci.org/display/JENKINS/Build+Flow+Plugin
- https://wiki.jenkins-ci.org/display/JENKINS/EnvInject+Plugin
- http://wiki.jenkins-ci.org/display/JENKINS/Git+Plugin
- http://wiki.jenkins-ci.org/display/JENKINS/Multiple+SCMs+Plugin
- http://wiki.jenkins-ci.org/display/JENKINS/NodeJS+Plugin
- http://wiki.jenkins-ci.org/display/JENKINS/Clone+Workspace+SCM+Plugin

### jenkins-automation-private repository
`jenkins-automation` is all the tooling to create a continuous integration/deployment process for your organization.
You need a way to specify all the projects that should be continuously integrated/deployed.
`jenkins-automation-private` is the repository where you specify all these projects.
It contains a single file listing all the projects that should be watched by jenkins.
When your organization starts a new project,
you add the root repo for the project to the `jenkins-automation-private` repo.
All iteration of the deployment process then occurs in the root project repo owned by the project team,
rather than `jenkins-automation-private`, owned by system engineering.

You will need to create a `jenkins-automation-private` repository with the following structure:

```
  - jenkins-automation-private/
    - repositories.yml
    - README.md
```

`repositories.yml` should have the following content:

```
repositories:
    - 'https://github.com/org/project-root-repo1'
    - 'https://github.examplecom/org/project-root-repo2'
```

`README.md` should provide organization-specific guidance on how teams can add their project to this repo for continuous integration/delivery.

### Job DSL Bootstrap

Once you have created your `jenkins-automation-private` repo,
and installed all the required jenkins plugins,
you need to create a "bootstrap" job in Jenkins to create the `dsl-master` job.
Create a free-style job with the following configuration: 

  - Project Name: dsl-bootstrap
  - Description: This job needs to run only once to build the dsl-master job.
  - Source Control Management > Git > Repository Url: https://github.com/cfpb/jenkins-automation
  - Build Environment > Inject Environment Variables > Properties Content: 

    ```
    JENKINS_AUTOMATION_PRIVATE_GIT_URL=https://github.com/<ORG>/jenkins-automation-private.git
    ```

  - Build > Process Job DSLs > Look on Filesystem > DSL Scripts: jenkins-bootstrap.groovy

Manually run the bootstrap job.  This will create the `dsl-master` job.  
The `dsl-master` job rebuilds `dsl-project-builder` whenever `jenkins-automation` or `jenkins-automation-private` changes.  The `dsl-project-builder` job rebuilds all jobs defined in all jenkins.groovy files whenever any project repository changes.

## Using Jenkins Automation

Jenkins Automation (JA) is built around the idea of a "project". A Project contains all the information needed to reproducibly deploy a software system.

In order to create a project, you must define three things:

  1. Source Code - Include the source code in your project repository or specify where to find your source code.
  2. Deployment instructions - Define how to deploy your software to a server or set of servers. We use [Ansible](www.ansible.com) for this purpose, but you can use any configuration management solution.
  3. Continuous Integration/Deployment configuration - Define what to test, when builds should fail, etc.

It is important that we open source as much Source Code as possible, while ensuring the security and integrity of our network and systems.
It is therefore possible to split a project between two Project Repositories, one public and one private.
If others wish to contribute to your open source project, 
they should be able to stand up a development environment without access to the private project repository.
They should be able to stand up a production environment by creating their own private project repository.

In the single repository model, the repository has the following structure:

- project-repository/
  - jenkins.groovy
  - ansible/
    group_vars/
      all/
        repositories.yml

