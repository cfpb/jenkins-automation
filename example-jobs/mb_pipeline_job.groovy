import jenkins.automation.builders.MultibranchPipelineJobBuilder

def mbjob = new MultibranchPipelineJobBuilder(
        name: "my MultibranchPipelineJobBuilder.groovy mb pipeline",
        description: "Sample MultibranchPipelineJobBuilder.groovy job",
        emails: ['email1@server1.com', 'email2@server2.com']
).build(this);