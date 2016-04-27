import jenkins.automation.builders.SalesforceAntJobBuilder

List developers = ['irina.muchnik@cfpb.gov', 'daniel.davis@cfpb.gov']

def repo = "https://github.com/cfpb/saleforce-automation"

new SalesforceAntJobBuilder(
        name: "Deploy SF ",
        description: 'An example using a job builder for a Salesforce Ant JobBuilder build jobs project.',
        repoUrl: repo,
        emails: developers,
        antTaskName: ["retrieveUnpackaged"],
        antInstallerName:"ant-latest"
).build(this)

