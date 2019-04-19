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
 *
 * <p>Basically, it follows the order in which the object names go ... to browse with that.
 */
package net.ugorji.oxygen.tool.facade.trees;

import java.util.*;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import net.ugorji.oxygen.tool.facade.FacadeContext;
import net.ugorji.oxygen.util.OxygenObjectArray;
import net.ugorji.oxygen.util.SimpleListBasedComparator;

public class SimpleJMXFacadeTreeModel extends JMXFacadeTreeModel {

  public static final List DEFAULT_HIERACHY;

  static {
    DEFAULT_HIERACHY = new ArrayList();
    DEFAULT_HIERACHY.add("type");
    DEFAULT_HIERACHY.add("j2eeType");
    DEFAULT_HIERACHY.add("name");
    DEFAULT_HIERACHY.add("Type");
    DEFAULT_HIERACHY.add("Name");
    DEFAULT_HIERACHY.add("Parent");
  }

  protected List paramHierachy;
  protected String domain;
  protected Comparator keysorder;
  protected String separator;
  protected boolean useKeyEqualsBeforeValueInPath;

  public SimpleJMXFacadeTreeModel(
      MBeanServerConnection mbs0,
      String domain0,
      String name0,
      String separator0,
      boolean useKeyEqualsBeforeValueInPath0,
      List paramHierachy0)
      throws Exception {
    super(mbs0, name0);
    domain = domain0;
    paramHierachy = paramHierachy0;
    keysorder = new SimpleListBasedComparator(paramHierachy);
    root = new ObjectName(domain + ":*");
    separator = separator0;
    useKeyEqualsBeforeValueInPath = useKeyEqualsBeforeValueInPath0;
  }

  public SimpleJMXFacadeTreeModel(
      MBeanServerConnection mbs0,
      String domain0,
      String name0,
      String separator0,
      boolean useKeyEqualsBeforeValueInPath0)
      throws Exception {
    this(mbs0, domain0, name0, separator0, useKeyEqualsBeforeValueInPath0, DEFAULT_HIERACHY);
  }

  public SimpleJMXFacadeTreeModel(
      MBeanServerConnection mbs0, String domain0, String name0, FacadeContext ctx)
      throws Exception {
    this(
        mbs0,
        domain0,
        name0,
        (TreeUtils.useKeyEqualsBeforeValueInPath(ctx, domain0) ? "," : "/"),
        TreeUtils.useKeyEqualsBeforeValueInPath(ctx, domain0),
        DEFAULT_HIERACHY);
  }

  public String getSeparator() {
    return separator;
  }

  public Object[] getChildren(Object parent) {
    Object[] files = (Object[]) cache.get(parent);
    if (files == null) {
      files = getChildren0(parent);
      cache.put(parent, files);
    }
    return files;
  }

  private Object[] getChildren0(Object parent) {
    // to find the children, we need to look at all mbeans, find the ones with this prefix,
    // get a set of all the values of the next key, and create a set of mbeans for those
    // then create a new set of object names, with just
    try {

      Set typevals = new HashSet();
      ObjectName oname = (ObjectName) parent;
      String onamestr = JMXUtils.getObjectNameString(oname, true);
      ObjectName onamepatt = new ObjectName(onamestr + "*");
      // System.out.println(onamestr);

      List l = JMXUtils.getSortedKeys(oname, keysorder);
      int lsize = l.size();
      // System.out.println("Get Children: lsize: " + lsize);
      Set names = JMXUtils.getAllRegistered(mbs, onamepatt, null);
      // System.out.println("|onamepatt: " + onamepatt + "|");
      for (Iterator itr = names.iterator(); itr.hasNext(); ) {
        ObjectName oname2 = (ObjectName) itr.next();
        // System.out.println("|oname2: " + oname2 + "|");
        List l2 = JMXUtils.getSortedKeys(oname2, keysorder);
        // skip if the same as the parent is returned in the query results
        if (l2.size() == lsize) {
          continue;
        }
        String s2 = (String) l2.get(lsize);
        // System.out.println("oname2: " + oname2 + " s2 " + s2);
        typevals.add(new OxygenObjectArray(new Object[] {s2, oname2.getKeyProperty(s2)}));
      }

      List kids = new ArrayList();
      for (Iterator itr = typevals.iterator(); itr.hasNext(); ) {
        OxygenObjectArray ooa = (OxygenObjectArray) itr.next();
        ObjectName oname3 = new ObjectName(onamestr + ooa.get(0) + "=" + ooa.get(1));
        kids.add(oname3);
      }

      return (ObjectName[]) kids.toArray(new ObjectName[0]);
    } catch (Exception exc) {
      throw new RuntimeException(exc);
    }

    //// THIS method below doesn't return the parents with no children really ////
    // try {
    //  ObjectName oname = (ObjectName)parent;
    //  FacadeChildQueryExp qexp = new FacadeChildQueryExp(keysorder, oname);
    //  Set names = JMXUtils.getAllRegistered(mbs, oname, qexp);
    //  return (ObjectName[])names.toArray(new ObjectName[0]);
    // } catch(Exception exc) {
    //  throw new RuntimeException(exc);
    // }
  }

