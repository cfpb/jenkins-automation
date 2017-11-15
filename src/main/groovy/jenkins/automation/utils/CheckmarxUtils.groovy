package jenkins.automation.utils


import java.util.Map


class CheckmarxUtils {

/**
 * Reusable block to add Checkmarx scanning to a project
 *
 * @param context A reference to the job object being modified
 *                (typically via <code>delegate</code>)
 * @param cxConfig A <code>Map</code> used to configure the Checkmarx project.
 *                 Valid keys:
 * <br/>&#x2014; <code>projectName</code>: required, specifies the name of the
 *               Checkmarx project
 * <br/>&#x2014; <code>groupId</code>: required, specifies group ID, which is
 *               actually the team ID, and which can be gotten by
 *               browser-inspecting the 'Team' select box for an existing
 *               Checkmarx job in the Jenkins UI
 * <br/>&#x2014; <code>preset</code>: optional, defaults to "Default 2014" preset,
 *               the ID of the Checkmarx preset configuration to use; this can be
 *               gotten by browser-inspecting the 'Preset' select box for an existing
 *               Checkmarx job in the Jenkins UI
 * <br/>&#x2014; <code>useOwnServerCredentials</code>: optional, defaults to
 *               <code>false</code>, specifies whether to use the global or per-job
 *               Checkmarx server URL/credentials
 * <br/>&#x2014; <code>serverUrl</code>: optional, specifies the Checkmarx server
 *               URL to be used
 * <br/>&#x2014; <code>username</code>: optional, specifies the Checkmarx username
 * <br/>&#x2014; <code>password</code>: optional, specifies the Checkmarx password
 * <br/>&#x2014; <code>excludeFolders</code>: optional, see checkmarxConfigDefaults
 *               for the default, specifies the directories to exclude from scanning
 * <br/>&#x2014; <code>filterPattern</code>: optional, see checkmarxConfigDefaults
 *               for the default, specifies globs to filter out
 * <br/>&#x2014; <code>incremental</code>: optional, defaults to <code>true</code>,
 *               specifies whether scan should be incremental
 * <br/>&#x2014; <code>fullScanCycle</code>: optional, defaults to <code>10</code>,
 *               specifies how frequently to run a full scan when normally doing
 *               incremental scans
 * <br/>&#x2014; <code>comment</code>: optional, additional comments to be adding
 *               to the scan results
 * <br/>&#x2014; <code>vulnerabilityThresholdEnabled</code>: optional, defaults to
 *               <code>true</code>, fail the build if the number of vulnerabilities
 *               exceeds the thresholds set
 * <br/>&#x2014; <code>highThresholdDefault</code>: optional, defaults to
 *               <code>1</code>, sets the threshold for max number of 'high'
 *               vulnerabilities
 * <br/>&#x2014; <code>mediumThresholdDefault</code>: optional, defaults to
 *               <code>2</code>, sets the threshold for max number of 'medium'
 *               vulnerabilities
 * <br/>&#x2014; <code>lowThresholdDefault</code>: optional, defaults to
 *               <code>3</code>, set the threshold for max number of 'low'
 *               vulnerabilities
 * @see <a href="https://github.com/cfpb/jenkins-automation/blob/gh-pages/docs/examples.md#checkmarx-security-job-builder" target="_blank">Checkmarx job Example</a>
 *
 */

