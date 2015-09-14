package org.danh.project.user.usertypes;

import java.util.Set;

public class SafeBasicUser extends TransportSafeUser {
	Set<Long> canvasIds;

	SafeBasicUser(BasicUser user) {
		super(user);
		this.canvasIds = user.getCanvasIds();
	}
}