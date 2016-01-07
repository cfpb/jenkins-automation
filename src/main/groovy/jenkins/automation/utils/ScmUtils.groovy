package jenkins.automation.utils

/**
 * Utility class to provide nicer, terser DSL for common tasks
 *  @see <a href="https://github.com/imuchnik/jenkins-automation/blob/gh-pages/docs/examples.md#using-multiscm-utility" target="_blank">Using Utility Class example</a>

 */
class ScmUtils {


    static void project_repos(context, repos, use_versions = true) {
        Boolean shallow
        context.with {
            repos.each { repo ->
                def parsed_out_url = repo.url.tokenize('@')

                def parsed_url = parsed_out_url[0]

                def version = parsed_out_url[1]

                if(repo.containsKey('shallow')) {
                    shallow = repo.shallow
                }else{
                    shallow = false
                }

                git {
                    remote {
                        url(parsed_url)
                    }
                    if (use_versions && version!=null) {
                        branch("*/tags/$version")
                    }
                    relativeTargetDir(repo.sub_directory)
                    shallowClone(shallow)
                }
            }
        }
    }
}
