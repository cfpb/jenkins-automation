import jenkins.automation.builders.MultibranchPipelineJobBuilder

new MultibranchPipelineJobBuilder(
        name: "mb-pipeline-ghe",
        description: "Sample Multibranch Pipeline Job using a Github Enterprise repository",
        branchSource: "Github",
        gitCredentials: "8fbdbaa0-d5ff-4acb-9e8f-27e49b77048b",
        gitOwner: "testprbuilder",
        gitRepository: "pr_tester",
        gitEndpoint: "https://github.example.com/api/v3/",
        oldNumToKeep: 1
).build(this)
