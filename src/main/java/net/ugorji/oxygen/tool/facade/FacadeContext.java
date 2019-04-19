/* <<< COPYRIGHT START >>>
 * Copyright 2006-Present OxygenSoftwareLibrary.com
 * Licensed under the GNU Lesser General Public License.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * @author: Ugorji Nwoke
 * <<< COPYRIGHT END >>>
 */

package net.ugorji.oxygen.tool.facade;

import java.util.*;
import net.ugorji.oxygen.util.StringUtils;

public class FacadeContext {
  private int flags = 0;
  private Map attributes = new HashMap();
  private Exception lastThrowable;
  private Set vars = new HashSet();
  private String definitionResource;
  private String helpResource;

  private FacadeHelpHelper facadeHelpHelper;
  private FacadeDefinitionHelper facadeDefinitionHelper;

  public FacadeContext(String _definitionResource, String _helpResource) throws Exception {
    definitionResource = _definitionResource;
    helpResource = _helpResource;

    facadeHelpHelper = new FacadeHelpHelper(helpResource);
    facadeDefinitionHelper = new FacadeDefinitionHelper(definitionResource);

    String s = facadeDefinitionHelper.getParameter("DEFAULT_SET_VARS");
    setVars(StringUtils.tokenize(s, ",", true));
  }

  public FacadeDefinitionHelper getFacadeDefinitionHelper() {
    return facadeDefinitionHelper;
  }

  public FacadeHelpHelper getFacadeHelpHelper() {
    return facadeHelpHelper;
  }

  public Map getAttributes() {
    return Collections.unmodifiableMap(attributes);
  }

  public Object getAttribute(String name) {
    return attributes.get(name);
  }

  public void setAttribute(String name, Object value) {
    attributes.put(name, value);
  }

  public void setAttributes(Map attributes) {
    this.attributes = attributes;
  }

  public int getFlags() {
    return flags;
  }

  public void setFlags(int flags) {
    this.flags = flags;
  }

  public Exception getLastThrowable() {
    return lastThrowable;
  }

  protected void setLastThrowable(Exception lastThrowable) {
    this.lastThrowable = lastThrowable;
  }

  public Set getVars() {
    return Collections.unmodifiableSet(vars);
  }

  public void setVars(Collection c) {
    this.vars.addAll(c);
  }

  public void setVar(String key) {
    vars.add(key.trim());
  }

  public void unsetVar(String key) {
    vars.remove(key.trim());
  }

  public void unsetVars(Collection c) {
    vars.removeAll(c);
  }

  public boolean isVarSet(String var) {
    return vars.contains(var);
  }

  public boolean isFlagSet(int _flag) {
    // System.out.println("isFlagSet() called");
    return ((flags & _flag) == _flag);
  }

  public void setFlag(int _flag) {
    flags = flags | _flag;
  }

  public void clearFlag(int _flag) {
    flags = flags & ~_flag;
  }

  public void handleThrowable(Exception thr) throws Exception {
    setLastThrowable(thr);
    if (isVarSet("showstacktrace")) {
      thr.printStackTrace(System.out);
    }
    if (isVarSet("failonerror")) {
      throw thr;
    } else {
      if (thr instanceof FacadeBenignException) {
        System.out.println("Note: " + thr.getMessage());
      } else {
        System.out.println("Handled Exception: " + thr);
      }
    }
  }

  public void handleReturnValue(Object o) throws Exception {
    if (isVarSet("printreturnvalue")) {
      System.out.println(o);
    }
  }
}
