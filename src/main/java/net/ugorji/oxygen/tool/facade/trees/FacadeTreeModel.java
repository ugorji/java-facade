/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade.trees;

import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public abstract class FacadeTreeModel implements TreeModel {
  protected CacheMap cache;
  protected Object currentNode;

  protected FacadeTreeModel() {
    cache = this.new CacheMap();
  }

  public abstract String getName();

  public abstract Object getParent(Object child) throws Exception;

  public abstract Object[] getChildren(Object parent);

  public abstract String toPathName(Object child) throws Exception;

  public abstract Object getChild(Object parent, String pathname) throws Exception;

  public abstract Object getBackendObject(Object child) throws Exception;

  public abstract BitSet getChildBitMask(Object child) throws Exception;

  public char[] getChildBitMaskRepresentation() throws Exception {
    return new char[] {'d', 'r', 'w'};
  }

  public String getSeparator() {
    return "/";
  }

  public String getUpRepresentation() {
    return "..";
  }

  public Object getCurrentNode() {
    return (currentNode == null ? getRoot() : currentNode);
  }

  public void setCurrentNode(Object o) {
    currentNode = o;
  }

  public String toString() {
    return getName();
  }

  public void addTreeModelListener(TreeModelListener l) {}

  public void removeTreeModelListener(TreeModelListener l) {}

  public void valueForPathChanged(TreePath path, Object newValue) {}

  protected int getMaxCacheSize() {
    return 1;
  }

  protected class CacheMap extends LinkedHashMap {
    protected boolean removeEldestEntry(Map.Entry eldest) {
      return size() > getMaxCacheSize();
    }
  }
}
