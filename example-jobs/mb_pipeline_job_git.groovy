import jenkins.automation.builders.MultibranchPipelineJobBuilder


new MultibranchPipelineJobBuilder(
    name: "multi-branch-pipeline-git",
    description: "Sample Multibranch Pipeline using git as the branch source",
    branchSource: MultibranchPipelineJobBuilder.BranchSourceType.GIT,
    gitCredentials: "009c8c9d-3cf5-4b2a-89f3-286977cabddf",
    gitRemote: "https://github.com/OrlandoSoto/orlando-shared-libraries",
).build(this)
