package org.danh.project.user;

import java.util.Map;

import org.danh.project.file.FileManager;
import org.danh.project.file.NotInitialisedException;
import org.danh.project.user.exceptions.InvalidUsernameException;
import org.danh.project.user.exceptions.UsernameAlreadyInUseException;
import org.danh.project.user.exceptions.UsernameNotRegisteredException;
import org.danh.project.user.usertypes.IUser;

public class UserDataManager {
	private static UserDataManager instance = null;
	Map<String, IUser> users;

	public static UserDataManager getInstance() {
		if (instance == null) {
			instance = new UserDataManager();
		}
		return instance;
	}

	UserDataManager() {
		try {
			this.users = FileManager.getInstance().deserialiseUsers();
		} catch (NotInitialisedException e) {
			e.printStackTrace();
		}
	}

	public void addUser(IUser user) throws InvalidUsernameException, UsernameAlreadyInUseException {
		String username = user.getUsername();
		if ((username == null) || (username.isEmpty())) {
			throw new InvalidUsernameException();
		}
		if (isRegistered(username)) {
			throw new UsernameAlreadyInUseException();
		}
		this.users.put(user.getUsername(), user);
	}

	public boolean isRegistered(String username) {
		return this.users.keySet().contains(username);
	}

	public boolean areValid(String username, String password) throws UsernameNotRegisteredException {
		if (!isRegistered(username)) {
			throw new UsernameNotRegisteredException();
		}
		return password.equals(this.users.get(username).getPassword());
	}

	public void saveToDisk() {
		try {
			FileManager.getInstance().serialiseUsers(this.users);
		} catch (NotInitialisedException e) {
			e.printStackTrace();
		}
	}

	public Map<String, IUser> getUsers() {
		return this.users;
	}
}