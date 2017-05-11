import jenkins.automation.builders.BaseJobBuilder

import jenkins.automation.utils.PluginUtils

def job=new BaseJobBuilder(
        name: "sample-base-job-with-sqs-support",
        description: "A job with some additional plugin added"
).build(this).with {
    PluginUtils.addSQSNotification(delegate, "" ,"https://localhost/jenkins","foobar",customSQSMessageValue="Say something sharp" )
}