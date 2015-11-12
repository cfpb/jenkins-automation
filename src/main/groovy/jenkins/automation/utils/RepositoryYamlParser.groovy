package jenkins.automation.utils

import java.util.regex.Pattern
import java.util.regex.Matcher
import org.yaml.snakeyaml.Yaml
import org.ini4j.Ini

class RepositoryYamlParser {
     public static String[] parseRepositories(def reposText) {
        Yaml yaml = new Yaml()
        def obj = yaml.load(reposText)
        def reposArray = obj.get('repositories', obj.get('public_repositories', obj.get('private_repositories', [])))
        return reposArray.collect {    // don't use each!!!
            it.contains('@') ? it.substring(0, it.indexOf('@')) : it
        }
        return reposArray
    }

    static String getRepoOriginUrl(def context, def repoName) {
        def gitConfigStream = context.streamFileFromWorkspace(repoName + '/.git/config')
        def gitConfig = new Ini(gitConfigStream)
        return gitConfig.get('remote "origin"').get('url')
    }

    static String[] getRepositoryUrls(def context, def repoName) {
        def repositories, repositoryYml, privateReposFilePath, publicReposFilePath, projectName, privateName
        def privateMatcher = repoName =~ /(?<projectName>.*)-private/
        if (privateMatcher.matches()) {
            projectName = privateMatcher.group('projectName')
            privateName = repoName
            privateReposFilePath = './' + privateName + '/ansible/group_vars/all/repositories.yml'
        } else {
            projectName = repoName
        }
        publicReposFilePath = './' + projectName + '/ansible/group_vars/all/repositories.yml'
        repositories = []
        if (privateName) {
            repositories << getRepoOriginUrl(context, privateName)
            if (fileExistsInWorkspace(context, privateReposFilePath)) {
                repositories.addAll(parseRepositories(context.readFileFromWorkspace(privateReposFilePath)))
            }
        }
        if (projectName) {
            repositories << getRepoOriginUrl(context, projectName)
            if (fileExistsInWorkspace(context, publicReposFilePath)) {
                repositories.addAll(parseRepositories(context.readFileFromWorkspace(publicReposFilePath)))
            }
        }
        return repositories
    }

    static boolean fileExistsInWorkspace(def context, def path) {
        try {
            context.readFileFromWorkspace(path)
            return true
        } catch (java.lang.IllegalArgumentException e) {
            return false
        }
    }
}