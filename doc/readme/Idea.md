Idea
======================
##Java version
####Una tantum
- (mela ;) File -> Project settings -> Project -> Project SDK:
- (mela ;) File -> Project settings -> Project -> Project language level:
- (mela ,) IntelliJIDEA -> Preferences... -> Build, Execution, Deployment -> Compiler -> Java Compiler -> Project bytecode version
####Ogni volta che si ricompila Maven
- (mela ;) File -> Project Structure -> Modules -> language level 
- (mela ,) IntelliJIDEA -> Preferences... -> Build, Execution, Deployment -> Compiler -> Java Compiler -> Per-module bytecode version -> Target bytecode

##Node package manager
     For global installation:

        npm install --global <package_name>

        yarn global add <package_name>

        pnpm --global add <package_name>

    To install a package as a project dependency or a development dependency:

        npm install --save <package_name> or npm install --save-dev <package_name>

        yarn add <package_name> --dev

        pnpm add --save-dev <package_name>
- Pnpm [jetbrains](https://www.jetbrains.com/help/idea/installing-and-removing-external-software-using-node-package-manager.html#ws_npm_command_line_installation)
