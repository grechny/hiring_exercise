# Exercise 1
## Programming
Create a simple python script getweather.py with the following specs
- Retrieves weather data from https://openweathermap.org/api
- It uses the current weather data API: https://openweathermap.org/current
- It uses pyown: https://pypi.python.org/pypi/pyowm
- The python script uses no arguments, only the following environment variables:
  - OPENWEATHER_API_KEY
  - CITY_NAME
- It outputs to stdout

Example of acceptable results:
```
$ export
declare -x OPENWEATHER_API_KEY="xxxxxxxxxxxx"
declare -x CITY_NAME="Honolulu"
$ python getweather.py
source=openweathermap, city="Honolulu", description="few clouds", temp=70.2,
humidity=75
```
## Ansible
All steps below must be done using Ansible:
- Install the Docker service
- Enable container logging to Docker host's syslog file [1]
NOTE: Settings for privilege escalation and modularisation are acceptable (become: yes,
ansible-roles, etc)
Example of expected result:
```
$ docker
The program 'docker' is currently not installed. You can install it by typing:
sudo apt install docker.io
$ ansible-playbook -i "localhost," -c local site.yml
...
...
PLAY RECAP *********
localhost : ok=9 changed=1 unreachable=0 failed=0
$ docker -v
Docker version 17.12.0-ce, build c97c6d6
$ docker info | grep 'Logging Driver'
Logging Driver: syslog
```

## Docker
- Build a docker image (Dockerfile) configured to run as executable
- The docker container should pack the getweather.py script

Example of expected result:
```
$ docker run --rm -e OPENWEATHER_API_KEY="xxxxxxxxxxxx" -e CITY_NAME="Honolulu"
weather:dev
$ grep openweathermap /var/log/syslog
Nov 30 11:50:07 ubuntu-vm ae9395e86676[1621]: source=openweathermap,
city="Honolulu", description="few clouds", temp=70.2, humidity=75
```