import jenkins.automation.utils.SiteMonitorJobBuilder

def urls = ["http://google.com","http://yourethemannowdog.ytmnd.com/"]

new SiteMonitorJobBuilder(
        name: "my-site-pulse-check",
        description: "Sample url pulse check job",
        urls: urls
).build(this);

