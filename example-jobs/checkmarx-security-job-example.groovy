import jenkins.automation.utils.CheckmarxSecurityJobBuilder

def projectName ='foo'
new CheckmarxSecurityJobBuilder(
        name: "${projectName}-checkmarx",
        description: "Sample checkmarx security job",
        useOwnServerCredentials: false,
        serverUrl: "http://awe-codescan-w-d01",
        groupId: "ac43cb0d-034d-4b1e-9bf5-e7c1c46f71d2",
        username: "CheckmarxUser",
        scanRepo: "https://github.com/OrlandoSoto/ckan-browser-tests",
        checkmarxComment: "Generated by Checkmarx job builder JAC",
        vulnerabilityThresholdEnabled: true,
        highThreshold: "1",
        mediumThreshold: "2",
        lowThreshold: "3",
        cleanWorkspace: true // Clean up the workspace before every checkout
).build(this);