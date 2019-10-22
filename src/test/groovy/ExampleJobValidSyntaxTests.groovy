import groovy.io.FileType
import javaposse.jobdsl.dsl.*
import spock.lang.*

class ExampleJobValidSyntaxTests extends Specification {
  
    void 'test dev and prod jobs do not fail'() {

        given:
        JobManagement jm = new MemoryJobManagement()
        DslScriptLoader scriptLoader = new DslScriptLoader(jm)

        def params = jm.getParameters()

        List<File> files = []

        new File('example-jobs').eachFileRecurse(FileType.FILES) {
            if (it.name.endsWith('.groovy')) {
                files << it
            }
        }

        when:
        ['dev', 'stage', 'prod'].each { environment ->
            params['JAC_ENVIRONMENT'] = environment

            println "\nTesting jobs for with environment = $environment \n"

            files.each { file ->
                println file
                scriptLoader.runScript(file.text)
            }
        }

        then:

        assert true
        noExceptionThrown()

    }
}
