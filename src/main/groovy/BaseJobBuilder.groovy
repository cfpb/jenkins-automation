import javaposse.jobdsl.dsl.DslFactory
class BaseJobBuilder {


    static void addColorizeOutput(context){

        context.with{
            colorizeOutput()
        }
    }

     def addBaseStuff(context, emails) {
        context.with{
            wrapper{
                addColorizeOutput(delegate)
            }
            logRotator {
                numToKeep(10)
            }
            publishers{
                mailer emails.join(' ')
            }
        }

    }
}
