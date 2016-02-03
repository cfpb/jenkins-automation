package jenkins.automation.utils

/**
 * Utility class to provide nicer, terser DSL for common tasks
 *
 * @see <a href="https://github.com/imuchnik/jenkins-automation/blob/gh-pages/docs/examples.md#using-multiscm-utility" target="_blank">Using Utility Class example</a>

 */
class ScmUtils {

/***
 *
 * Utility method to create a multiScm block from a List of repos
 * @param repos List of repo maps
 * @param use_versions flag to check out the repo at a specific tag. The tag is parsed out from url property appended after @ sign.
 * each repo object must include url property and optionally sub_directory(checkout subdirectory, shallow(shallow clone) and disable_submodule properties.
 */
    static void project_repos(context, repos, use_versions = true) {
        Boolean disable_submodule = false
        context.with {
            repos.each { repo ->
                def parsed_out_url = repo.url.tokenize('@')

                def parsed_url = parsed_out_url[0]

                def version = parsed_out_url[1]

                disable_submodule = (repo.disable_submodule ?: false)

                git {
                    remote {
                        url(parsed_url)
                    }
                    if (use_versions && version != null) {
                        branch "*/tags/$version"
                    } else {
                        branch "master"
                    }
                    relativeTargetDir(repo.sub_directory)
                    shallowClone(repo.shallow && repo.shallow != null ? repo.shallow : true)
                    if (disable_submodule) {
                        configure { node ->
                            node / 'extensions' / 'hudson.plugins.git.extensions.impl.SubmoduleOption' {
                                disableSubmodules disable_submodule
                            }
                        }
                    }
                }
            }
        }
    }
}
