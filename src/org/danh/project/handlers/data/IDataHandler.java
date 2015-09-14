package org.danh.project.handlers.data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract interface IDataHandler {
	public abstract void receiveData(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse);

	public abstract void getData(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse);
}

