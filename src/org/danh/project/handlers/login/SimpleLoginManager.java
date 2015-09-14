package org.danh.project.handlers.login;

import javax.servlet.http.HttpSession;

import org.danh.project.user.UserDataManager;
import org.danh.project.user.exceptions.InvalidUsernameException;
import org.danh.project.user.exceptions.UsernameAlreadyInUseException;
import org.danh.project.user.exceptions.UsernameNotRegisteredException;
import org.danh.project.user.results.Result;
import org.danh.project.user.results.ResultDetailsInvalid;
import org.danh.project.user.results.ResultIncorrectPassword;
import org.danh.project.user.results.ResultInvalidUsername;
import org.danh.project.user.results.ResultMiscError;
import org.danh.project.user.results.ResultOK;
import org.danh.project.user.results.ResultUsernameInUse;
import org.danh.project.user.usertypes.IUser;

public class SimpleLoginManager implements ILoginManager {
	UserDataManager manager = UserDataManager.getInstance();
	private static final String CURRENT_USER_KEY = "currentUser";

	public Result doLogin(String username, String password, HttpSession httpSession) {
		try {
			if (this.manager.areValid(username, password)) {
				IUser user = this.manager.getUsers().get(username);
				if (user != null) {
					doSessionLogin(httpSession, user);
					return new ResultOK();
				}
				return new ResultMiscError("Details are valid but no associated user object found, report to admin");
			}
			return new ResultDetailsInvalid();
		} catch (UsernameNotRegisteredException e) {
		}
		return new ResultDetailsInvalid();
	}

	public void doLogout(HttpSession httpSession) {
		doSessionLogout(httpSession);
	}

	public IUser getUserData(String username) throws UsernameNotRegisteredException {
		IUser user = (IUser) this.manager.getUsers().get(username);
		if (user == null) {
			throw new UsernameNotRegisteredException();
		}
		return user;
	}

	public void startUp() {
	}

	public void shutDown() {
		this.manager.saveToDisk();
	}

	public Result registerUser(IUser user) {
		try {
			this.manager.addUser(user);
			return new ResultOK();
		} catch (InvalidUsernameException e) {
			return new ResultInvalidUsername();
		} catch (UsernameAlreadyInUseException e) {
			return new ResultUsernameInUse();
		}
	}

	public Result setUserPassword(String username, String currentPassword, String newPassword) {
		IUser user = (IUser) this.manager.getUsers().get(username);
		if (user != null) {
			if (user.getPassword().equals(currentPassword)) {
				user.setPassword(newPassword);
				return new ResultOK();
			}
			return new ResultIncorrectPassword();
		}
		return new ResultInvalidUsername();
	}

	public IUser getCurrentUser(HttpSession httpSession) {
		return (IUser) httpSession.getAttribute(CURRENT_USER_KEY);
	}

	void doSessionLogin(HttpSession httpSession, IUser user) {
		httpSession.setAttribute(CURRENT_USER_KEY, user);
	}

	void doSessionLogout(HttpSession httpSession) {
		httpSession.removeAttribute(CURRENT_USER_KEY);
	}
}
