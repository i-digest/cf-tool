# CF-tool 


CF-tool is a Java library for to automatically update system packages on virtualization hosts deployed based on Apache CloudStack. Working with the hypervisors is used through the CloudStack API.



# Artifacts

CF-tool can be used with a single bundle or smaller fine-grained jars. The bundle contains all jars that must be included in classpath.
The big bundle is named:

    cf-tool-${cf-tool.version}.jar

The dependencies are minimal: you may use CF-tool without any dependency on *nix.

## Supported platforms

CF-tool supports the following platforms:
* FreeBSD - (in development)
* Linux
* Mac OSx - (in development)

# Building

## Requirements

* Maven 3.3+
* Java 8+

Check out and build:

```sh
git clone git://github.com/i-digest/cf-tool.git
cd cf-tool
mvn clean install
 ```
## Results

The following artifacts are build:

    cf-tool/target/cf-tool-${cf-tool.version}.jar


Maven has a concept of `SNAPSHOT`. During development, the cf-tool version will always end with `-SNAPSHOT`, which means that the version is in development and not a release.

Note that all those artifacts are also installed in the local maven repository, so you will usually find them in the following folder: `~/.m2/repository/org/cftool/`.


## Usage

The library is launched at the terminal command line or via an SSH client and passed through the hypervisor node list parameters:

```sh
java -jar cf-tool-${cf-tool.version}.jar --hosts=node1,node2,node3
```
```
input options: 
--hosts             - accepts a comma-separated list of hosts (nodes)
--command[optional] - accepts a command and performs the specified operation on the list of hosts (nodes)
            List of commands: reboot                    - hosts (nodes) are rebooted with a check for successful loading
                              upgrade_system_packages   - updating system packages on hosts (nodes)
                              enable_maintenance        - puts hosts (nodes) into maintenance mode
                              disable_maintenance       - takes hosts (nodes) out of maintenance mode
```
When using the library with only a list of hosts (nodes), the following steps will be performed on each element of the list:
1. Switching to maintenance mode and moving virtual machines to another host (node) using the CloudStack API
2. Through the SSH client, a connection will be established and an operation will be performed to update system packages
3. The system will reboot and wait for a successful operation
4. Transfer from maintenance mode with the return of all virtual machines

If an error occurs at any stage, the error that occurred and during which step will be displayed in the console. After fixing the problem, you can continue the operation in manual mode through the transmission of commands.

## Configuration

All specific configuration for CloudStack API and for hosts (nodes) located in src/main/resources/application.yml ```src/main/resources/application.yml```
