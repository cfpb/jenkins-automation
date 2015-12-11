package jenkins.automation.utils

import javaposse.jobdsl.*


/**
 * Utility Class used to determine the environment at runtime
 * see example usage
 */


class EnvironmentUtils {

/**
 *  Enum for Jenkins environments
 *  relies on ENVIRONMENT env variable in Jenkins
 */

    static isDev(Environment env) {
        return env == Environment.DEV
    }

    static isProd(Environment env) {
        return env == Environment.PROD
    }

    static getEnv(Environment env) {
        try {
            return env
        } catch (all) {

            return all.getLocalizedMessage()
        }
    }

}
