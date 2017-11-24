import jenkins.automation.builders.SauceConnectJobBuilder

def projectName ='foo'
new SauceConnectJobBuilder(
        name: "${projectName}-browser-tests",
        description: "Sample sauce connect job",
        emails: ['email1@server1.com', 'email2@server2.com'],
        sauceCredentialId: '1234',
        additionalOptions: "-v"
).build(this);