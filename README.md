# Intro
This repository is intended for developing and debugging Groovy scripts for Boomi in a dev container environment via Microsoft Visual Studio Code. 
It is also possible to do from command line if you are familiar with that. 

# Tools
Docker Desktop  
Visual Studio Code

## Plugins in VSCode
Dev Containers

# HowTo
1. Clone this repository
2. In VS Code, run the command "Dev Containers: Open Folder in Container..."
3. Find and open the folder BoomiGroovyTool
4. Open sample.groovy
5. Open a new terminal and run groovy -cp out/artifacts/BoomiGroovyTool.jar sample.groovy
