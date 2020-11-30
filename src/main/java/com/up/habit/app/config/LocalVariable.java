package com.up.habit.app.config;

import com.up.habit.kit.RequestKit;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * TODO:
 *
 * @author 王剑洪 on 2020/3/26 1:02
 */
public enum LocalVariable {
  ME;

  private ThreadLocal<String> HOST_URL = new ThreadLocal();
  private ThreadLocal<Object> ADMIN_INFO = new ThreadLocal<>();
  private ThreadLocal<Object> MEMBER_INFO = new ThreadLocal<>();


  public void setHost(HttpServletRequest request) {
    String host = RequestKit.getHost(request);
    HOST_URL.set(host);
  }

  public String getHost() {
    return HOST_URL.get();
  }

  public void removeHost() {
    HOST_URL.remove();
  }

  public void setAdmin(Object admin) {
    ADMIN_INFO.set(admin);
  }

  public Object getAdmin() {
    return ADMIN_INFO.get();
  }

  public void removeAdmin() {
    ADMIN_INFO.remove();
  }

  public void setMember(Object admin) {
    MEMBER_INFO.set(admin);
  }

  public Object getMember() {
    return MEMBER_INFO.get();
  }

  public void removeMember() {
    MEMBER_INFO.remove();
  }

}
