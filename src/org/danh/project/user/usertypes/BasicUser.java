package org.danh.project.user.usertypes;

import java.util.HashSet;
import java.util.Set;

import org.danh.project.canvas.CanvasManager;
import org.danh.project.canvas.CanvasNotFoundException;

public class BasicUser implements IUser {
	private static final long serialVersionUID = -319890831241197217L;
	String username;
	String password;
	Set<Long> canvasIds = new HashSet<Long>();

	public BasicUser(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Long> getCanvasIds() {
		removeInvalidIds();
		return this.canvasIds;
	}

	public TransportSafeUser getSafeUser() {
		return new SafeBasicUser(this);
	}

	public void addCanvasId(long canvasId) {
		this.canvasIds.add(canvasId);
	}

	void removeInvalidIds() {
		HashSet<Long> toRemove = new HashSet<Long>();
		CanvasManager manager = CanvasManager.getInstance();
		for(Long canvasId : this.canvasIds){
			try {
				manager.getCanvas(canvasId);
			} catch (CanvasNotFoundException e) {
				toRemove.add(canvasId);
			}
		}
		this.canvasIds.removeAll(toRemove);
	}
}