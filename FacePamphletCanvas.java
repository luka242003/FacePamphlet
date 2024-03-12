/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */


import acm.graphics.*;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas 
					implements FacePamphletConstants {
	
	/** 
	 * Constructor
	 * This method takes care of any initialization needed for 
	 * the display
	 */
	public FacePamphletCanvas() {
		// You fill this in
	}

	
	/** 
	 * This method displays a message string near the bottom of the 
	 * canvas.  Every time this method is called, the previously 
	 * displayed message (if any) is replaced by the new message text 
	 * passed in.
	 */
	public void showMessage(String msg) {
		// You fill this in
		display = new GLabel(msg);
		display.setFont(MESSAGE_FONT);
		if(getElementAt(getWidth() /2 - display.getWidth() / 2, getHeight() - BOTTOM_MESSAGE_MARGIN) != null) {
			remove(getElementAt(getWidth() /2 - display.getWidth() / 2, getHeight() - BOTTOM_MESSAGE_MARGIN));
		}
			add(display, getWidth() /2 - display.getWidth() / 2, getHeight() - BOTTOM_MESSAGE_MARGIN);
	}
	
	
	/** 
	 * This method displays the given profile on the canvas.  The 
	 * canvas is first cleared of all existing items (including 
	 * messages displayed near the bottom of the screen) and then the 
	 * given profile is displayed.  The profile display includes the 
	 * name of the user from the profile, the corresponding image 
	 * (or an indication that an image does not exist), the status of
	 * the user, and a list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile profile) {
		drawName(profile);
		drawImage(profile);
		drawStatus(profile);
		drawFriends(profile);
		// You fill this in
	}
	
	private void drawName(FacePamphletProfile profile) {
		String name = profile.getName();
		nameLabel = new GLabel(name);
		nameLabel.setFont(PROFILE_NAME_FONT);
		if(getElementAt(LEFT_MARGIN, TOP_MARGIN + nameLabel.getHeight()) != null) {
			remove(getElementAt(LEFT_MARGIN, TOP_MARGIN + nameLabel.getHeight()));
		}
			add(nameLabel, LEFT_MARGIN, TOP_MARGIN + nameLabel.getHeight());
	}
	
	private void drawImage(FacePamphletProfile profile) {
		GImage image = profile.getImage();
		GLabel noImage = new GLabel("No Image");
		GRect rect = new GRect(IMAGE_WIDTH, IMAGE_HEIGHT);
		if(image == null) {
			if(getElementAt(LEFT_MARGIN, IMAGE_MARGIN + nameLabel.getHeight() + IMAGE_MARGIN) != null ) {
				remove(getElementAt(LEFT_MARGIN, IMAGE_MARGIN + nameLabel.getHeight() + IMAGE_MARGIN));
			}
			add(rect, LEFT_MARGIN, TOP_MARGIN + nameLabel.getHeight() + IMAGE_MARGIN );
			add(noImage, rect.getX() + IMAGE_WIDTH / 2 - noImage.getWidth() / 2, rect.getY() + IMAGE_HEIGHT /2 + noImage.getHeight() / 2);
		}
		else {
			if(getElementAt( rect.getX() + IMAGE_WIDTH / 2 - noImage.getWidth() / 2, rect.getY() + IMAGE_HEIGHT /2 + noImage.getHeight() / 2) != null) {
				remove(getElementAt( rect.getX() + IMAGE_WIDTH / 2 - noImage.getWidth() / 2, rect.getY() + IMAGE_HEIGHT /2 + noImage.getHeight() / 2));
			}
				image.setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
				add(image, LEFT_MARGIN, IMAGE_MARGIN + nameLabel.getHeight() + IMAGE_MARGIN);
		}
	}
	
	private void drawStatus(FacePamphletProfile profile) {
		String status = profile.getStatus();
		String curStatus = "";
		if(status.equals("")) {
			curStatus = "No current status";
		}
		else {
			curStatus = profile.getName() + " is " + status;
		}
		GLabel statusLabel = new GLabel(curStatus);
		statusLabel.setFont(PROFILE_STATUS_FONT);
		if(getElementAt(LEFT_MARGIN, IMAGE_MARGIN + nameLabel.getHeight() + IMAGE_MARGIN + IMAGE_HEIGHT + statusLabel.getHeight()) != null) {
			remove(getElementAt(LEFT_MARGIN, IMAGE_MARGIN + nameLabel.getHeight() + IMAGE_MARGIN + IMAGE_HEIGHT + statusLabel.getHeight()));
		}
		add(statusLabel, LEFT_MARGIN, IMAGE_MARGIN + nameLabel.getHeight() + IMAGE_MARGIN + IMAGE_HEIGHT + statusLabel.getHeight());
	}
	
	private void drawFriends(FacePamphletProfile profile) {
		GLabel friends = new GLabel("Friends");
		friends.setFont(PROFILE_STATUS_FONT);
		add(friends, getWidth() /2, IMAGE_MARGIN + nameLabel.getHeight() + IMAGE_MARGIN);
		double y = friends.getY() + friends.getHeight();
		Iterator<String> friendsList = profile.getFriends();
		while(friendsList.hasNext()) {
			String friend = friendsList.next();
			GLabel friendLabel = new GLabel(friend);
			friendLabel.setFont(PROFILE_FRIEND_FONT);
			if(getElementAt(getWidth() / 2, y) != null) {
				remove(getElementAt(getWidth() / 2, y));
			}
			add(friendLabel, getWidth() /2, y);
			y += friendLabel.getHeight();
		}
		
	}
	private GLabel nameLabel;
	public GLabel display;
}
