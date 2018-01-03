import jenkins.automation.builders.BaseJobBuilder
import jenkins.automation.utils.GhUtils

new BaseJobBuilder(
    name: 'GH_PR_builder',
    description: 'Does GH PR building',
).build(this).with {
    GhUtils.ghPrWatcher(
        delegate,
        [
            ghProject: 'cfpb/reponame',
            ghHostname: 'github.org.tld',
            ghAuthId: '<unique GH PR builder credential ID>',
            ghPrHooks: false,
            ghPrCron: '*/2 * * * *',
            ghPrOrgsList: 'CFPB',
            ghPrStatusContext: 'Test your example job',
            ghPrResultMessage: [
              'SUCCESS': 'Tests completed normally',
              'ERROR': 'Tests errored',
              'FAILURE': 'Tests failed',
            ]
        ]
    )

    steps {
        shell('''
        echo Testing
        '''.stripIndent()
        )
    }
}
