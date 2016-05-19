package jenkins.automation.builders

import javaposse.jobdsl.dsl.DslFactory
import javaposse.jobdsl.dsl.Job


/**
 * Bdd Security builder creates a default BDD security build configuration

 *
 * @param name job name
 * @param description job description
 * @param baseUrl url of the application to scan
 * @param bddSecurityRepo repo where BDD Security framework resides
 * @param chromedriverPath path to the Chromedriver binary
 *
 * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#bdd-security-job-builder" target="_blank">BDD job Example</a>
 *
 */
class BddSecurityJobBuilder {

    String name
    String description
    String baseUrl
    List<String> emails
    String bddSecurityRepo
    String chromedriverPath

    /**
     * The main job-dsl script that build job configuration xml
     * @param DslFactory
     * @return Job
     */
    Job build(DslFactory factory) {
        def baseJob = new BaseJobBuilder(
                name: this.name,
                description: this.description,
                emails: this.emails
        ).build(factory)

        baseJob.with {
            scm {
                git {
                    remote {
                        url(bddSecurityRepo)
                    }
                    branch('*/master')
                    extensions {
                        cloneOptions {
                            shallow()
                        }
                    }
                }
            }
        }

        baseJob.with {
            steps {
                shell("""umask 002
                        /usr/bin/Xvfb :1 -ac -screen 0 1024x768x24 &
                        sleep 10
                        export DISPLAY=:1

                        echo \${WORKSPACE}

                        cd \${WORKSPACE}
                        sed -i 's/<zapPath>.*<\\/zapPath>/<zapPath>zap\\/zap.sh<\\/zapPath>/g' config.xml
                        sed -i 's/<baseUrl><\\/baseUrl>/<baseUrl>${baseUrl}<\\/baseUrl>/g' config.xml

                        sed -i 's/<defaultDriver path.*/<defaultDriver path="${chromedriverPath}">Chrome<\\/defaultDriver>/g' config.xml

                        export JAVA_HOME=/opt/jdk1.8.0_91
                        export PATH=\$JAVA_HOME/bin:\$PATH
                        
                        ./gradlew test
            }
        }

        baseJob.with {
        
            /**
             * cucumber report plugin
             */
             
            configure { project ->
                project / publishers / 'com.github.bogdanlivadariu.jenkins.reporting.cucumber.CucumberTestReportPublisher' {
                    'fileIncludePattern'('build/reports/cucumber/*.json')
                }
            }
            
            /**
             * Another cucumber plugin to test
             */
             
            configure { project ->
                project / publishers / 'net.masterthought.jenkins.CucumberReportPublisher’ {
                    'fileIncludePattern'('build/reports/cucumber/*.json')
                }
            }
        }

        return baseJob
    }
}
