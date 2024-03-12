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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.*;

public class FacePamphlet_extension extends Program 
					implements FacePamphletConstants {

	/**
	 * This method has the responsibility for initializing the 
	 * interactors in the application, and taking care of any other 
	 * initialization that needs to be performed.
	 */
	public void init() {
		// You fill this in
		canvas = new FacePamphletCanvas_extension();
		add(canvas);
		database = new FacePamphletDatabase_extension();
		map = new  HashMap<FacePamphletProfile_extension, JComboBox<String>>();
	
		initStatus();
		initPicture();
		initFriend();
		initSex();
		initName();
		initNorth();
		initChat();
		addActionListeners();
		status.addActionListener(this); 
        picture.addActionListener(this); 
        friend.addActionListener(this);
        sex1.addActionListener(this);
        sex2.addActionListener(this);
        sex3.addActionListener(this);
        friendsBox.addActionListener(this);
        choose.addActionListener(this);

    }
    
  
    /**
     * This class is responsible for detecting when the buttons are
     * clicked or interactors are used, so you will hve to add code
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
    			friendsBox.removeAllItems();
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
    				canvas.removeAll();
    				canvas.displayProfile(curProfile);
    				String message = "Unable to open image file " + picture.getText();
        			canvas.showMessage(message);
    				// Code that is executed if the filename cannot be opened. 
    			}
    		}
    		else {
    			canvas.removeAll();
    			friendsBox.removeAllItems();
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
    					FacePamphletProfile_extension friendProfile = database.getProfile(friend.getText());
    					friendProfile.addFriend(curProfile.getName());
    					map.get(curProfile).addItem(friend.getText());
    					canvas.removeAll();
    					String message = friend.getText() + " added as a friend";
    					canvas.displayProfile(curProfile);
    					canvas.showMessage(message);
    				}
    				else {
    					canvas.removeAll();
    					friendsBox.removeAllItems();
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
    			friendsBox.removeAllItems();
    			String message = "Please select a profile to add friend";
    			canvas.showMessage(message);
    		}
    	}
    	
    	else if(e.getActionCommand().equals("Add") && !enterName.getText().equals("")) {
    		if(!database.containsProfile(enterName.getText())){
    			canvas.removeAll();
    			profile = new FacePamphletProfile_extension(enterName.getText());
    			database.addProfile(profile);
    			curProfile = profile;
    			map.put(curProfile, friendsBox);
    			canvas.displayProfile(curProfile);
    			String message = "New profile created";
    			canvas.showMessage(message);
    		}
    		else {
    			canvas.removeAll();
    			friendsBox.removeAllItems();
    			canvas.displayProfile(database.getProfile(enterName.getText()));
    			String message = "A profile with the name " + enterName.getText() + " already exists";
    			canvas.showMessage(message);
    			curProfile = new  FacePamphletProfile_extension(enterName.getText());
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
    			friendsBox.removeAllItems();
    			String message = "Profile of " + enterName.getText() + " deleted";
    			canvas.showMessage(message);
    		}
    		else {
    			canvas.removeAll();
    			friendsBox.removeAllItems();
    			String message = "A profile with the name " + enterName.getText() + " does not exist";
    			canvas.showMessage(message);
    		}
    		curProfile = null;
    	}
    	
    	else if(e.getActionCommand().equals("Lookup") && !enterName.getText().equals("")) {
    		
    		if(database.containsProfile(enterName.getText())){
    			canvas.removeAll();
    			friendsBox.removeAllItems();
    			profile = database.getProfile(enterName.getText());
    			curProfile = profile;
    			String message = "Displaying " + enterName.getText();
    			canvas.showMessage(message);
    			canvas.displayProfile(curProfile);
    			addFriendsToBox();
    		}
    		else {
    			canvas.removeAll();
    			friendsBox.removeAllItems();
    			String message = "A profile with the name " + enterName.getText() + " does not exist";
    			canvas.showMessage(message);
    			curProfile = null;
    		}
    	}
    	
    	  
    	else if(sex1.isSelected() || sex2.isSelected() || sex3.isSelected()) {
    		if(curProfile != null) {
    			if(sex1.isSelected()) {
    				canvas.removeAll();
    				curProfile.setSex("Male");
    				canvas.displayProfile(curProfile);
    				String message = curProfile.getName() + " is Male";
    				canvas.showMessage(message);
    				//addFriendsToBox();
    			}
    			else if(sex2.isSelected()) {
    				canvas.removeAll();
    				curProfile.setSex("Female");
    				canvas.displayProfile(curProfile);
    				String message = curProfile.getName() + " is Female";
    				canvas.showMessage(message);
    				//addFriendsToBox();
    			}
    			else if(sex3.isSelected()) {
    				canvas.removeAll();
    				curProfile.setSex("Other");
    				canvas.displayProfile(curProfile);
    				String message = curProfile.getName() + " is other gender";
    				canvas.showMessage(message);
    				//addFriendsToBox();
    			}
    		}
    		else {
    			canvas.removeAll();
    			friendsBox.removeAllItems();
    			String message = "Please select a profile to change status";
    			canvas.showMessage(message);
    			curProfile = null;
    			
    		}
    	}
    	
    	else if(e.getSource() == choose) {
    		if(curProfile != null) {
    			choose.setEnabled(false);
    			String selectedName = (String) friendsBox.getSelectedItem();
    			  if (!chatAdded) {
    		            addChat(selectedName);
    		            chatAdded = true;
    		        }
    		}
    		messages = new ArrayList<GLabel>();
    		current = new ArrayList<Integer>();
    	}
    	
    	else if(e.getSource() == friendText) {
    		if(!friendText.getText().equals("")) {
    			GLabel mes = new GLabel(friendText.getText());
				messages.add(mes);
				current.add(1);
				drawMessages();
    		}
    	}
    	
    	else if(e.getSource() == profileText) {
    		if(!profileText.getText().equals("")) {
    			GLabel mes = new GLabel(friendText.getText());
    			messages.add(mes);
    			current.add(2);
    			drawMessages(); 
    		}
    	}
    }
    
    private void initSex() {
    	sex1 = new JRadioButton("Male");
    	sex2 = new JRadioButton("Female");
    	sex3 = new JRadioButton("Other");
    	ButtonGroup sex = new ButtonGroup();
    	sex.add(sex1);
    	sex.add(sex2);
    	sex.add(sex3);
    	add(sex1, WEST);
    	add(sex2, WEST);
    	add(sex3, WEST);
    }
    
    private void initChat() {
    	choose = new JButton("Choose");
    	add(choose, SOUTH);
    	friendsBox = new JComboBox<String>();
    	//friendsBox.addItem("                      ");
    	add(friendsBox, SOUTH);
    }
    
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
    
    private void addFriendsToBox() {
    	Iterator<String> friends = curProfile.getFriends();
		while(friends.hasNext()) {
			friendsBox.addItem(friends.next());
		}
    }
    
    private void addChat(String friend) {
    	canvas.removeAll();
    	friendLabel = new JLabel(friend);
    	curLabel = new JLabel(curProfile.getName());
    	friendText = new JTextField(TEXT_FIELD_SIZE);
    	profileText = new JTextField(TEXT_FIELD_SIZE);
    	choose.setVisible(false);
    	friendsBox.setVisible(false);
    	add(friendLabel, SOUTH);
    	add(friendText, SOUTH);
       	add(curLabel, SOUTH);
    	add(profileText, SOUTH);
       
    }
    
    private void drawMessages() {
    	canvas.removeAll();
    	y = canvas.getHeight();
    	for(int i = messages.size() - 1; i >= 0; i--) {
    		y -= messages.get(i).getHeight();
    		if(current.get(i) == 1) {
    			canvas.add(messages.get(i), 0, (int)y);
    		}
    		else if(current.get(i) == 2) {
    			canvas.add(messages.get(i), (int)canvas.getWidth() - messages.get(i).getWidth(), y);
    		}
    	}
    }
    
    private int y = 0;
    private ArrayList <Integer> current;
    private int curMessenger = 0;
    private JTextField profileText;
    private JTextField friendText;
    private boolean chatAdded = false;
    private ArrayList<GLabel> messages;
    private HashMap<FacePamphletProfile_extension, JComboBox<String>> map; 
    private JLabel friendLabel;
    private JLabel curLabel;
    private JButton choose;
    private JComboBox<String> friendsBox;
    private JRadioButton sex1;
    private JRadioButton sex2;
    private JRadioButton sex3;
    private JTextField enterName;
    private JTextField status;
    private JTextField friend;
    private JTextField picture;
    private FacePamphletDatabase_extension database;
    private FacePamphletProfile_extension profile;
    private FacePamphletProfile_extension curProfile;
    private JButton changeStatus;
    private JButton changeFriend;
    private JButton changePicture;
    private FacePamphletCanvas_extension canvas;
}