    static void checkmarxScan(context, Map cxConfig) {
        assert cxConfig.projectName != null
        assert cxConfig.groupId != null

        def defaults = checkmarxConfigDefaults

        context.with {
            configure {
                it / builders / "com.checkmarx.jenkins.CxScanBuilder" {

                    "useOwnServerCredentials"(
                        cxConfig.get(
                            "useOwnServerCredentials",
                            defaults.useOwnServerCredentials
                        )
                    )

                    if (cxConfig.useOwnServerCredentials) {
                        "serverUrl"(cxConfig.serverUrl)
                        "username"(cxConfig.username)
                        "password"(cxConfig.password)
                    }

                    "projectName"(cxConfig.projectName)
                    "groupId"(cxConfig.groupId) // really the Checkmarx "team"
                    "preset"(cxConfig.get("preset", defaults.preset))

                    // TODO figure out whether this does anything
                    "presetSpecified"(false)

                    // TODO probably this should be configurable
                    "exclusionsSetting"("job")

                    "excludeFolders"(
                        cxConfig.get("excludeFolders", defaults.excludeFolders)
                    )
                    "filterPattern"(
                        cxConfig.get("filterPattern", defaults.filterPattern)
                    )

                    "incremental"(
                        cxConfig.get("incremental", defaults.incremental)
                    )

                    // TODO is this right?
                    "fullScansScheduled"(false)
                    "fullScanCycle"(
                        cxConfig.get("fullScanCycle", defaults.fullScanCycle)
                    )

                    // TODO should we really be setting this?
                    "isThisBuildIncremental"(false)

                    "sourceEncoding"("1") // this is the default

                    "comment"(cxConfig.get("comment", defaults.comment))

                    // TODO probably this should be configurable
                    "skipSCMTriggers"(false)
                    "waitForResultsEnabled"(true)

                    "generatePdfReport"(true)

                    "vulnerabilityThresholdEnabled"(
                        cxConfig.get(
                            "vulnerabilityThresholdEnabled",
                            defaults.vulnerabilityThresholdEnabled
                        )
                    )

                    "highThreshold"(
                        cxConfig.get("highThreshold", defaults.highThreshold)
                    )
                    "mediumThreshold"(
                        cxConfig.get("mediumThreshold", defaults.mediumThreshold)
                    )
                    "lowThreshold"(
                        cxConfig.get("lowThreshold", defaults.lowThreshold)
                    )

                    // build failing won't work w/o this block
                    "vulnerabilityThresholdResult" {
                        "name"("FAILURE")
                        "ordinal"("2")
                        "color"("RED")
                        "completeBuild"("true")
                    }
                }
            }
        }
    }

/**
 * A map defining the default Checkmarx config values used in `checkmarxScan`.
*/
    static Map checkmarxConfigDefaults = [
        useOwnServerCredentials: false,
        preset: "17", // "Default 2014"
        excludeFolders: "resources, .git",
        incremental: true,
        fullScanCycle: "10",
        comment: "",
        vulnerabilityThresholdEnabled: true,
        highThreshold: 1,
        mediumThreshold: 2,
        lowThreshold: 3,
        filterPattern: """
            !**/_cvs/**/*, !**/.svn/**/*,   !**/.hg/**/*,   !**/.git/**/*,  !**/.bzr/**/*, !**/bin/**/*,
            !**/obj/**/*,  !**/backup/**/*, !**/.idea/**/*, !**/*.DS_Store, !**/*.ipr,     !**/*.iws,
            !**/*.bak,     !**/*.tmp,       !**/*.aac,      !**/*.aif,      !**/*.iff,     !**/*.m3u, !**/*.mid, !**/*.mp3,
            !**/*.mpa,     !**/*.ra,        !**/*.wav,      !**/*.wma,      !**/*.3g2,     !**/*.3gp, !**/*.asf, !**/*.asx,
            !**/*.avi,     !**/*.flv,       !**/*.mov,      !**/*.mp4,      !**/*.mpg,     !**/*.rm,  !**/*.swf, !**/*.vob,
            !**/*.wmv,     !**/*.bmp,       !**/*.gif,      !**/*.jpg,      !**/*.png,     !**/*.psd, !**/*.tif, !**/*.swf,
            !**/*.jar,     !**/*.zip,       !**/*.rar,      !**/*.exe,      !**/*.dll,     !**/*.pdb, !**/*.7z,  !**/*.gz,
            !**/*.tar.gz,  !**/*.tar,       !**/*.gz,       !**/*.ahtm,     !**/*.ahtml,   !**/*.fhtml, !**/*.hdm,
            !**/*.hdml,    !**/*.hsql,      !**/*.ht,       !**/*.hta,      !**/*.htc,     !**/*.htd, !**/*.war, !**/*.ear,
            !**/*.htmls,   !**/*.ihtml,     !**/*.mht,      !**/*.mhtm,     !**/*.mhtml,   !**/*.ssi, !**/*.stm,
            !**/*.stml,    !**/*.ttml,      !**/*.txn,      !**/*.xhtm,     !**/*.xhtml,   !**/*.class, !**/*.iml, !Checkmarx/Reports/*.*
        """.stripIndent(),
    ]

}
