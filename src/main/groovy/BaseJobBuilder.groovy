import javaposse.jobdsl.dsl.DslFactory
class BaseJobBuilder {


    static void addColorizeOutput(context){

        context.with{
            colorizeOutput()
        }
    }

     static void addBaseStuff(context, emails) {
        context.with{
            wrappers{
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
