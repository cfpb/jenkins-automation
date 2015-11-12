import jenkins.automation.utils.RepositoryYamlParser
import jenkins.automation.utils.ScmUtils


def reposToAutomate = RepositoryYamlParser.parseRepositories('some_yaml_file')


reposToAutomate.each { project ->
    job(project.projectName + 'SeedJob') {

        multiscm {
            ScmUtils.project_repos(delegate, [ project.url, 'https://github.com/cfpb/jenkins-automation.git'], use_versions)
        }

        triggers {
            scm 'H/5 * * * *'
        }
        steps {

            dsl {
                external 'jobs/seed.groovy'
                // you can also give a pattern here like jobs/**/*Jobs.groovy', which would runa
                //scripts in jobs directory that end with /*Jobs.groovy
                additionalClasspath 'src/main/groovy'
            }
        }
    }
}