package jenkins.automation.utils

class ScmUtils {

    //TODO : add a conditional to handle a single repo.


    static void project_repos(context, repos, use_versions = true) {
        context.with {
            repos.each { repo ->
                def parsed_out_url = repo.url.tokenize('@')

                def parsed_url = parsed_out_url[0]

                def version = parsed_out_url[1]
                git {
                    remote {
                        url(parsed_url)
                    }
                    if (use_versions && version!=null) {
                        branch("*/tags/$version")
                    }
                    relativeTargetDir(repo.name)
                }
            }
        }
    }
}