  public Object getParent(Object child) throws Exception {
    ObjectName oname = (ObjectName) child;
    List l = JMXUtils.getSortedKeys(oname, keysorder);
    int lsize = l.size();
    if (lsize == 0) {
      return null;
    }
    if (lsize == 1) {
      return new ObjectName(oname.getDomain() + ":*");
    } else {
      String keyToRemove = (String) l.get(lsize - 1);
      l = JMXUtils.getKeys(oname);
      l.remove(keyToRemove);
      return JMXUtils.getObjectName(oname, l);
    }
    // return JMXUtils.checkRegistered(mbs, oname2);
  }

  public Object getChild(Object parent, String pathname) throws Exception {
    return (useKeyEqualsBeforeValueInPath
        ? getChild0(parent, pathname)
        : getChild1(parent, pathname));
  }

  private Object getChild0(Object parent, String pathname) throws Exception {
    ObjectName oname = (ObjectName) parent;
    String onamestr = JMXUtils.getObjectNameString(oname, true);
    onamestr = onamestr + pathname;
    // System.out.println("onamestr: " + onamestr);
    ObjectName oname2 = new ObjectName(onamestr);
    return oname2;
    // return JMXUtils.checkRegistered(mbs, oname2);
  }

  private Object getChild1(Object parent, String pathname) throws Exception {
    Object[] kids = getChildren(parent);
    List l1 = JMXUtils.getSortedKeys((ObjectName) parent, keysorder);
    for (int i = 0; i < kids.length; i++) {
      ObjectName oname = (ObjectName) kids[i];
      List l2 = JMXUtils.getSortedKeys(oname, keysorder);
      l2.removeAll(l1);
      if (oname.getKeyProperty((String) l2.get(0)).equals(pathname)) {
        return oname;
      }
    }
    return null;
  }

  public String toPathName(Object child) throws Exception {
    ObjectName oname = (ObjectName) child;
    List l = JMXUtils.getSortedKeys(oname, keysorder);
    int lsize = l.size();
    if (lsize == 0) {
      return null;
    } else {
      String s = (String) l.get(l.size() - 1);
      // return (s + "=" + oname.getKeyProperty(s));
      return (useKeyEqualsBeforeValueInPath
          ? (s + "=" + oname.getKeyProperty(s))
          : oname.getKeyProperty(s));
    }
    //// THIS BELOW IS ALL BOGUS
    // ObjectName oname = (ObjectName)child;
    // Hashtable ht = new Hashtable(oname.getKeyPropertyList());
    // List l = JMXUtils.getSortedKeys(oname, keysorder);
    // List l2 = new ArrayList();
    // for(Iterator itr = l.iterator(); itr.hasNext(); ) {
    //  String s = (String)itr.next();
    //  l2.add(h2.get(s));
    // }

  }
}
