/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade;

import net.ugorji.oxygen.util.Closeable;
import net.ugorji.oxygen.util.OxyLocal;

/*
 * Since we now support and recommend using instances of a Context,
 * We have a way to set/get the default.
 * Other modules/classes can always depend on a default Session
 * being available to work with.
 *
 * Most instances will extend FacadeSession
 */
public class FacadeSession implements Closeable {

  private Class defaultClass;

  public static FacadeSession getDefault(Class clz) {
    return (FacadeSession) OxyLocal.get(clz);
  }

  public void setDefault() {
    setDefault(getClass());
  }

  public void setDefault(Class clz) {
    if (!clz.isInstance(this)) {
      throw new RuntimeException(
          "setDefault failed. Parameter with class: "
              + getClass()
              + " is not an instance of class: "
              + clz);
    }
    clearLocal();
    defaultClass = clz;
    OxyLocal.set(defaultClass, this);
  }

  public void close() {
    clearLocal();
  }

  private void clearLocal() {
    if (defaultClass != null) {
      OxyLocal.set(defaultClass, null);
      defaultClass = null;
    }
  }
}
