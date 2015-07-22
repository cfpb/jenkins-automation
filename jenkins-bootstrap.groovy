job('dsl-master') {
  description('This job rebuilds dsl-project-builder whenever jenkins-automation or jenkins-automation-private changes.')
  
  multiscm {
    git {
      remote {
        url('https://github.com/cfpb/jenkins-automation')
      }
      branch('*/master')
      relativeTargetDir('jenkins-automation')
    }
    git {
      remote {
        url(JENKINS_AUTOMATION_PRIVATE_GIT_URL)
      }
      branch('*/master')
      relativeTargetDir('jenkins-automation-private')
    }
  }

  triggers {
    scm('H/3 * * * *')
  }

  steps {
    shell('''
    ./jenkins-automation/lib/getProjectRepos.py
    ''')
    dsl {
      external(['jenkins-automation/jenkins-automation.groovy'])
      removeAction('DISABLE')
    }
  }

  wrappers {
    colorizeOutput()
  }
}