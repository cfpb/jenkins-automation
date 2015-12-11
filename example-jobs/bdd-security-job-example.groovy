import jenkins.automation.builders.BddSecurityJobBuilder

def projectName ='foo'
new BddSecurityJobBuilder(
        name: "${projectName}bdd_security_job",
        description: "Sample bdd security job",
        baseUrl: "http://google.com"
).build(this);

