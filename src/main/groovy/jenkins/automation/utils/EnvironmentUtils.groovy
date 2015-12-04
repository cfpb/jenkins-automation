package jenkins.automation.utils

import jenkins.automation.utils.Environment

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
        return e==Environment.DEV
    }

    static isProd() {
        Environment e = getEnv().toUpperCase()
        return e==Environment.PROD
    }

    static getEnv() {
        try {
            return "${ENVIRONMENT}"
        } catch (all) {
            println("WARNING!!!!! ENVIRONMENT variable not defined in main jenkins config!!!")
            return "dev"
        }
    }

}
