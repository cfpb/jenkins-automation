#!/usr/bin/env python
import os
import loadRepositories as repos


def get_proj_url(repo_url, workspace_dir):
    repo_name = repos.clone_repo(repo_url, False, workspace_dir, 'projs')
    repo_data = repos.load_repo_data('projs/' + repo_name, workspace_dir)
    proj_url = repo_data.get('project_repository')
    if proj_url:
        return proj_url.split('@')[0]
    else:
        return None


def get_project_repos(workspace_dir):
    repo_data = repos.load_repo_data('jenkins-automation-private', workspace_dir)
    repo_urls = repo_data['repositories']
    proj_urls = []
    for repo_url in repo_urls:
        proj_url = get_proj_url(repo_url, workspace_dir)
        if proj_url:
            proj_urls.append(proj_url)
    return repo_urls + proj_urls


if __name__ == "__main__":
    workspace_dir = os.environ['WORKSPACE']
    proj_repos = get_project_repos(workspace_dir)
    with open(os.path.join(workspace_dir, 'project_repos.txt'), 'w') as f:
        f.write('\n'.join(proj_repos))
