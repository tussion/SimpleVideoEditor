/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.shaman.sve.player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;
import org.shaman.sve.FrameTime;
import org.shaman.sve.filters.AbstractImageFilter;
import org.shaman.sve.model.ImageTimelineObject;
import org.shaman.sve.model.Resource;
import org.shaman.sve.model.TimelineObject;

/**
 *
 * @author Sebastian Weiss
 */
public class PlayerImageControl {
	private static final Logger LOG = Logger.getLogger(PlayerImageControl.class.getName());
	
	private final ImageTimelineObject timelineObject;
	private final Player player;

	public PlayerImageControl(ImageTimelineObject timelineObject, Player player) {
		this.timelineObject = timelineObject;
		this.player = player;
	}
	
	public Image computeFrame(FrameTime ft, boolean thumbnail) {
		if (!timelineObject.isEnabled()) return null;
		
		FrameTime start = ft.clone().fromMillis(timelineObject.getStart());
		FrameTime end = ft.clone().fromMillis(timelineObject.getDuration()).addLocal(start);
		if (start.compareTo(ft) > 0) {
			return null; //too early: before the video/image shows up
		} else if (end.compareTo(ft) <= 0) {
			return null; //too late: end of the video/image passed
		}
		int frame = ft.toFrames() - start.toFrames();
		
		BufferedImage img = ((Resource.ImageProvider) timelineObject.getResource()).getFrame(frame, thumbnail);
		float thumbnailScale = thumbnail ? ((Resource.ImageProvider) timelineObject.getResource()).getThumbnailScale() : 1;
		//call filters
		for (TimelineObject child : timelineObject.getChildren()) {
			if (child instanceof AbstractImageFilter) {
				img = ((AbstractImageFilter) child).process(img, ft, thumbnail, thumbnailScale);
			}
		}
		
		return img;
	}
	
	public void drawFrame(Graphics2D g, Image frame, boolean thumbnail, boolean selected) {
		if (!timelineObject.isEnabled()) return;
		Graphics2D g2d = (Graphics2D) g.create();
		
		if (frame != null) {
			int x = timelineObject.getX();
			int y = timelineObject.getY();
			int w = timelineObject.getWidth();
			int h = timelineObject.getHeight();
			if (timelineObject.isKeepAspectRatio()) {
				//w,h defines a rect, place the image centered inside it
				float aspect = w / (float) h;
				if (aspect > timelineObject.getAspect()) {
					// w is greater
					w = (int) (h * timelineObject.getAspect());
					x += (timelineObject.getWidth() - w) / 2;
				} else if (aspect < timelineObject.getAspect()) {
					// h is greater
					h = (int) (w / timelineObject.getAspect());
					y += (timelineObject.getHeight() - h) / 2;
				}
			}
			g2d.setClip(0, 0, player.getProject().getWidth(), player.getProject().getHeight());
			g2d.drawImage(frame, x, y, w, h, null);
			g2d.setClip(null);
		}
		
		if (selected) {
			g2d.setColor(Color.GRAY);
			Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
			g2d.setStroke(dashed);
			g2d.drawRect(timelineObject.getX(), timelineObject.getY(), timelineObject.getWidth(), timelineObject.getHeight());
		}
		
		g2d.dispose();
	}
}
