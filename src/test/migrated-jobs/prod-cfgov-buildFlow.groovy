import FlowJobBuilder

def cfGovDeployJob = new FlowJobBuilder(
        name: 'cf.gov',
        description: 'CF.gov master build flow job',
        jobs: []
).build(this)

cfGovDeployJob.configure { Node project ->
    project / 'properties' /'com.sonyericsson.jenkins.plugins.bfa.model.ScannerJobProperty'(plugin:"build-failure-analyzer@1.11.0") {
        doNotScan 'false'
    }
}
cfGovDeployJob.configure { Node project ->
    project / 'properties' /'org.jenkins.ci.plugins.html5__notifier.JobPropertyImpl'(plugin:"html5-notifier-plugin@1.3") {
        skip 'false'
    }
}
cfGovDeployJob.parameters {
    choiceParam(
            'DEPLOY_ENV',
            [
                    'Staging',
                    '***PRODUCTION(cfproweb01)***',
                    '***PRODUCTION(cfproweb02)***'
            ])
    booleanParam('DEPLOY_DJANGO', false)
    booleanParam('DEPLOY_WP', false)
}
cfGovDeployJob.buildFlow('''
        build("cf.gov-pfc-grunt-test")
        if(params.get("DEPLOY_DJANGO").equals("true")) {
            build("cf.gov-django-dev-build-master")
            build("cf.gov-fab-deploy-django", DEPLOY_ENV:params.get("DEPLOY_ENV"))
        }
        if(params.get("DEPLOY_WP").equals("true")) {
            //build("cf.gov-wordpress-unit-tests")
            build("cf.gov-fab-deploy-wordpress", DEPLOY_ENV:params.get("DEPLOY_ENV"))
        }

        // Run load tests after deployment
        //build("cf.gov-load-test-production")
        build("cf.gov-postdeploy-smoketests", TEST_ENV:params.get("DEPLOY_ENV"))''')

cfGovDeployJob.configure { Node project ->
    project / 'properties' /'hudson.plugins.disk__usage.DiskUsageProperty'(plugin:"disk-usage@0.24") {
    }

}

cfGovDeployJob.configure { Node project ->
    project / 'publishers' /'hudson.plugins.claim.ClaimPublisher'(plugin:"claim@2.5")

}
cfGovDeployJob.configure { Node project ->
    project /'buildNeedsWorkspace' << false
}