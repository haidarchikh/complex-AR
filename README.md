# Complex Activity Recognition
The system is built as a part of Project in mobile systems 2 [M7031E] course at Lulea University of Technology. The project architecture.

![](https://raw.github.com/haidarchikh/complex-AR/master/resources/Architecture.png)

The system consists of three layers. Sensor layer, Gateway layer and Management layer. The sensor layer gathers data from the environment and the gateway gives us means to push data over the network where the management layer process the data. The developed code architecture uses UNIX pipeline methodology; each component works as a pipeline with an input and output queue. This architecture gives us flexibility in experimentation. For example, using Weka to infer an activity requires collecting the data from an accelerometer, originate the data in ARFF format and extract features and classify the data.

##How to install  
1- Using terminal, clone the project into an arbitrary directory or just download the project as a zip file.
``` Shell
git clone https://github.com/haidarchikh/complex-AR
```
2- Step into complex-AR folder

3- Build the project
``` shell
gradle build
```
You will get
* Eclipse project that you can import (import existing project into workspace)

##Further Information
For further information please refer to the paper in this repository.
