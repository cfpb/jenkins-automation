import jenkins.automation.utils.ScmUtils


//def reposToAutomate = RepositoryYamlParser.parseRepositories('some_yaml_file')
def reposToAutomate = [
        [projectName: "OAH", url: "https://github.cfpb.gov/muchniki/oah-jenkins-automation"]

]
def reposToInclude =reposToAutomate
reposToInclude<< [projectName: "automation", url: 'https://github.com/cfpb/jenkins-automation']

reposToAutomate.each { project ->
    job(project.projectName + 'SeedJob') {

        multiscm {
            ScmUtils.project_repos(delegate, reposToInclude, false)
        }

        triggers {
            scm 'H/5 * * * *'
        }
        steps {

            dsl {
                external 'jobs/**/*.groovy'
                // you can also give a pattern here like jobs/**/*Jobs.groovy', which would runa
                //scripts in jobs directory that end with /*Jobs.groovy
                additionalClasspath 'src/main/groovy'
            }
        }
    }
}