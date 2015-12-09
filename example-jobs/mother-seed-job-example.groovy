import jenkins.automation.utils.ScmUtils

import java.lang.reflect.Array


//def reposToAutomate = RepositoryYamlParser.parseRepositories('some_yaml_file')
def reposToAutomate = [
        [name: "OAH", url: "https://github.com/muchniki/oah-jenkins-automation"],
        [name: "PFC", url: "https://github.com/paying-for-college/jenkins"],
        [name: "Qu", url: "https://github.com//qu-jenkins-automation"]

]




reposToAutomate.each { project ->
    def reposToInclude = [[name: "automation", url: 'https://github.com/imuchnik/jenkins-automation']]  //repo containing utils and builders
    reposToInclude<< project
    listView(project.name) {
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
            regex(/(?i)(${project.name}.*)/)
        }
    }
    job(project.name + 'SeedJob') {

        multiscm {
            ScmUtils.project_repos(delegate, reposToInclude, false)
        }

        triggers {
            scm 'H/5 * * * *'
        }
        steps {

            dsl {
                external "jobs/**/*.groovy"
                // you can also give a pattern here like jobs/**/*Jobs.groovy', which would run
                //scripts in jobs directory that end with /*Jobs.groovy
                additionalClasspath "automation/src/main/groovy \r\n ${project.name}/src/main/groovy \r\n"

            }
        }
    }
}