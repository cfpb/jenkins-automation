import jenkins.automation.builders.FrontEndTestJobBuilder


String basePath = 'JsJobSamples'
List developers = ['jane@example.com', 'joe@example.com']

def repos = [
        [name: 'jenkins-automation', url: "https://github.com/cfpb/jenkins-automation@2.0"],
        [name: 'collab', url: "https://github.com/cfpb/jenkins-automation"]
]
folder(basePath) {
    description 'This example shows how to create jobs using Job builders.'
}


new FrontEndTestJobBuilder(
        name: "$basePath/BuilderVsBuilders",
        description: 'An example using a job builder for a Javascript build jobs project.',
        repos: repos,
        emails: developers,
        use_versions: true
//        some more testparams
).build(this)

