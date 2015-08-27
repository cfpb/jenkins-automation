static void buildWorkSpace(def job, String projectName, def orgName) {

    def privateRepoUrl = "https://github.com/$orgName/$projectName"
    def repoList = [[repoUrl = 'test1', repoName = 'foo']]
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

def myJob = job('example')
buildWorkSpace(myJob, 'collab', 'cfpb')