#!/usr/bin/env python
import os
import loadRepositories as repos


def get_secondary_proj_url(repo_url, workspace_dir):
    repo_name = repos.clone_repo(repo_url, False, workspace_dir, 'projs')
    repo_data = repos.read_repo_data('projs/' + repo_name, workspace_dir)
    secondary_proj_url = repo_data.get('project_repository')
    if secondary_proj_url:
        return secondary_proj_url.split('@')[0]
    else:
        return None


def get_project_repos(workspace_dir):
    """ 
    Loads all project repositories listed in jenkins-automation-private,
    and any public project repositories specified in those repos
    """
    repo_data, proj_name = repos.build_repo_data('jenkins-automation-private', False, workspace_dir)
    proj_urls = repo_data['repositories']
    secondary_proj_urls = []
    for repo_url in proj_urls:
        secondary_proj_urls.append(get_secondary_proj_url(repo_url, workspace_dir))
    return proj_urls + filter(None, secondary_proj_urls)


if __name__ == "__main__":
    workspace_dir = os.environ['WORKSPACE']
    proj_repos = get_project_repos(workspace_dir)
    with open(os.path.join(workspace_dir, 'project_repos.txt'), 'w') as f:
        f.write('\n'.join(proj_repos))
