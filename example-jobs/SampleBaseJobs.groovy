import jenkins.automation.builders.BaseJobBuilder
import jenkins.automation.utils.CommonUtils

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

