import jenkins.automation.builders.MultibranchPipelineJobBuilder

new MultibranchPipelineJobBuilder(
        name: "mb-pipeline-gh",
        description: "Sample Multibranch Pipeline Job using a public Github repository",
        branchSource: "git",
        gitRemote: "https://github.com/OrlandoSoto/orlando-shared-libraries.git",
        oldNumToKeep: 1
).build(this)
