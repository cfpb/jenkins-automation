import jenkins.automation.builders.BaseJobBuilder
import jenkins.automation.utils.CommonUtils
import jenkins.automation.utils.ScmUtils

//Note that BaseJobBuilder is simply a wrapper around `job` that also adds some additional configuration such as log rotation and broken build claiming
new BaseJobBuilder(
        name: "sample-base-job",
        description: "This is a simple job"
).build(this)

new BaseJobBuilder(
        name: "sample-base-job-with-additional-config",
        description: "A job with some additional configurations added"
).build(this).with {
    //how to use CommonUtils
    CommonUtils.addInjectGlobalPasswords(delegate)
}

new BaseJobBuilder(
        name: "sample-base-job-with-log-parsing",
        description: "A job with log parsing added"
).build(this).with {
    //how to use CommonUtils; pass a custom filename to override the default
    CommonUtils.addLogParserPublisher(delegate, "/var/lib/jenkins/some_rules_file.txt")
}


def repos = [
        [name: 'jenkins-automation', url: "https://github.com/cfpb/jenkins-automation@2.0"],
        [name: 'collab', url: "https://github.com/cfpb/collab", shallow: true]
]
new BaseJobBuilder(
        name: "sample-base-job-with-multiscm",
        description: "A sample with multiple source control repositories",
).build(this).with {
    multiscm {
        ScmUtils.project_repos(delegate, repos, true)
    }
}