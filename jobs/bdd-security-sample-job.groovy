import BddSecurityJobBuilder

def projectName ='foo'
new BddSecurityJobBuilder(
        name: "${projectName}-bdd-security-job",
        description: this.description,
        baseUrl: "http://google.com"
).build(this);

