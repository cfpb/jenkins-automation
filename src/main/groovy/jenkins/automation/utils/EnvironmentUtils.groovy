package jenkins.automation.utils

import javaposse.jobdsl.*


/**
 * Utility Class used to determine the environment at runtime
 */


class EnvironmentUtils {

/**
 *  Enum for Jenkins environments
 *  relies on ENVIRONMENT env variable in Jenkins
 */

    static isDev() {
        Environment e = getEnv().toUpperCase()
        return e == Environment.DEV
    }

    static isProd() {
        Environment e = getEnv().toUpperCase()
        return e == Environment.PROD
    }

    static getEnv() {
        try {
            String envDIDNOTWORK = System.getenv()
            def env =build.
            return env
        } catch (all) {

            return all.getLocalizedMessage()
        }
    }

}
