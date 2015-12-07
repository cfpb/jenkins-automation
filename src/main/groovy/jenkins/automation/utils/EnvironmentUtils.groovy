package jenkins.automation.utils

import  groovy.lang.*


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
            def configuration = new HashMap()


            def binding = Script.getBinding()
            configuration.putAll(binding.getVariables())

            String env = configuration["ENVIRONMENT"]
            return "${configuration}"
        } catch (all) {

            return all.getLocalizedMessage()
        }
    }

}
