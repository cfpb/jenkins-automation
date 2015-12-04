import jenkins.automation.builders.SiteMonitorJobBuilder


new SiteMonitorJobBuilder(
        name: "my-site-pulse-check",
        description: "Sample url pulse check job",
        cronSchedule: "@daily",
        urls: ["http://google.com","http://yourethemannowdog.ytmnd.com/"]
).build(this);


new SiteMonitorJobBuilder(
        name: "my-site-pulse-check-without-schedule",
        description: "Sample url pulse check job without a schedule",
        cronSchedule: "",
        urls: ["http://google.com","http://yourethemannowdog.ytmnd.com/"]
).build(this);

