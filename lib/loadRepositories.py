#!/usr/bin/env python
import yaml
import os
import sys
import subprocess


def print_msg(msg):
    cmd = 'echo {0}:'.format(msg)
    subprocess.call(cmd.split(' '))


def clone_repo(pinned_url, is_pinned, workspace_dir, path=None):
    url, version = pinned_url.split('@') if '@' in pinned_url else [pinned_url, None]
    if is_pinned and not version:
        raise Exception("You must explicitly pin a version (e.g. append `@v1.0`) to {0}".format(url))
    repo_parent_dir = os.path.join(workspace_dir, path) if path else workspace_dir
    repo_name = url.split('/')[4].replace(".git", "")
    repo_dir = os.path.join(repo_parent_dir, repo_name)
    if not os.path.exists(repo_parent_dir):
        os.makedirs(repo_parent_dir)
    
    print_msg("\nCloning or Updating {0}".format(repo_name))
    if not os.path.exists(repo_dir):
        cmd = 'git clone {0}'.format(url)
        subprocess.call(cmd.split(' '), cwd=repo_parent_dir)
    else:
        cmd = 'git remote set-url origin {0}'.format(url)
        subprocess.call(cmd.split(' '), cwd=repo_dir)
        cmd = 'git checkout master'
        subprocess.call(cmd.split(' '), cwd=repo_dir)
        cmd = 'git reset --hard'
        subprocess.call(cmd.split(' '), cwd=repo_dir)
        cmd = 'git pull --rebase'
        subprocess.call(cmd.split(' '), cwd=repo_dir)

    if is_pinned:
        if not version:
            raise Exception('url must be pinned to a particular commit, e.g. https://github.com/project/repo.git@hashOrTag')
        cmd = 'git checkout {0}'.format(version)
        subprocess.call(cmd.split(' '), cwd=repo_dir)

    return repo_name


def read_repo_data(repo_name, workspace_dir):
    """ Return repositories.yml data from an already-cloned repository """
    repo_path = os.path.join(workspace_dir, repo_name)
    repo_data_path = os.path.join(repo_path, 'ansible/group_vars/all/repositories.yml')
    try:
        with open(repo_data_path, 'r') as f:
            repo_data = yaml.load(f)
    except IOError:
        repo_data_path = os.path.join(repo_path, 'repositories.yml')
        try:
            with open(repo_data_path, 'r') as f:
                repo_data = yaml.load(f)
        except IOError: 
            return {}   # Ok to ignore, repo may not have a repositories.yml file

    return repo_data


def build_repo_data(repo_name, is_pinned, workspace_dir):
    """ Return merged repositories.yml data from both public and private repos """
    repo_data = read_repo_data(repo_name, workspace_dir)
    proj_url = repo_data.get('project_repository')
    if proj_url:
        proj_name = clone_repo(proj_url, is_pinned, workspace_dir)
        proj_data = read_repo_data(proj_name, workspace_dir)
        proj_data.update(repo_data)
        repo_data = proj_data
        if "private_repositories" in repo_data or "public_repositores" in repo_data:
            repo_data['repositories'] = repo_data.get("private_repositories", []) + repo_data.get("public_repositores", [])
    else:
        proj_name = repo_name

    return repo_data, proj_name


def load_repositories(repo_name, is_pinned, workspace_dir):
    repo_data, proj_name = build_repo_data(repo_name, is_pinned, workspace_dir)
    
    for pinned_url in repo_data['repositories']:
        clone_repo(pinned_url, is_pinned, workspace_dir, proj_name+'-repos')

    try:
        unitybox_repository = repo_data['unitybox_repository']
    except KeyError: 
        raise Exception("repositories.yml must contain an entry for unitybox_repository: e.g. `unitybox_repository: https://github.com/cfpb/Unitybox.git@xxx`")

    clone_repo(unitybox_repository, True, workspace_dir)

if __name__ == "__main__":
    repo_name = sys.argv[1]
    is_pinned = len(sys.argv) <= 2 or sys.argv[2].upper() not in ["FALSE", "0"]
    workspace_dir = os.environ['WORKSPACE']
    load_repositories(repo_name, is_pinned, workspace_dir)
