import jenkins.automation.builders.*
// evaluate(new File("/Users/sotoo/Documents/dev/jenkins-automation/src/main/groovy/jenkins/automation/builders/MultibranchPipelineJobBuilder.groovy"))
def mbjob = new MultibranchPipelineJobBuilder(
        name: "new-mb-pipeline",
        description: "Sample MultibranchPipelineJobBuilder.groovy job",
        emails: ['email1@server1.com', 'email2@server2.com']
).build(this);