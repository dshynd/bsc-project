package org.danh.project.user.usertypes;

import java.io.Serializable;
import java.util.Set;

public abstract interface IUser extends Serializable {
	public abstract String getUsername();

	public abstract String getPassword();

	public abstract void setPassword(String password);

	public abstract TransportSafeUser getSafeUser();

	public abstract Set<Long> getCanvasIds();

	public abstract void addCanvasId(long canvasId);
}