package jenkins.automation.utils

/**
 * Utility class to provide nicer, terser DSL for common tasks
 */
class ScmUtils {

/***
 *
 * Utility method to create a multiscm block from a list of repos.
 * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#using-multiscm-utility" target="_blank">example</a>
 * @param context A reference to the job object being modified
 * @param repos List of repo maps. Each repo object must include url property and
 * optionally sub_directory(checkout subdirectory), shallow(shallow clone), branch property, and
 * disable_submodule properties.
 * @param use_versions Flag to check out the repo at a specific tag.
 * The tag is parsed out from url property appended after {@literal @} sign.
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
                    } else if (repo.branch) {
                        branch repo.branch
                    } else {
                        branch "master"
                    }
                    if (disable_submodule) {
                        configure { node ->
                            node / 'extensions' / 'hudson.plugins.git.extensions.impl.SubmoduleOption' {
                                disableSubmodules disable_submodule
                            }
                        }
                    }
                    extensions {
                        if (repo.sub_directory) {
                            relativeTargetDirectory(repo.sub_directory)
                        }
                        if (repo.shallow && repo.shallow != null) {
                            cloneOptions {
                                shallow()
                            }
                        }
                    }
                }
            }
        }
    }

/***
 *
 * Utility method to do git shallow-clones easily.
 * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#using-the-git-shallow-clone-utility" target="_blank">example</a>
 * @param context A reference to the job object being modified
 * @param gitUrl A string containing a git repo URL
 * @param gitBranch Optional branch to check-out; defaults to master
 */
    static void shallowGit(context, gitUrl, gitBranch = "master") {
        context.with {
            git {
                remote {
                    url gitUrl
                }

                branch gitBranch

                extensions {
                    cloneOptions {
                        shallow()
                    }
                }
            }
        }
    }

}
