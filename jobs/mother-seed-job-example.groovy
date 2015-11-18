import jenkins.automation.utils.ScmUtils

import java.lang.reflect.Array


//def reposToAutomate = RepositoryYamlParser.parseRepositories('some_yaml_file')
def reposToAutomate = [
        [projectName: "OAH", url: "https://github.cfpb.gov/muchniki/oah-jenkins-automation"]

]
def reposToInclude  = reposToAutomate.collect()


reposToInclude<< [projectName: "automation", url: 'https://github.com/imuchnik/jenkins-automation']

reposToAutomate.each { project ->
    listView(project.projectName) {
        columns {
            status()
            weather()
            name()
            lastSuccess()
            lastFailure()
            lastDuration()
            buildButton()
        }
        filterBuildQueue()
        filterExecutors()
        jobs {
            regex(/(?i)(${project.projectName}.*)/)
        }
    }
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