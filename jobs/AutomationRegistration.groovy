import ScmUtils

def repos = [
        [name: 'jenkins-automation', url: "https://github.com/cfpb/jenkins-automation"],
        [name: 'collab', url: "https://github.com/cfpb/jenkins-automation"]
]

job('automation-registration') {
    description('This job allows project to register for automation.')

    multiscm {
        ScmUtils.project_repos(delegate, repos)
    }

    triggers {
        scm('H/3 * * * *')
    }

    wrappers {
        colorizeOutput()
    }
}