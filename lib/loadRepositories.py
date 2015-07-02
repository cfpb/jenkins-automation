#!/usr/bin/env python
import yaml
import os
import sys
import subprocess

def get_repo_dependencies(data):
    return data.get("repositories") or data.get("public_repositories") or data.get("private_repositories", [])


def print_msg(msg):
    cmd = 'echo {0}:'.format(msg)
    subprocess.call(cmd.split(' '))


def clone_repo(pinned_url, is_pinned, workspace_dir, path=None):
    url, version = pinned_url.split('@') if '@' in pinned_url else [pinned_url, None]
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


def load_repo_data(repo_name, workspace_dir):
    repo_path = os.path.join(workspace_dir, repo_name)
    repo_data_path = os.path.join(repo_path, 'ansible/group_vars/all/repositories.yml')
    try:
        with open(repo_data_path, 'r') as f:
            repo_data = yaml.load(f)
    except IOError:
        repo_data_path = os.path.join(repo_path, 'repositories.yml')
        with open(repo_data_path, 'r') as f:
            repo_data = yaml.load(f)
    return repo_data


def load_repositories(repo_name, is_pinned, workspace_dir):
    repo_data = load_repo_data(repo_name, workspace_dir)
    proj_url = repo_data.get('project_repository')
    repo_dependencies = get_repo_dependencies(repo_data)
    if proj_url:
        proj_name = clone_repo(proj_url, is_pinned, workspace_dir)
        proj_data = load_repo_data(proj_name, workspace_dir)
        repo_dependencies += get_repo_dependencies(proj_data)
    else:
        proj_name = repo_name

    for pinned_url in repo_dependencies:
        clone_repo(pinned_url, is_pinned, workspace_dir, proj_name+'-repos')


if __name__ == "__main__":
    repo_name = sys.argv[1]
    is_pinned = len(sys.argv) <= 2 or sys.argv[2].upper() not in ["FALSE", "0"]
    workspace_dir = os.environ['WORKSPACE']
    load_repositories(repo_name, is_pinned, workspace_dir)
