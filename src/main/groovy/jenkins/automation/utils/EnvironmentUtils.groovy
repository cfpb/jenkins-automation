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

    String env

    boolean isDev() {
        return env.toUpperCase() as Environment== Environment.DEV
    }

    boolean isProd() {
        return env.toUpperCase() as Environment == Environment.PROD
    }

    String getEnv() {
        try {
            return env.toLowerCase() as Environment
        } catch (all) {

            return all.getLocalizedMessage()
        }
    }

}
