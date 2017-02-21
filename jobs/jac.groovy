job('generate docs') {
    scm {
        git {
            remote {
                name('origin')
                branch('master')
                url("https://github.com/cfpb/jenkins-automation")
                credentials('009c8c9d-3cf5-4b2a-89f3-286977cabddf')
            }
            extensions {
                cleanBeforeCheckout()
                wipeOutWorkspace()
            }
        }
    }

    steps {

        gradle {
            tasks('groovydoc')
            useWrapper()
        }
        shell("""
            git add -A
            git commit -m'Jenkins autogenerate docs'
       """)
    }
    triggers {
        scm 'H/5 * * * *'
    }
    publishers {
        git {
            pushOnlyIfSuccess()
            pushMerge(false)
            forcePush(true)
            branch('origin', 'gh-pages')
        }
    }
}
