package jenkins.automation.utils

/**
 * Utility Class used to determine the environment at runtime
 * see example usage
 */


class EnvironmentUtils {

/**
 *  Enum for Jenkins environments
 *  relies on ENVIRONMENT env variable in Jenkins
 */

    static isDev(String env) {
        return getEnv(env) == Environment.DEV
    }

    static isProd(String env) {
        return getEnv(env) == Environment.PROD
    }

    static isStage(String env) {
        return getEnv(env) == Environment.STAGE
    }

    static getEnv(String env) {
        return env.toUpperCase() as Environment
    }

}
