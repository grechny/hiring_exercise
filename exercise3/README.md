# Exercise 3

## Syslog configuration

Configure rsyslog service with the following settings:
- logging of default log files from /var/log/*
- logging of custom log files

## Ansible

Configuration must be executed using Ansible utilizing concept of Ansible roles. Ansible role should
accept the following parameters:
- logging only default log files
- logging custom files
- selecting external log server to send logs to

Example of expected result:
- proper contents of /etc/rsyslog.d/ folder
- logs properly delivered to external syslog server

## *Role usage example*:

```yaml
  vars:
    rsyslog_d_files:
      ufw:
        rules:
        - rule: ':msg,contains,"[UFW "'
          logpath: '/var/log/ufw.log'
        remote_host: "logserver.example.com"
        remote_port: "514"
        filters:
        - "*.*"
      haproxy:
        settings:
        # Create an additional socket in haproxy's chroot in order to allow logging via
        # /dev/log to chroot'ed HAProxy processes
        - '$AddUnixListenSocket /var/lib/haproxy/dev/log'
        rules:
        - rule: ':programname, startswith, "haproxy"'
          logpath: '/var/log/haproxy.log'
```
