package jenkins.automation.utils


/**
 * Utility Class used to determine the environment at runtime
 * see example usage
 *
 * TODO: where is this example usage?
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

    //TODO: why do we need this? all it does is return the arg passed in
    static getEnv(Environment env) {
        try {
            return env
        } catch (all) {

            return all.getLocalizedMessage()
        }
    }

}
