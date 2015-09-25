import JsJobBuilder


String basePath = 'example7'
List developers = ['irina.muchnik@cfpb.gov', 'daniel.davis@cfpb.gov']
def repos = [
        [name: 'jenkins-automation', url: "https://github.com/cfpb/jenkins-automation"],
        [name: 'collab', url: "https://github.com/cfpb/jenkins-automation"]
]
folder(basePath) {
    description 'This example shows how to create jobs using Job builders.'
}

new GrailsCiJobBuilder(
        name: "$basePath/grails-project1",
        description: 'An example using a job builder for a Grails project.',
        repos: repos,
        emails: developers,
).build(this)

