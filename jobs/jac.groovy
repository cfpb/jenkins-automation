job('generate docs') {
    scm {
        git {
            remote {
                name('origin')
                url("https://github.com/cfpb/jenkins-automation")
            }
        }
    }

    steps{
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
            branch('origin','gh-pages')
        }
    }
}
gi