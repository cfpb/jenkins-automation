package jenkins.automation.utils

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job

class FlowJobBuilder {
    List<String> jobs
    String name
    String description

    Job build(DslFactory factory){
        factory.buildFlowJob(name){
            it.description this.description

            String jobsToBuild =""

            jobs.each {jobName->
                jobsToBuild+= "build('${jobName}') \r\n"
            }
            buildFlow(jobsToBuild)
        }
    }


}
