package jenkins.automation.utils

import groovy.lang.Script


/**
 * Utility Class used to determine the environment at runtime
 */


class EnvironmentUtils extends Expando {

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
            String env = System.getenv()
            return env
        } catch (all) {

            return all.getLocalizedMessage()
        }
    }

}
