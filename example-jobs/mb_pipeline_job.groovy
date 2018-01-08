import jenkins.automation.builders.MultibranchPipelineJobBuilder
// evaluate(new File("/Users/sotoo/Documents/dev/jenkins-automation/src/main/groovy/jenkins/automation/builders/MultibranchPipelineJobBuilder.groovy"))
new MultibranchPipelineJobBuilder(
        name: "new-mb-pipeline",
        description: "Sample MultibranchPipelineJobBuilder.groovy job",
        emails: ['email1@server1.com', 'email2@server2.com']
).build(this)
