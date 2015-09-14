package org.danh.project.user.usertypes;

public abstract class TransportSafeUser
{
  String username;
  
  public TransportSafeUser(IUser user)
  {
    this.username = user.getUsername();
  }
  
  public String getUsername()
  {
    return this.username;
  }
}