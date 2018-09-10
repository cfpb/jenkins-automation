# Welcome to jenkins-automation

[![Build Status](https://travis-ci.org/cfpb/jenkins-automation.svg?branch=master)](https://travis-ci.org/cfpb/jenkins-automation)

Repos automated by Jenkins DSL

Jenkins automation is a library of helper functions to make continuous integration
and deployment fast, easy and fun.

We use [Job DSL](https://github.com/jenkinsci/job-dsl-plugin/wiki) to automate.
We have further enhanced the functionality with a set of builders. You can see what they can do and how to use them in our
[API documentation](http://cfpb.github.io/jenkins-automation/).

Our collection of builders is still growing and we would love your contributions. Please see [How to contribute](CONTRIBUTING.md).

### Good place to start learning about job-dsl API:

    http://jenkinsci.github.io/job-dsl-plugin/

### Once you are little more comfortable, try the sandbox:

    http://job-dsl.herokuapp.com/

#### If you would like to get started on it for your project a good place to start is our [starter repo](https://github.com/cfpb/jenkins-as-code-starter-project).

## Guiding principles

1. Make valuable
2. Make it easy
3. Make it fast
4. Make it pretty

and always [YAGNI](https://en.wikipedia.org/wiki/You_aren%27t_gonna_need_it),
            [DRY](https://en.wikipedia.org/wiki/Don%27t_repeat_yourself)
          and [KISS](https://en.wikipedia.org/wiki/KISS_principle)

### Configuration added to all jobs

Any jobs created by using these `Builders` get the following configuration added automatically:

- Colorized Output (requires AnsiColor plugin)
- Timestamps in the build log (requires Timestamper plugin)
- Broken build claiming (requires Claim plugin)
- Build Failure Analyzer (requires Build Failure Analyzer plugin)
- Max 30 builds retained

If the `emails` property is set on a job, the job automatically gets configured with an Extended Email block, as well (requires Email Extension plugin)

### Commonly used Jenkins Plugins that we support

- https://wiki.jenkins-ci.org/display/JENKINS/Job+DSL+Plugin
- https://wiki.jenkins-ci.org/display/JENKINS/EnvInject+Plugin
- http://wiki.jenkins-ci.org/display/JENKINS/Git+Plugin
- http://wiki.jenkins-ci.org/display/JENKINS/Multiple+SCMs+Plugin
- http://wiki.jenkins-ci.org/display/JENKINS/NodeJS+Plugin
- http://wiki.jenkins-ci.org/display/JENKINS/Clone+Workspace+SCM+Plugin


### Gradle init scripts

Within the path 

```
gradle/init.d/init.gradle
```

There is a file that contains tasks that may be imported into other 
gradle builds by using the additional flag `-I` during normal gradle
execution. Currently this file contains a task designed to copy the
jar files of all dependencies of a project into a directory `jac_dependencies`.
This tool may be useful when integrating with tools like
[OWASP](https://www.owasp.org/index.php/OWASP_Dependency_Check)


Common execution might be

```
./gradlew -I gradle/init.d/init.gradle copyDeps
```
