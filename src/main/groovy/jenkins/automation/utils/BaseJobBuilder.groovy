package jenkins.automation.utils

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

class BaseJobBuilder {
    String name
    String description
    List<String> emails
    Boolean use_versions

    Job build(DslFactory factory){
        factory.job(name){
            it.description this.description
            addBaseStuff(delegate,this.emails)
        }
    }

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
                //TODO: make it parametrized to builder
                numToKeep(10)
            }
            publishers{
                allowBrokenBuildClaiming()
                mailer emails.join(' ')  //TODO use extended email
            }
        }

    }
}
