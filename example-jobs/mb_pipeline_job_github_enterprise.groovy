import jenkins.automation.builders.MultibranchPipelineJobBuilder


new MultibranchPipelineJobBuilder(
    name: "multi-branch-pipeline-github-enterprise",
    description: "Sample Multibranch Pipeline using the GitHub API as the branch source",
    branchSource: MultibranchPipelineJobBuilder.BranchSourceType.GITHUB,
    gitCredentials: "8fbdbaa0-d5ff-4acb-9e8f-27e49b77048a",
    ghOwner: "org-name",
    ghRepo: "repo-name",
    ghApiEndpoint: "https://github.example.com/api/v3",
    oldNumToKeep: 1
).build(this)
