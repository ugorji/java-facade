
Facade 

Highlights:
- Nothing tied to the environment
- Create something which can be used in standard java programs, but is really 
  powerful when used in interpreted and interactive scripting programs.
- Framework manages state and exceptions, and acts as a delegator so
  each method call goes thru a pre-defined lifecycle.

Package:
- net.ugorji.oxygen.tool.facade

Main class:
- net.ugorji.oxygen.tool.facade.FacadeCreator
- net.ugorji.oxygen.tool.facade.FacadeContext
- net.ugorji.oxygen.tool.facade.FacadePluginAdapter (convenience class, that plugins can extend)
- net.ugorji.oxygen.tool.facade.plugins.Sample
- net.ugorji.oxygen.tool.facade.plugins.Help

CLI:
- java net.ugorji.oxygen.tool.facade.Creator -d $outputdir -src $cmdInfoXmlFile

$cmdInfoXmlFile will reference the help file. And then we use standard ResourceBundle
methodologies to find the help (from within the Help plugin).

plugins can be stored under:
- net.ugorji.oxygen.tool.wlscript.facadeplugins.
Guidelines:
- plugins should not store state in the object
  instead, store state in the script context
- plugins should not print anything. Instead, they should return
  objects. if you want to print something, return it in an object
  whose toString() method can get shown.
- Plugins should typically not handle their exceptions. Instead, just
  let the exception propagate to the caller. We will inform the user,
  especially based on the variables they set (e.g. exitonerror, etc)

Lifecycle:
- void init(FacadeContext)
- void close()
# none of the perform methods are mandatory. You should typically have at least one
# perform method (since that does just about all the work)
- Object execute(List) throws Exception (take multiple arguments)
- Object execute(Map) throws Exception  (take named arguments)
- Object execute(arg1, arg2, ...) throws Exception     (take no arguments)


How to share state?
-------------------
FacadeContext object has some variables:
- int flags                    (read/write)
- Map attributes               (read/write)
- Throwable lastThrowable      (read)
- Set vars                     (read/write)
- Set implicitVars             (read e.g. exitonerror, etc)
# cancel implicitVars ... just use vars

Each *application* built on top of the OxygenFacade framework, should typically 
implement an Interactor class, which interacts with the FacadeContext. This 
Interactor class should typically have just static method.
E.g. 
  isConnected(FacadeContext)
  ...
All plugins should typically work with this *Interactor* class, not directly with
the FacadeContext, which is just a blind bucket for sharing state.

USAGE MODEL:
------------

import wl
wl.connect 'system' 'gumby1234' 't3://localhost:7001'
print wl.list()
print wl.lastError()
print wl.lastErrorStack()

# if using jython interactively, the returned object gets printed out
  automatically, so no need to type print first. However, for some
  other scripting environments, U may need to say print explicitly.

SAMPLE COMMAND INFO FILE:
-------------------------

<application>
  <general name="wl" classname="net.ugorji.oxygen.wiki.wl" help="net.ugorji.oxygen/wiki/wl.xml" />
  <param name="available_vars" value="a, b, c" />
  <param ... />
  
  <command name="" classname="" category="" />
  <command ... />

</application>

Help files support i18n. They are found via the classloader, as:
en/us/net.ugorji.oxygen/wiki/wl.xml, etc etc etc

Default is net.ugorji.oxygen/wiki/wl.xml

<application>
  <help command="" summary="" >
  </help>
  <help ... />
</application>

How to implement this?
----------------------

*Framework*
High level package called facade
Stored under java/net.ugorji.oxygen/tool/facade/...

*Application*
Have a seperate high level package, called wl
Stored under wlfacade/java/net.ugorji.oxygen/tool/wl/...
E.g. net.ugorji.oxygen.tool.wl.Connect, net.ugorji.oxygen.tool.wl.Disconnect, etc

Note that, we will cover just about every command which was in the old
wlscript side of things.

How to proceed?
---------------
Work item: October 25, 2004
1. Implement the framework classes.
2. Implement the *Application* introspector class.
3. Implement a first set of *Application* classes (and test it out)
   - connect, disconnect, pwd

Work item: October 26, 2004 (at night)
1. Implement a couple more highlevel plugins

By Friday, when I return, this should be completed.

Limitations:
-------------
- No support for primitives in your plugins
- No support for arrays in your plugins
- try not to return String. Instead, return Object whose toString() is what U want.
  e.g. ObjectWrapper
  Some scripting engines treat String badly.


