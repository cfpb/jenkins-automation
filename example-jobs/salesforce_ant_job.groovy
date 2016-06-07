import jenkins.automation.builders.SalesforceAntJobBuilder

List developers = ['jane@example.com', 'joe@example.com']

def repo = "https://github.com/cfpb/salesforce-automation"

new SalesforceAntJobBuilder(
        name: "example-salesforce-ant-job",
        description: 'An example using a job builder for a Salesforce Ant JobBuilder build jobs project.',
        repoUrl: repo,
        emails: developers,
        antTasks: ["retrieveUnpackaged", "publish"],
        antInstallerName:"ant-latest"
).build(this)

