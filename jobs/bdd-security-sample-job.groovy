import BddSecurityJobBuilder

def projectName ='foo'
new BddSecurityJobBuilder(
        name: "${projectName}bdd_security_job",
        description: this.description,
        baseUrl: "http://google.com"
).build(this);

