class ScmUtils {

    static void addMultiScm(def job, String projectName, def orgName) {

        def privateRepoUrl = "https://github.com/$orgName/$projectName"
        def repoList = [[repoUrl: 'test1', repoName: 'foo']]
        job.with {
            multiscm {
                repoList.each { repo ->
                    git {
                        remote {
                            url(repo.repoUrl)
                        }
                        relativeTargetDir(repo.repoName)
                    }
                }

            }
        }
    }

    static void project_repos(context, repos) {
        context.with {
            repos.each { repo ->
                git {
                    remote {
                        url(repo.url)
                    }
                    relativeTargetDir(repo.name)
                }
            }
        }
    }
}