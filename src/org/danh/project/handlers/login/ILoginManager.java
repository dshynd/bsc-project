package org.danh.project.handlers.login;

import javax.servlet.http.HttpSession;
import org.danh.project.user.exceptions.UsernameNotRegisteredException;
import org.danh.project.user.results.Result;
import org.danh.project.user.usertypes.IUser;

public abstract interface ILoginManager {
	public abstract Result doLogin(String username, String password, HttpSession httpSession);

	public abstract void doLogout(HttpSession httpSession);

	public abstract IUser getCurrentUser(HttpSession httpSession);

	public abstract Result registerUser(IUser user);

	public abstract IUser getUserData(String name) throws UsernameNotRegisteredException;

	public abstract Result setUserPassword(String username, String currentPassword, String newPassword);

	public abstract void startUp();

	public abstract void shutDown();
}
