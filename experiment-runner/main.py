import os
import os.path as path
import re
import time


# def wait_until_match(stream, pattern):
#     while True:
#         line = stream.readline()
#         print(line.strip())
#         if len(line) == 0:
#             return False
#
#         if pattern.search(line):
#             return True


# def read_all(stream):
#     while True:
#         line = stream.readline()
#         if len(line) == 0:
#             return
#         print(line)

#
# def job_finished(job_name):
#     data = os.popen('kubectl get pod -o=json').read()
#     response = json.loads(data)
#
#     for item in response['items']:
#         meta = item['metadata']
#         ref = meta['ownerReferences'][0]
#         if ref['name'] != job_name or ref['kind'] != 'Job':
#             continue
#         return item['status']['phase'] == "Succeeded"
#
#     return False


# def pod_ready(service_name):
#     data = os.popen('kubectl get pod -o=json').read()
#     response = json.loads(data)
#
#     for item in response['items']:
#         meta = item['metadata']
#         labels = meta['labels']
#         if 'app' not in labels or labels['app'] != service_name:
#             continue
#
#         ref = meta['ownerReferences'][0]
#         if ref['kind'] != 'ReplicaSet':
#             continue
#
#         return item['status']['phase'] == "Running"
#     return False


def follow_until_match(service_name, pattern, selector=''):
    stream = os.popen(f'kubectl logs -f service/{service_name} {selector}')

    while True:
        line = stream.readline()
        print(line.strip())

        if len(line) == 0:
            return False

        if pattern.search(line):
            return True


def wait_until(fn, *args):
    while not fn(*args):
        time.sleep(1)


def deploy_chart(operations_dir, name, config, *value_files):
    chart_path = path.join(operations_dir, name)
    cmd = ''
    if len(value_files) != 0:
        value_paths = [path.join(operations_dir, name, file) for file in value_files]

        values = "-f ".join(value_paths)
        cmd = f'helm install -f {config} -f {values} {name} {chart_path}'
    else:
        cmd = f'helm install -f {config} {name} {chart_path}'
    print(cmd)
    response = os.popen(cmd).read()
    print(response)


def nuke():
    cmd = 'helm uninstall $(helm list --short)'
    print(cmd)
    print(os.popen(cmd).read())


def follow_logs_stream(service_name, selector=''):
    return os.popen(f'kubectl logs -f service/{service_name} {selector}')


def has_status(name, state):
    def contains(string, value):
        return string.find(value) != -1

    for line in os.popen('kubectl get pods').readlines():
        if contains(line, name) and contains(line, state):
            return True
    return False


def completed(name):
    return has_status(name, 'Completed')


def running(name):
    return has_status(name, 'Running')


def all_cleared():
    data = os.popen('kubectl get pods').readlines()
    print(data)
    return len(data) == 0


def get_pod_name(name):
    for line in os.popen('kubectl get pods').readlines():
        if line.startswith(name):
            return line.split(" ")[0]
    return False


def fetch_results(name, experiment, result_dir):
    # give zipkin some time to transfer data
    time.sleep(10)

    pod = get_pod_name(name)
    base = path.basename(experiment)
    exp_name = path.splitext(base)[0]

    target = path.join(result_dir, exp_name)
    cmd = f'kubectl cp default/{pod}:results {target}'
    print(cmd)
    print(os.popen(cmd).read())

    setup = open(experiment, 'r').read()
    open(path.join(target, 'setup.yaml'), 'w').write(setup)


def execute_experiment(op_dir, experiment, result_dir):
    print('deploying keycloak')
    deploy_chart(op_dir, 'keycloak', experiment)
    wait_until(running, 'keycloak')
    print('keycloak ready')

    print('deploying diagnostics')
    deploy_chart(op_dir, 'diagnostics', experiment)
    wait_until(running, 'debug')
    wait_until(running, 'zipkin')
    print('diagnostics ready')

    print('running fill-keycloak job')
    deploy_chart(op_dir, 'fill-keycloak', experiment)
    wait_until(completed, 'fill-keycloak')
    print('finished filling keycloak')

    print('deploying gateway')
    deploy_chart(op_dir, 'gateway', experiment)
    wait_until(running, 'gateway')
    print('gateway deployed')

    print('deploying account-state')
    deploy_chart(op_dir, 'account-state', experiment, 'values.aks.dbPassword.yaml')

    pattern = re.compile('(:?finished provisioning|keeping old records)')
    while True:
        wait_until(running, 'account-state')
        if path.basename(experiment).startswith('p'):
            follow_until_match('account-state', pattern, selector='account-state')
        else:
            follow_until_match('account-state', pattern)

        time.sleep(2)
        # check if didn't crash in the meantime
        if running('account-state'):
            break

    print('account-state service deployed and filled')

    print('pushing policies')
    deploy_chart(op_dir, 'policy', experiment)
    wait_until(completed, 'policy-pusher')
    print('policies pushed')

    print('starting locust')
    deploy_chart(op_dir, 'locust', experiment)
    wait_until(running, 'locust')
    print('experiments started')

    pattern = re.compile('Time limit reached. Stopping Locust.')
    follow_until_match('locust', pattern)
    print('locust finished')

    print('pulling data')
    fetch_results('locust', experiment, result_dir)
    print('done fetching results')

    print('cleaning up all charts')
    nuke()
    print('waiting for cleanup')

    wait_until(all_cleared)


def file_names(dir):
    files = [path.join(dir, f) for f in os.listdir(dir) if path.isfile(path.join(dir, f))]
    files.sort()
    return files


def execute_experiments(op_dir, experiments_dir, results_dir):
    for exp_conf in file_names(experiments_dir):
        execute_experiment(op_dir, exp_conf, results_dir)


execute_experiments('../operations', './experiments', './results')