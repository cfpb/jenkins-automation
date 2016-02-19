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

new BaseJobBuilder(
        name: "sample-base-job-with-virtualenv",
        description: "A job that creates and activates a python 2.7 virtualenv"
).build(this).with {
    steps {
        shell( CommonUtils.python27Virtualenv + """
                # pip install ansible
                ls -la
                env
                echo "Hello world"
            """.stripIndent()
        )
    }
}

new BaseJobBuilder(
        name: "sample-base-job-with-performance-publisher",
        description: "A job with a performance publisher. It does not include the actual bits that run the load tests"
).build(this).with {
    steps {
        shell("echo 'Run jmeter tests here'")
    }
    CommonUtils.addPerformancePublisher(delegate,failedThresholdPositive=10, failedThresholdNegative=10, unstableThresholdPositive=5, unstableThresholdNegative=5)
}

def repos = [
        [url: "https://github.com/cfpb/jenkins-automation@2.0"],
        [url: "https://github.com/cfpb/collab"]
]
new BaseJobBuilder(
        name: "sample-base-job-with-multiscm",
        description: "A sample with multiple source control repositories",
).build(this).with {
    multiscm {
        ScmUtils.project_repos(delegate, repos, true)
    }
}
