/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

/**
 * This acts just like the JConsole. Superclasses can act different at will e.g. a superclass that
 * understands WLS style of hierachy. All it will have to do is re-implement getChildren and
 * getParent.
 */
package net.ugorji.oxygen.tool.facade.trees;

import java.io.File;
import java.util.BitSet;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

public abstract class JMXFacadeTreeModel extends FacadeTreeModel {
  // protected static String NON_KEY = "1=1";

  protected MBeanServerConnection mbs;
  protected ObjectName root;
  protected String name;

  public JMXFacadeTreeModel(MBeanServerConnection mbs0, String name0) throws Exception {
    mbs = mbs0;
    name = name0;
  }

  public Object getChild(Object parent, int index) {
    return getChildren(parent)[index];
  }

  public int getChildCount(Object parent) {
    return getChildren(parent).length;
  }

  public int getIndexOfChild(Object parent, Object child) {
    File[] files = (File[]) getChildren(parent);
    for (int i = 0; i < files.length; i++) {
      if (files[i].equals(child)) {
        return i;
      }
    }
    return -1;
  }

  public Object getRoot() {
    return root;
  }

  public boolean isLeaf(Object node) {
    return false;
  }

  public String getName() {
    return name;
    // return "jmx:" + domain;
  }

  public Object getBackendObject(Object child) throws Exception {
    ObjectName oname = (ObjectName) child;
    String s = mbs.getObjectInstance(oname).getClassName();
    Class clazz = Class.forName(s, true, getClass().getClassLoader());
    return MBeanServerInvocationHandler.newProxyInstance(mbs, oname, clazz, false);
    // if(clazz.isInterface()) {
  }

  public MBeanServerConnection getMBeanServerConnection() {
    return mbs;
  }

  public BitSet getChildBitMask(Object child) throws Exception {
    // no concept of bit sets for children of an mbean ... :(
    BitSet bs = new BitSet(3);
    bs.set(0);
    bs.set(1);
    return bs;
  }
}
