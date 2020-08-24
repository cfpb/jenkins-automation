import jenkins.automation.utils.CommonUtils

def emails = 'foo@example.com,  bar@example.com'
def triggers = ['SUCCESS']
def sendToDevelopers = true
def sendToRequester = false
def includeCulprits = false
def sendToRecipient = false

job("example") {
    publishers {
        CommonUtils.addExtendedEmail(delegate, emails = 'foo@example.com, bar@example.com')
    }
}

// override accepts emails as a list. Compatible with builders
job('example email list') {
    publishers {
        CommonUtils.addExtendedEmail(delegate, emails = ['foo@example.com', 'bar@example.com'])
    }
}

// Override default email triggers.
job('example email with default triggers') {
    publishers {
        CommonUtils.addExtendedEmail(delegate, emails = ["foo@example.com"], triggers = ['statusChanged'])
    }
}

// Override default email pre-send script, by providing a groovy code
job('example email with presend script') {
    publishers {
        CommonUtils.addExtendedEmail(delegate, emails, triggers, sendToDevelopers, sendToRequester, includeCulprits, sendToRecipient, "cancel = true")
    }
}

// Override default email pre-send script by providing path to the script file
job('example script by providing path to the script file') {
    publishers {
        CommonUtils.addExtendedEmail(delegate,, emails, triggers, sendToDevelopers, sendToRequester, includeCulprits, sendToRecipient, "\${SCRIPT, template='path/to/script.groovy'}")
    }
}
// Override using configuration map, a little nicer way to invoke the function with named arguments
job('example using configuration map') {
    publishers {
        CommonUtils.addExtendedEmail(emails: 'foo@example.com', triggers: ['statusChanged'], attachmentPattern: ".csv", delegate,)
    }
}
// Override the default content and customize subject
job('example using Override the default content and customize subject') {
    publishers {
        CommonUtils.addExtendedEmail(emails: 'foo@example.com', content: "foo", subject: "bar", delegate)
    }
}

