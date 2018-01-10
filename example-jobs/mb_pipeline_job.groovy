import jenkins.automation.builders.MultibranchPipelineJobBuilder

new MultibranchPipelineJobBuilder(
        name: "new-mb-pipeline",
        description: "Sample Multibranch Pipeline Job job",
        branchSourceGithub: true,
        ghOrganizationName: "testprbuilder",
        ghRepositoryName: "pr_tester",
        ghScanCredentialsId: "8fbdbaa0-d5ff-4acb-9e8f-27e49b77048b",
        ghEndpoint: "https://github.example.com/api/v3/",
        branchSourceGit: true,
        ghRemote: "https://github.com/OrlandoSoto/orlando-shared-libraries.git",
        ghCredentialsId: "009c8c9d-3cf5-4b2a-89f3-286977cabddf",
        oldNumToKeep: 365
).build(this)
