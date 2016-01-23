package jenkins.automation.utils

/**
 * Utils class when most reused common properties should live.
 *  Adds a minimum base functionality required -build claiming, notifications and log.
 * @param context  delegate passed in context\
 */
class CommonUtils {
    static void addDefaults(context) {
        context.with {
            wrappers {
                colorizeOutput()
            }
            logRotator {
                numToKeep(30)
            }
            publishers {
                allowBrokenBuildClaiming()
            }
        }
    }
}
