package org.aio.activities.quests;

import org.aio.activities.activity.Activity;
import org.aio.activities.banking.Bank;
import org.aio.util.Sleep;
import org.osbot.rs07.api.Chatbox.MessageType;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Tab;

public class CooksAssistant extends QuestActivity {

	private final Area cookRoom = new Area(3205, 3215, 3212, 3212);
	private final Area basement = new Area(3214, 9625, 3216, 9623);
	private final Area cow = new Area(3253, 3270, 3255, 3275);
	private final Area chicken = new Area(3235, 3295, 3226, 3300);
	private final Area wheat = new Area(3162, 3295, 3157, 3298);
	private final Area upper = new Area(new Position(3168, 3305, 2), new Position(3165, 3308, 2));
	private final Area bin = new Area(3165, 3305, 3168, 3308);
	
	private boolean operated = false;
	private boolean put = false;
	
	private final String[] cookOptions = {"What's wrong?", "I'm always happy to help a cook in distress.",
			"Actually, I know where to find this stuff."};
	
	private final String[] itemsNeeded = {"Pot of flour", "Bucket of milk", "Egg"};
	
    public CooksAssistant() {
        super(Quest.COOKS_ASSISTANT);
    }

    @Override
    public void runActivity() throws InterruptedException {

        if (getTabs().getOpen() != Tab.INVENTORY) {
            getTabs().open(Tab.INVENTORY);
        }
        
        switch (getProgress()) {
            case 0:
                talkToCook();
                break;
            case 1:
            	if(hasRequiredItems()) {
            		
            		while(getInventory().contains(itemsNeeded) | getProgress() != 1) {
            			
                		talkToCook();
            			
            		}
            		
            	} else  {
            		getItemsNeeded();
            	}
                break;
        }
        
    }
    
    private boolean hasRequiredItems() {
    	
    	for (String itemName : itemsNeeded) {
    		
    	    if(!getInventory().contains(itemName))
    	    	return false;
    	    
    	}
    	
    	return true;
    	
    }

    private void getItemsNeeded() throws InterruptedException {

    	// Get pot
		if(!getInventory().contains("Pot") && !getInventory().contains("Pot of flour") && !getInventory().contains("Bucket of milk")) {
			
			getGroundItem(cookRoom, "Pot");

		}
		
		// Get bucket
		else if(!getInventory().contains("Bucket") && !getInventory().contains("Bucket of milk")) {
			
			getGroundItem(basement, "Bucket");

		}
    	
		// Milk cow
		else if(getInventory().contains("Bucket") && !getInventory().contains("Bucket of milk")) {
			
			getItemFromObject(cow, "Bucket of milk", "Dairy cow", "Milk");

		}
		
		// Get egg
		else if(!getInventory().contains("Egg") && getInventory().contains("Bucket of milk")) {
			
			getGroundItem(chicken, "Egg");
			
    	}
    	
    	// Get flour
		else if(!getInventory().contains("Pot of flour") && getInventory().contains("Egg") && getInventory().contains("Bucket of milk")) {

			// Get grain
			if(!put && !getInventory().contains("Grain"))
				getItemFromObject(wheat, "Grain", "Wheat", "Pick");
			
			// Put grain
			if(!put && !operated && getInventory().contains("Grain")) {
				
				if(upper.contains(myPlayer())){
					
					if(getInventory().getSelectedItemName() != null && getInventory().getSelectedItemName().equals("Grain")) {
						
						RS2Object hopper = getObjects().closest(n -> n.getName().equals("Hopper"));
							
						if(hopper != null && hopper.interact("Use"))
							Sleep.sleepUntil(() -> getChatbox().contains(MessageType.GAME, "There is already grain in the hopper.") | (!myPlayer().isAnimating() && !getInventory().contains("Grain")), 15000);
						if(getChatbox().contains(MessageType.GAME, "There is already grain in the hopper.") | !getInventory().contains("Grain"))
							put = true;
						
					} else {
						
						if(getInventory().interact("Use", "Grain"))
							Sleep.sleepUntil(() -> getInventory().getSelectedItemName() != null && getInventory().getSelectedItemName().equals("Grain"), 5000);
						
					}
					
				} else {
					getWalking().webWalk(upper);
				}
				
			}
			
			// Operate machine
			if(!operated && put) {
				
				if(upper.contains(myPlayer())){
					
					RS2Object controls = objects.closest(n -> n.getName().equals("Hopper controls"));

					if(controls != null && controls.interact("Operate"))
						Sleep.sleepUntil(() -> !myPlayer().isMoving() && myPlayer().isAnimating(), 10000);
					if(!myPlayer().isMoving() && myPlayer().isAnimating())
						operated = true;
					
				} else {
					getWalking().webWalk(upper);
				}
				
			}
			
			// Get flour
			if(operated && put) {

				getItemFromObject(bin, "Pot of flour", "Flour bin", "Empty");
				
			}
			
		}
    	
    }
    
    private void getItemFromObject(Area place, String itemName, String objectName, String interaction) throws InterruptedException {
    	
		if(place.contains(myPlayer())){
			
			RS2Object object = objects.closest(n -> n.getName().equals(objectName));

			if(object != null && object.interact(interaction))
				Sleep.sleepUntil(() -> getInventory().contains(itemName) && !myPlayer().isAnimating(), 15000);
			
		} else {
			getWalking().webWalk(place);
		}
    	
    }
    
    private void getGroundItem(Area place, String itemName) throws InterruptedException {
    	
		if(place.contains(myPosition())) {
			
			GroundItem itemToGet = groundItems.closest(itemName);
			
			if(itemToGet != null && itemToGet.interact("Take"))
				
				Sleep.sleepUntil(() -> getInventory().contains(itemName), 8000);
					
		} else {
			
			walking.webWalk(place);
			
		}
    	
    }

    private void talkToCook() throws InterruptedException {

        NPC cook = getNpcs().closest("Cook");

        if(cook == null){
        	getWalking().webWalk(cookRoom);
        } else {
        	completeDialog("Cook", cookOptions);
        }
    }

    private void completeDialog(String npcName, String... options) throws InterruptedException {
        if(!getDialogues().inDialogue()){
            talkTo(npcName);
        } else if(getDialogues().isPendingContinuation()){
            getDialogues().clickContinue();
        } else if(options.length > 0 && getDialogues().isPendingOption()){
            getDialogues().completeDialogue(options);
        }
    }

    private void talkTo(String npcName){
        NPC npc = getNpcs().closest(npcName);
        if(npc != null) {
            npc.interact("Talk-to");
            Sleep.sleepUntil(() -> getDialogues().inDialogue(), 5000);
        }
    }

    @Override
    public Activity copy() {
        return new CooksAssistant();
    }
}