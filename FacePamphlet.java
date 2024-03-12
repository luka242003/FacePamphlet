/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;


import java.awt.event.*;
import java.util.Iterator;

import javax.swing.*;

public class FacePamphlet extends Program 
					implements FacePamphletConstants {

	/**
	 * This method has the responsibility for initializing the 
	 * interactors in the application, and taking care of any other 
	 * initialization that needs to be performed.
	 */
	public void init() {
		// You fill this in
		canvas = new FacePamphletCanvas();
		add(canvas);
		database = new FacePamphletDatabase();
		initStatus();
		initPicture();
		initFriend();
		//initSex();
		initName();
		initNorth();
		addActionListeners();
		status.addActionListener(this); 
        picture.addActionListener(this); 
        friend.addActionListener(this);
    }
    
  
    /**
     * This class is responsible for detecting when the buttons are
     * clicked or interactors are used, so you will have to add code
     * to respond to these actions.
     */
    public void actionPerformed(ActionEvent e) {
		// You fill this in as well as add any additional methods
    	if(e.getActionCommand().equals("Change Status") || e.getSource() == status) {
    		if(curProfile != null) {
    			canvas.removeAll();
    			curProfile.setStatus(status.getText());  
    			String message = "Status updated to " + status.getText();
    			canvas.displayProfile(curProfile);
    			canvas.showMessage(message);
    		}
    		else {
    			canvas.removeAll();
    			String message = "Please select a profile to change status";
    			canvas.showMessage(message);
    			curProfile = null;
    		}
    	}
    	
    	else if(e.getActionCommand().equals("Change Picture") || e.getSource() == picture) {
    		if(curProfile != null) {
    			GImage image = null; 
    			try { 
    				image = new GImage(picture.getText()); 
    				curProfile.setImage(image);
        			canvas.displayProfile(curProfile);
        			String message = "Picture updated";
        			canvas.showMessage(message);
    			} catch (ErrorException ex) { 
    				image = null;
    				if(canvas.display != null)
    					canvas.remove(canvas.display);
    				String message = "Unable to open image file " + picture.getText();
        			canvas.showMessage(message);
    				// Code that is executed if the filename cannot be opened. 
    			}
    		}
    		else {
    			canvas.removeAll();
    			String message = "Please select a profile to change status";
    			canvas.showMessage(message);
    		}
    	}
    	
    	else if((e.getActionCommand().equals("Add Friend") || e.getSource() == friend) && !friend.getText().equals(curProfile.getName())) {
    		boolean isFriend = false;
    		if(curProfile != null) {
    			if(database.containsProfile(friend.getText())) {
    				Iterator<String> friends = curProfile.getFriends();
    				while(friends.hasNext()) {
    					if(friends.next().equals(friend.getText())) {
    						isFriend = true;
    						break;
    					}
    				}
    				if(!isFriend) {
    					curProfile.addFriend(friend.getText());
    					FacePamphletProfile friendProfile = database.getProfile(friend.getText());
    					friendProfile.addFriend(curProfile.getName());
    					canvas.removeAll();
    					String message = friend.getText() + " added as a friend";
    					canvas.displayProfile(curProfile);
    					canvas.showMessage(message);
    				}
    				else {
    					canvas.removeAll();
    					String message = curProfile.getName() + " already has " + friend.getText() + " as a friend";
    					canvas.displayProfile(curProfile);
    					canvas.showMessage(message);
    				}
    			}
    			else {
    				canvas.removeAll();
    				String message = friend.getText() + " does not exist";
					canvas.displayProfile(curProfile);
					canvas.showMessage(message);
    			}
    		}
    		else {
    			canvas.removeAll();
    			String message = "Please select a profile to add friend";
    			canvas.showMessage(message);
    		}
    	}
    	
    	else if(e.getActionCommand().equals("Add") && !enterName.getText().equals("")) {
    		if(!database.containsProfile(enterName.getText())){
    			canvas.removeAll();
    			profile = new FacePamphletProfile(enterName.getText());
    			database.addProfile(profile);
    			curProfile = profile;
    			canvas.displayProfile(curProfile);
    			String message = "New profile created";
    			canvas.showMessage(message);
    		}
    		else {
    			canvas.removeAll();
    			canvas.displayProfile(database.getProfile(enterName.getText()));
    			String message = "A profile with the name " + enterName.getText() + " already exists";
    			canvas.showMessage(message);
    			curProfile = new  FacePamphletProfile(enterName.getText());
    		}
    	}
    	
    	else if(e.getActionCommand().equals("Delete") && !enterName.getText().equals("")) {
    		if(database.containsProfile(enterName.getText())){
    			database.deleteProfile(enterName.getText());
    			Iterator<String> remove = curProfile.getFriends();
    			while(remove.hasNext()) {
    				(database.getProfile(remove.next())).removeFriend(curProfile.getName());
    			}
    			canvas.removeAll();
    			String message = "Profile of " + enterName.getText() + " deleted";
    			canvas.showMessage(message);
    			
    		}
    		else {
    			canvas.removeAll();
    			String message = "A profile with the name " + enterName.getText() + " does not exist";
    			canvas.showMessage(message);	
    		}
    		curProfile = null;
    	}
    	
    	else if(e.getActionCommand().equals("Lookup") && !enterName.getText().equals("")) {
    		
    		if(database.containsProfile(enterName.getText())){
    			canvas.removeAll();
    			profile = database.getProfile(enterName.getText());
    			curProfile = profile;
    			String message = "Displaying " + enterName.getText();
    			canvas.showMessage(message);
    			canvas.displayProfile(curProfile);
    		}
    		else {
    			canvas.removeAll();
    			String message = "A profile with the name " + enterName.getText() + " does not exist";
    			canvas.showMessage(message);
    			curProfile = null;
    		}
    	}	
    }
//    
//    private void initSex() {
//    	JRadioButton sex1 = new JRadioButton("Male");
//    	JRadioButton sex2 = new JRadioButton("Female");
//    	ButtonGroup sex = new ButtonGroup();
//    	sex.add(sex1);
//    	sex.add(sex2);
//    	add(sex1, WEST);
//    	add(sex2, WEST);
//    }
    
    private void initStatus() {
    	status = new JTextField(TEXT_FIELD_SIZE);
    	add(status, WEST); 	
    	changeStatus = new JButton("Change Status");
    	add(changeStatus, WEST);
    	add(new JLabel(EMPTY_LABEL_TEXT), WEST); 
    }
    
    private void initPicture() {
    	picture = new JTextField(TEXT_FIELD_SIZE);
    	add(picture, WEST); 	
    	changePicture = new JButton("Change Picture");
    	add(changePicture, WEST);
    	add(new JLabel(EMPTY_LABEL_TEXT), WEST); 
    }
    
    private void initFriend() {
    	friend = new JTextField(TEXT_FIELD_SIZE);
    	add(friend, WEST); 	
    	changeFriend = new JButton("Add Friend");
    	add(changeFriend, WEST);
    }
    
    private void initName() {
    	JLabel name = new JLabel("Name");
    	add(name, NORTH);
    	enterName = new JTextField(TEXT_FIELD_SIZE);
    	add(enterName, NORTH); 
    }
    
    private void initNorth() {
    	add(new JButton("Add"), NORTH);
    	add(new JButton("Delete"), NORTH);
    	add(new JButton("Lookup"), NORTH);
    }
    
    private JTextField enterName;
    private JTextField status;
    private JTextField friend;
    private JTextField picture;
    private FacePamphletDatabase database;
    private FacePamphletProfile profile;
    private FacePamphletProfile curProfile;
    private JButton changeStatus;
    private JButton changeFriend;
    private JButton changePicture;
    private FacePamphletCanvas canvas;
}
