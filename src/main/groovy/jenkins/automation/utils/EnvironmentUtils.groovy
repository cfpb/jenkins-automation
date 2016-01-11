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

    private static final EnvironmentUtils instance = new EnvironmentUtils()
    String env

    private EnvironmentUtils() {
        // do nothing
    }

    public static EnvironmentUtils getInstance(String env) {
        instance.env = env
        return instance
    }


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