import javaposse.jobdsl.dsl.DslFactory
class BaseJobBuilder {

    List<String> emails


    static void addColorizeOutput(context){

        context.with{
            colorizeOutput()
        }
    }

     def addBaseStuff(context) {
        context.with{
            wrapper{
                addColorizeOutput(delegate)
            }
            logRotator {
                numToKeep(10)
            }
            publishers{
                mailer this.emails.join(' ')
            }
        }

    }
}
