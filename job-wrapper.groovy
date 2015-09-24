import gov.cfpb.ScmUtils


def myJob = job('example'){

}

addMultiScm(myJob, projectName='collab', orgName='cfpb')

job('example') {
    description('some project')
    multiscm {
        ScmUtils.project_repos delegate, ['collab', 'cfpb', 'etc']
        git {

        }
    }

}