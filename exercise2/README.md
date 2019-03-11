# Exercise 2

## Programming

Build a program (in any language) for repetitive network scans displaying differences between
subsequent scans.
- scan can be executed either using external tools or using dedicated libraries of selected
programming language
- target of the scan must be parametrized as CLI argument
- target can be single IP address as well as network range

Example of expected result:

Initial scan:
```
$ ./scanner 10.1.1.1
*Target - 10.1.1.1: Full scan results:*
Host: 10.1.1.1 Ports: 22/open/tcp////
Host: 10.1.1.1 Ports: 25/open/tcp////
```

Repetitive scan with no changes on target host:
```
$ ./scanner 10.1.1.1
*Target - 10.1.1.1: No new records found in the last scan.*
```

Repetitive scan with changes on target host:
```
$ ./scanner 10.1.1.1
*Target - 10.1.1.1: Full scan results:*
Host: 10.1.1.1 Ports: 22/open/tcp////
Host: 10.1.1.1 Ports: 25/open/tcp////
Host: 10.1.1.1 Ports: 80/open/tcp////
```

## How to use network-scan application

### Requirements

- Java 8
- Maven 3.5 if you need to build application

### Build application from source

``mvn clean install``

You can specify the following parameters in the `src\main\resources\application.properties` path:
- `scanner.number_of_threads` - number of parallel threads to scan the network
- `scanner.port_min` - port number to start scanning
- `scanner.port_max` - port number to finish scanning
- `scanner.connection_timeout` - time in ms to wait answer from external service

### Run application

``java -jar network-scan-1.0.jar <ip-address-string>``

Possible values of `<ip-address-string>`:
- single ip-address (e.g. 8.8.8.8)
- multiple ip-addresses (e.g. 8.8.8.8,10.10.10.10)
- range of ip-addresses (e.g. 10.10.10.1-10.10.10.10)
