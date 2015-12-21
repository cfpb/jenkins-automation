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
        return getEnv() as Environment== Environment.dev
    }

    boolean isProd() {
        return getEnv() as Environment == Environment.prod
    }

    boolean isStage() {
        return getEnv() as Environment == Environment.stage
    }

    String getEnv() {
        try {
            return env.toLowerCase() as Environment
        } catch (all) {
            return all.getLocalizedMessage()
        }
    }

}
