package net.ugorji.oxygen.tool.facade.trees;

import java.util.*;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.QueryExp;
import net.ugorji.oxygen.util.StringUtils;

public class JMXUtils {
  public static List getSortedKeys(ObjectName oname, Comparator comp) {
    List l = getKeys(oname);
    Collections.sort(l, comp);
    return l;
  }

  public static List getKeys(ObjectName oname) {
    // List l = new ArrayList(oname.getKeyPropertyList().keySet());
    List l = new ArrayList();
    String props = oname.getKeyPropertyListString();
    int ptr = 0;
    int eq = 0;
    // System.out.println("|Props: " + props + "|");
    while ((eq = props.indexOf("=", ptr)) != -1) {
      String key = props.substring(ptr, eq);
      String value = oname.getKeyProperty(key);
      // System.out.println("|key: " + key + " value: " + value + "|");
      l.add(key);
      // ptr = eq + key.length() + 1 + value.length() + 1;
      ptr = eq + value.length() + 1 + 1;
    }
    return l;
  }

  public static class FirstArrayElementComparator implements Comparator {
    public int compare(Object o1, Object o2) {
      Comparable oo1 = (Comparable) ((Object[]) o1)[0];
      Comparable oo2 = (Comparable) ((Object[]) o2)[0];
      return oo1.compareTo(oo2);
    }
  }

  public static ObjectName checkRegistered(MBeanServerConnection mbs, ObjectName oname)
      throws Exception {
    return checkRegistered(mbs, oname, null);
  }

  public static ObjectName checkRegistered(
      MBeanServerConnection mbs, ObjectName oname, QueryExp qexp) throws Exception {
    Set names = getAllRegistered(mbs, oname, qexp);
    ObjectName[] ons = (ObjectName[]) names.toArray(new ObjectName[0]);
    if (ons.length == 1) {
      return (ObjectName) ons[0];
    } else {
      throw new RuntimeException(
          "Expected 1 result for parent: mbeanserver query returned " + ons.length + " results");
    }
  }

  public static Set getAllRegistered(MBeanServerConnection mbs, ObjectName oname, QueryExp qexp)
      throws Exception {
    // System.out.println("|ObjectName: " + oname + "|");
    // when connecting remotely, passing a sub-class of QueryExp is not allowed for some funky
    // reason.
    // so, instead use the qexp on the client as below
    Set names0 = mbs.queryNames(oname, null);
    Set names = null;
    if (qexp == null) {
      names = names0;
    } else {
      names = new HashSet();
      for (Iterator itr = names0.iterator(); itr.hasNext(); ) {
        ObjectName oname2 = (ObjectName) itr.next();
        if (qexp.apply(oname2)) {
          names.add(oname2);
        }
      }
    }
    return names;
  }

  public static String getObjectNameString(ObjectName oname, boolean addCommaIfNecessary) {
    String s = oname.getKeyPropertyListString();
    if (StringUtils.isBlank(s) || "*".equals(s)) {
      s = oname.getDomain() + ":";
    } else {
      if (addCommaIfNecessary) {
        s = oname.getDomain() + ":" + s + ",";
      } else {
        s = oname.getDomain() + ":" + s;
      }
    }
    return s;
  }

  public static ObjectName getObjectName(ObjectName oname, List keysSubset) throws Exception {
    StringBuffer sb = new StringBuffer(oname.getDomain());
    sb.append(":");
    String skey = (String) keysSubset.get(0);
    sb.append(skey).append("=").append(oname.getKeyProperty(skey));
    for (int i = 1; i < keysSubset.size(); i++) {
      skey = (String) keysSubset.get(i);
      sb.append(",").append(skey).append("=").append(oname.getKeyProperty(skey));
    }
    // System.out.println("JMXUtils.getObjectName: " + sb.toString());
    return new ObjectName(sb.toString());
  }
}
