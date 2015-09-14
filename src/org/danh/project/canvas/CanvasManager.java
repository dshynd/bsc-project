package org.danh.project.canvas;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.danh.project.file.FileManager;
import org.danh.project.file.NotInitialisedException;
import org.danh.project.image.producers.IImageProducer;

public class CanvasManager {
	private static final CanvasManager instance = new CanvasManager();
	Map<Long, Canvas> canvases = Collections.synchronizedMap(new HashMap<Long, Canvas>());

	public static CanvasManager getInstance() {
		return instance;
	}

	public Canvas getCanvas(long canvasId) throws CanvasNotFoundException {
		if (!exists(canvasId)) {
			throw new CanvasNotFoundException();
		}
		if (!this.canvases.containsKey(Long.valueOf(canvasId))) {
			Canvas canvas = new Canvas(canvasId);
			this.canvases.put(Long.valueOf(canvasId), canvas);
		}
		return (Canvas) this.canvases.get(Long.valueOf(canvasId));
	}

	public Canvas createCanvas(long canvasId) throws CanvasAlreadyExistsException {
		if (!exists(canvasId)) {
			Canvas canvas = new Canvas(canvasId);
			this.canvases.put(Long.valueOf(canvasId), canvas);
			return canvas;
		}
		throw new CanvasAlreadyExistsException();
	}

	boolean exists(long canvasId) {
		boolean bool = false;
		try {
			bool = FileManager.getInstance().fileExists(canvasId);
		} catch (NotInitialisedException localNotInitialisedException) {
		}
		return (this.canvases.containsKey(Long.valueOf(canvasId))) || (bool);
	}

	public IImageProducer getImageProducer(long canvasId) throws CanvasNotFoundException {
		Canvas canvas = (Canvas) this.canvases.get(Long.valueOf(canvasId));
		if (canvas != null) {
			return canvas.getImageProducer();
		}
		throw new CanvasNotFoundException();
	}
}
