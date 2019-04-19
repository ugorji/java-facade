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

import java.util.Comparator;
import java.util.List;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.QueryExp;

public class FacadeChildQueryExp implements QueryExp {
  private ObjectName base;
  private Comparator keysOrder;
  private List sortedkeys;
  private int sortedkeyssize;

  public FacadeChildQueryExp(Comparator keysOrder0, ObjectName base0) {
    base = base0;
    keysOrder = keysOrder0;
    sortedkeys = JMXUtils.getSortedKeys(base, keysOrder);
    sortedkeyssize = sortedkeys.size();
  }

  public boolean apply(ObjectName oname) {
    // only accept if the number of keys is equal to 1 more than the number of keys in the first
    List l = JMXUtils.getSortedKeys(oname, keysOrder);
    int lsize = l.size();
    return ((lsize == sortedkeyssize + 1) && l.subList(0, sortedkeyssize).containsAll(sortedkeys));
  }

  public void setMBeanServer(MBeanServer mbs) {}
}
