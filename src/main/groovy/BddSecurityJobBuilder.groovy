import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job


class BddSecurityJobBuilder {

    String name
    String description
    String baseUrl

    Job build(DslFactory factory){
        factory.job(name){
            it.description this.description

            steps {
                shell("""umask 002
                        #export http_proxy=localhost:8888
                        #export https_proxy=localhost:8888
                        #export HTTP_PROXY=localhost:8888
                        #export HTTPS_PROXY=localhost:8888

                        /usr/bin/Xvfb :1 -ac -screen 0 1024x768x24 &
                        sleep 10
                        export DISPLAY=:1

                        #cd $HOME/ZAP_2.4.0
                        #java -jar zap-2.4.0.jar -daemon -host 127.0.0.1 -port 8888 &
                        #sleep 30



                        cd /var/lib/jenkins/workspace/${JOB_NAME}
                        sed -i 's/<zapPath>.*<\\/zapPath>/<zapPath>\\/var\\/lib\\/jenkins\\/workspace\\/'${JOB_NAME}'\\/zap\\/zap.sh<\\/zapPath>/g' config.xml
                        sed -i 's/<baseUrl><\\/baseUrl>/<baseUrl>${this.baseUrl}/baseUrl>/g' config.xml

                        ant resolve

                        ant jbehave.run""")

            }

        }
    }

}
