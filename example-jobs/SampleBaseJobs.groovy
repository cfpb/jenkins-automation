import jenkins.automation.builders.BaseJobBuilder
import jenkins.automation.utils.CommonUtils

new BaseJobBuilder(
        name: "sample-base-job",
        description: "This is a simple job"
).build(this)

new BaseJobBuilder(
        name: "sample-base-job-with-additional-config",
        description: "A job with some additional configurations added"
).build(this).with {
    //how to use CommonUtils
    CommonUtils.addInjectGlobals(delegate)
}