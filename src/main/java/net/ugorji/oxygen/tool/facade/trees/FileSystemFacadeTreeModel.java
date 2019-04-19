/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade.trees;

import java.io.File;
import java.util.BitSet;
import net.ugorji.oxygen.util.StringUtils;

public class FileSystemFacadeTreeModel extends FacadeTreeModel {
  private File root;
  private String name;

  public FileSystemFacadeTreeModel(File root0) throws Exception {
    root = root0;
    String s = root.getCanonicalPath();
    s = StringUtils.trim(s, '\\');
    s = StringUtils.trim(s, '/');
    s = StringUtils.trim(s, ':');
    s = (StringUtils.isBlank(s) ? "file" : ("file:" + s));
    name = s;
  }

  public Object[] getChildren(Object parent) {
    File[] files = (File[]) cache.get(parent);
    if (files == null) {
      files = ((File) parent).listFiles();
      cache.put(parent, files);
    }
    return files;
  }

  public Object getParent(Object child) {
    File f = (File) child;
    return f.getParentFile();
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
    return !(((File) node).isDirectory());
  }

  public Object getBackendObject(Object child) throws Exception {
    return child;
    // return ((File)child).getCanonicalFile();
  }

  public Object getChild(Object parent, String pathname) throws Exception {
    return new File((File) parent, pathname);
  }

  public String toPathName(Object child) throws Exception {
    return ((File) child).getName();
  }

  public BitSet getChildBitMask(Object child) throws Exception {
    File f = (File) child;
    BitSet bs = new BitSet(3);
    if (f.isDirectory()) bs.set(0);
    if (f.canRead()) bs.set(1);
    if (f.canWrite()) bs.set(2);
    return bs;
  }

  public String getName() {
    return name;
  }
}
