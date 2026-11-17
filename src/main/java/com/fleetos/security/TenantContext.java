package com.fleetos.security;
public final class TenantContext {
 private static final ThreadLocal<ApiCredential> CURRENT=new ThreadLocal<>();
 private TenantContext(){}
 public static void set(ApiCredential credential){CURRENT.set(credential);}
 public static ApiCredential credential(){ApiCredential value=CURRENT.get();if(value==null)throw new IllegalStateException("No authenticated organization");return value;}
 public static Organization organization(){return credential().getOrganization();}
 public static void clear(){CURRENT.remove();}
}
