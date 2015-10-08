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

    static void project_repos(context, repos, use_versions = true) {
        context.with {
            repos.each { repo ->
                def parsed_out_url = repo.url.tokenize('@')
                assert parsed_out_url.length == 2

                def url = parsed_out_url[0]
                def version = parsed_out_url[1]
                git {
                    remote {
                        url(url)
                    }
                    if (version) {
                        branch("*/tags/$version")
                    }
                    relativeTargetDir(repo.name)
                }
            }
        }
    }
}