// ** templates **
// ***************

job('template-base') {
  wrappers {
    colorizeOutput()
  }

  // handle failure
}


job('template-base-js') {
  using 'template-base'
  parameters {
    stringParam("WORKSPACE_BUILDER", "", "name of the workspace builder job from which to clone the workspace")
  }
  scm {
    cloneWorkspace('$WORKSPACE_BUILDER')
  }
  wrappers {
    nodejs('Node 0.12')
  }
}


job('template-build-js') {
  using 'template-base-js'

  steps {
    shell(
      '''
      cd $DIR_TO_BUILD
      if [ -f 'frontendbuild.sh' ]; then
        ./frontendbuild.sh
      else
        npm install
        npm run build
      fi
      '''
    )
  }
  publishers {
    archiveArtifacts('dist/')
  }

}


job('template-test-js') {
  using 'template-base-js'

  steps {
    shell(
      '''
      cd $DIR_UNDER_TEST
      if [ -f 'frontendtest.sh' ]; then
        ./frontendtest.sh
      else
        npm install
        npm test
      fi
      '''
    )
  }
}

job('template-deploy') {
  using 'template-base'
  parameters {
    choiceParam("INVENTORY_FILE", ["dev", "test", "prod"], "name of ansible inventory file to use for deployment")
    stringParam("WORKSPACE_BUILDER", "", "name of the workspace builder job from which to clone the workspace")
  }
  scm {
    cloneWorkspace('$WORKSPACE_BUILDER')
  }
  steps {
    shell(
      '''
      # enable python virtualenv that has ansible already installed
      source $JENKINS_HOME/ANSIBLE_VENV/bin/activate
      # specify $PROJ_NAME in your jenkins-{ENV}.groovy: 
      #  job() {
      #    environmentVariables(['PROJ_NAME': 'xxx'])
      #  }
      ansible-playbook -i $PROJ_NAME/ansible/inventories/$INVENTORY_FILE $PROJ_NAME/ansible/playbook.yml --vault-password-file=$PROJ_NAME/ansible/get_vault_password --private-key=/var/lib/jenkins/.ssh/id_rsa_deploy
      '''
    )
  }
  wrappers {
    injectPasswords()
  }
}

job('template-workspace-builder-base') {
  parameters {
    stringParam("GIT_TRIGGER_COMMIT", "", "the commit hash of the trigger repo")
  }

  publishers {
    publishCloneWorkspace('')
  }
}


// ** build flows **
// *****************

buildFlowJob('template-base-build-flow') {
  configure { node ->
    node / buildNeedsWorkspace('true')
  }
}

buildFlowJob('template-primary-build-flow') {
  using 'template-base-build-flow'
  parameters {
    choiceParam("INVENTORY_FILE", ["dev", "test", "prod"], "name of ansible inventory file to use for deployment")
    stringParam("WORKSPACE_BUILDER", "", "name of the workspace builder job from which to clone the workspace")
  }
}

buildFlowJob('template-base-trigger') {
  using 'template-base-build-flow'
  triggers {
    scm('H/3 * * * *')
    // pullrequest()
  }
}


// ** DSL Master Job **
// ********************
projectRepos = readFileFromWorkspace("project_repos.txt").split('\n')

job('DSL Master') {
  using 'template-base'
  multiscm {
    git {
      remote {
        url('https://github.com/cfpb/jenkins-automation')
      }
      branch('*/master')
    }
    projectRepos.each { repo ->
      git {
        remote {
          url(repo)
        }
        branch('*/master')
        relativeTargetDir(repo.split('/')[4])
      }
    }
  }

  steps {
    dsl {
      external(['**/jenkins.groovy'])
      additionalClasspath('lib/*.jar')
      removeAction('DISABLE')
    }
  }
}

