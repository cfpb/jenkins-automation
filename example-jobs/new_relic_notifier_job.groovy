import jenkins.automation.builders.BaseJobBuilder
import jenkins.automation.utils.PluginUtils

def job = new BaseJobBuilder(
        name: "sample-base-job-with-new-relic",
        description: "A job with some additional plugin added"
).build(this).with {
    PluginUtils.addNewRelicSupport(delegate, "nr-api-key", "new", "foo", "some_log", "deploy", '1')
}

