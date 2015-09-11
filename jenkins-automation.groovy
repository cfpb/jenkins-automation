// ** constants **
// ***************
def CREATE_AND_ACTIVATE_VIRTUAL_ENV = """
if [ -d ".env" ]; then
  echo "**> virtualenv exists"
else
  echo "**> creating virtualenv"
  virtualenv -p /usr/local/bin/python2.7 .env
fi

. .env/bin/activate
"""

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
             ./frontendbuild.sh
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
      ./frontendtest.sh

      '''
    )
  }
}

job('template-django-unit-test') {
  using 'template-base'

  steps {
    shell("""\
$CREATE_AND_ACTIVATE_VIRTUAL_ENV

pip install -r requirements.txt
python manage.py test
    """)
  }

  publishers {
    archiveXUnit {
      jUnit {
        pattern("**/reports/junit.xml")
      }
    }
    allowBrokenBuildClaiming()
    cobertura("**/reports/coverage.xml") {
      failNoReports(true)
    }
  }

}

job('template-deploy') {
  using 'template-base'

  steps {
    shell(
      '''
      # enable python virtualenv that has ansible already installed
      source $JENKINS_HOME/ANSIBLE_VENV/bin/activate
      
      # specify $PROJ_NAME and $INVENTORY_FILE in your jenkins.groovy: 
      #  job() {
      #    environmentVariables(['PROJ_NAME': 'xxx', 'INVENTORY_FILE': 'xxx'])
      #  }

      ansible-playbook -i $PROJ_NAME/ansible/inventories/$INVENTORY_FILE $PROJ_NAME/ansible/playbook.yml --vault-password-file=$PROJ_NAME/ansible/get_vault_password --private-key=/var/lib/jenkins/.ssh/id_rsa_deploy
      '''
    )
  }
  wrappers {
    injectPasswords()
  }

  // Add mask passwords, since there's no option for it yet
  configure { node ->
    node / "buildWrappers" / EnvInjectPasswordWrapper << maskPasswordParameters("true")
  }
}

// Requires: 
// - CONFIG_FILE: name of the config file to use
// - BEHAVE_TAGS: any tags you want to use
// 
// Example: environmentVariables(['CONFIG_FILE': 'environment_dev.cfg', 'BEHAVE_TAGS': '-t=~ignore -t=smoke_testing'])
job('template-browser-test') {
  using 'template-base'

  wrappers {
    injectPasswords()
    sauceOnDemandConfig {
      enableSauceConnect(true)
      webDriverBrowsers("Linuxchrome44")
    }
  }
  // Add mask passwords, since there's no option for it yet
  configure { node ->
    node / "buildWrappers" / EnvInjectPasswordWrapper << maskPasswordParameters("true")
  }

  steps {
    shell("""
$CREATE_AND_ACTIVATE_VIRTUAL_ENV

cd test/browser_testing
cp features/\${CONFIG_FILE} features/environment.cfg


rm -rf test-results
mkdir test-results

export SELENIUM_TUNNEL=\${sauce_tunnel_name}
export SELENIUM_VIDEO=True
export SELENIUM_CMD_TIMEOUT=600
export SELENIUM_IDLE_TIMEOUT=90
export SELENIUM_VIDEO_UPLOAD_ON_PASS=True
export SELENIUM_NAME=\${JOB_NAME}
export SELENIUM_LIB="2.45.0"
export SELENIUM_RESOLUTION="1024x768"

pip install -r requirements.txt
behave -k -f=plain --logging-level=INFO --junit --junit-directory=test-results \${BEHAVE_TAGS}
    """)
  }

  publishers {
    archiveXUnit {
      jUnit {
        pattern("**/test-results/TESTS-*.xml")
      }
    }
    allowBrokenBuildClaiming()
  }
}

// ** build flows **
// *****************

buildFlowJob('template-base-build-flow') {
  triggers {
    scm('H/3 * * * *')
    // pullrequest()
  }
  configure { node ->
    node / buildNeedsWorkspace('true')
  }
}


// ** DSL Project Builder **
// ********************
projectRepos = readFileFromWorkspace("project_repos.txt").split('\n')

job('dsl-project-builder') {
  using 'template-base'
  description('This job rebuilds all jobs defined in all jenkins.groovy files whenever any project repository changes.')
  multiscm {
    git {
      remote {
        url('https://github.com/Ooblioob/jenkins-automation')
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

