import jenkins.automation.utils.FlowJobBuilder

def cfGovDeployJob = new FlowJobBuilder(
        name: 'cf.gov',
        description: 'CF.gov master build flow job',
        jobs: []
).build(this)


cfGovDeployJob.parameters {
    booleanParam('DEPLOY_DJANGO', false)
    booleanParam('DEPLOY_WP', false)
    choiceParam('OPTION',
            [
                    'Staging',
                    '***PRODUCTION(cfproweb01)***',
                    '*** PRODUCTION(cfproweb02)*** '
            ])
}
cfGovDeployJob.buildFlow('''
            build("cf.gov-pfc-grunt-test")
            if (params.get("DEPLOY_DJANGO").equals("true")) {
                //build("cf.gov-django-dev-build-master")
                build("cf.gov-fab-deploy-django", DEPLOY_ENV: params.get("DEPLOY_ENV"))
            }

            if (params.get("DEPLOY_WP").equals("true")) {
                //build("cf.gov-wordpress-unit-tests")
                build("cf.gov-fab-deploy-wordpress", DEPLOY_ENV: params.get("DEPLOY_ENV"))
            }

            // Run load tests after deployment
            //build("cf.gov-load-test-production")
            build("cf.gov-postdeploy-smoketests", TEST_ENV: params.get("DEPLOY_ENV"))
    ''')