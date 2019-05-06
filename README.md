# java-facade

This repository contains the `java-facade` library.

Toolkit based on the command-pattern for scripting arbitrary product environments. 
Toolkit parses high-level commands and their implementation from an XML file and 
creates a scripting module that affords users highly interactive and customized 
sessions within disparate scripting environments (jython, groovy, jruby, etc). 

Before BEA Systems was acquired by Oracle, there were plans to use this 
as the framework driving the scripting of the WebLogic Server and 
WebLogic Real Time (CEP/Event Server).

## Dependencies

This repository is part of a multi-project `gradle` build.

It has the following dependencies:

- [java-common](https://github.com/ugorji/java-common)

Before building:

- clone the dependencies into adjacent folders directly under same parent folder
- download [`settings.gradle`](https://gist.githubusercontent.com/ugorji/2a338462e63680d117016793989847fa/raw/settings.gradle) into the parent folder

## Building

```sh
gradle clean
gradle build
```

## Running

For example, to run within a javascript interactive shell e.g. jjs, use:

```
jjs -cp "java-common.jar;java-facade.jar;commons-collections.jar;..."
```

## Usage

By default, this provides an interactive environment to navigate the JMX MBeans
in a running JVM. 

Consequently, this allows you to navigate the management objects for the 
applications running on that JVM.

An application server can then add more `nouns` to do more complex functions.

### General Navigation

Each domain in JMX is a tree within `java-facade`.

```
trees        show all trees. It returns an OxyTable, with a C beside the current Tree.
             In the OxyTable, show some pertinent info on the trees e.g. separator

tree         switch to a different tree. With no argument, just displays the current tree.

ls           show children
cd           go to a specific child. return that child. With no args, just return the current child.
             e.g. for mbs, it's the object name. for jndi, it's the String. for file, it's the file.
pwd          String representation of full path of the current node.
find         find a specific child
obj          return the object bound to a given child. 
             (e.g. for mbs, its the object we register with that object name)
             (for JNDI, it's the same)
             (for file, it's the file. i.e. for file, cd and pwd return the same thing)
explorer     shows a JTree in a JWindow for the tree
```

### Exploring the cursor object

At the point in the tree, an MBean is pointed to by the cursor. You can explore the 
`cursor` object using the commands:

```
atts         show attributes of current mbean
ops          show operations of current mbean
get          get value of an mbean
set          set value of an mbean
invoke       invoke an operation on an mbean
mbs          return the current mbean server connection
```

### Visual (GUI) exploring

Each tree can pop a GUI window showing file-explorer type navigation.

You can use the GUI to explore the tree, and each time you select a 
node in the tree, your cursor in your interactive CLI is updated.

This allows two-way navigation seamlessly.

### Programming using variables

At any point, you can get and set your own variables within the session:

```
setvars       if params, set them. returns the current set of vars.
unsetvars     if params, unset them. returns the current set of "unset" vars.
vars          returns the full set of available vars (whether set or unset)
```

### Tracking Errors

During an interactive session, we store the last error and its stacktrace.

This allows you pull up and view the error at any time (until a new error happens).

```
lasterror
lasterrorstacktrace
```

### Exiting

```
exit
```

### Getting Help

```
quickhelp
help
commands
commandcategories
```

## Caveats

Because we support multiple trees, there is no one way of determining what is universal
separator is. For example, the name of a node in JMX may have a / in it, so / is not a 
good separator for JMS. On the other hand, a / will never occur in the directory or 
file name so / is a good separator for a filesystem tree.

Consequently, each tree will determine what the separator is, or specify that there is none.
If there is none, then navigation will always be one at a time, and the concept of an 
absolute path does not exist for that tree.

As an example, we have `,` be a separator for JMX and `/` for file systems.

The user can configure what a separator will be for a given tree during that session.
This way, a user can set that the separator for their XYZ tree is `:`.

## Sample Usage

There are 3 tree in this sample below: JMX MBean server, JNDI tree and the file system.

```
MBS:  cd /Type=Name/Type=Name/Type=Name
   e.g. cd /Server=myserver/Application=myapp/Processor=myprocessor
JNDI: cd /weblogic/jms/MyJmsConnectionConsumer
FILE: cd /depot/internal/wls
```

The File tree is always bound to a directory or file. 
The MBS is bound to a MBean.
JNDI is bound to an object in the JNDI context.

Each tree has a FacadeContext bound to it. As you navigate it, we update the cursor
in that FacadeContext so we know what object to operate on.

Applications can introduce their own instances of FacadeContext, and their own commands
to allow for more custom operations. For example, a WebLogic Facade introduced the 
concept of a `connect` operation which is needed to connect to a WebLogic instance and 
see operations available.

Since multiple trees can exist, you can have a session where you have connected to 
a WebLogic instance, a Tomcat instance and some JNDI end-points. Your trees within that
singular session could look like:

```
filejmx:$domain
jmx:$host:$port:$domain
jndi:local
jndi:$host:$port
```

## Integrating into different languages 

There are a number of JVM-based programming languages that expose an interactive tool:

- JRuby
- Jython
- GraalVM
- Groovy

These can be used easily because java-facade exposes pure java objects that can be 
navigated in any language session.

Jython Example:

```
import fc
fc.trees()
fc.tree()
fc.setvars('showstacktrace')

fc.tree('file:C:')
fc.cd('Documents and Settings')
fc.cd('ugorjid')
fc.cd('.emacs.d')
fc.ls()
fc.pwd()

fc.cd('/')
fc.cd('/Documents and Settings/ugorjid/.emacs.d')
fc.pwd()
fc.ls()
```

JRuby example:

```
import fc
fc trees
fc tree
fc setvars showstacktrace

fc tree file:/
fc cd home
fc cd ugorji
fc ls
fc pwd

fc cd /
fc cd /home/ugorji
fc pwd
fc ls
```

More example using JRuby:

```
fc tree mbs:domain1
fc cd /
fc cd 'type=GarbageCollector'
fc pwd
fc ls
fc cd 'type=MemoryPool'
fc cd 'name=Code Cache'
fc pwd
fc ls

fc lasterror
fc lasterrorstacktrace

fc cd 'type=MemoryPool,name=Code Cache'
fc ops
fc atts
fc mbs

fc cd '/MemoryPool/Code Cache'
```

Connecting:

```
fc connect localhost 1099
fc trees
fc tree 'jmx:localhost:1099:java.lang'
fc ls
```

Examine connections in the current session:

```
fc connections
fc disconnect
fc connections
fc trees
```

To exit:

```
fc exit
```

# Implementation details

```
Implementation: 
Package net.ugorji.oxygen.tool.facade.tree;

FacadeTreeModel (holder implementation ... abstract)
  - adds a method: getParent(Object child)

  JMXFacadeTreeModel
  FileSystemFacadeTreeModel
  JNDIFacadeTreeModel

FacadeContext keeps a map of 
  String to FacadeTreeModel

net.ugorji.oxygen.tool.facade.ui;
  DisposableWindow
```

