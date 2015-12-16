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
        return env.toUpperCase() as Environment== Environment.DEV
    }

    static isProd(String env) {
        return env.toUpperCase() as Environment == Environment.PROD
    }

    static getEnv(String env) {
        try {
            return env.toUpperCase() as Environment
        } catch (all) {

            return all.getLocalizedMessage()
        }
    }

}
