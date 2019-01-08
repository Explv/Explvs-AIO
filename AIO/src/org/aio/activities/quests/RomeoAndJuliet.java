package org.aio.activities.quests;

import org.aio.activities.activity.Activity;
import org.aio.activities.banking.Bank;
import org.aio.util.Sleep;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Tab;

public class RomeoAndJuliet extends QuestActivity {

	private final Area Romeo = new Area(3205, 3431, 3220, 3415);
	private final Area Juliet = new Area(new Position(3155, 3425, 1), new Position(3161, 3426, 1));
	private final Area Lawrence = new Area(3252, 3486, 3259, 3472);
	private final Area Apothecary = new Area(3197, 3406, 3192, 3402);
	private final Area berries = new Area(3263, 3365, 3278, 3375);
	
	private final String[] RomeoOptions = {"Perhaps I could help to find her for you?", "Yes, ok, I'll let her know.", "Ok, thanks."};
	private final String[] ApothecaryOptions = {"Talk about something else.", "Talk about Romeo & Juliet.", "Ok, thanks.﻿﻿﻿"};
	
	private final String[] questItems = {"Cadava potion", "Cadava berries", "Message"};

    public RomeoAndJuliet() {
        super(Quest.ROMEO_AND_JULIET);
    }

    @Override
    public void runActivity() throws InterruptedException {
    	
        if(getInventory().getEmptySlotCount() < 5){
        	
            if(Bank.inAnyBank(myPosition())){
                if(!getBank().isOpen()) {
                    if (getBank().open()) {
                        Sleep.sleepUntil(() -> getBank().isOpen(), 5000);
                    }
                }
                else getBank().depositAllExcept(questItems);
            } else{
                getWalking().webWalk(Bank.getAreas());
            }
            
        } else {
        	
            if (getTabs().getOpen() != Tab.INVENTORY) {
                getTabs().open(Tab.INVENTORY);
            }

            switch (getProgress()) {
                case 0:
                	talkToRomeo();
                    break;
                case 10:
                    talkToJuliet();
                    break;
                case 20:
                	talkToRomeo();
                    break;
                case 30:
                	talkToLawrence();
                    break;
                case 40:
                	talkToApothecary();
                    break;
                case 50:
                	// Make sure we are not in the cut scene
            		getTabs().open(Tab.QUEST);
                	if(getDialogues().isPendingContinuation()) {
                		
                		getDialogues().clickContinue();
                		
                	} else if(getTabs().getOpen() == Tab.QUEST){ 
                		
                		deliverCadavaPotion();
                		
                	}
                    break;
                case 60:
                	// Make sure we are not in the cut scene
                	if(Tab.INVENTORY.isDisabled(bot)) {
                		
                		getDialogues().clickContinue();
                		
                	} else { 
                		
                		talkToRomeo();
                		
                	}
                    break;
                case 100:
                	if(getDialogues().inDialogue())
        				getDialogues().clickContinue();
                	break;

            }
        	
        }
            
    }

    private void deliverCadavaPotion() throws InterruptedException {

    	if(getInventory().contains("Cadava potion")) {
    	
    		talkToJuliet();
    	
    	} else if(getInventory().contains("Cadava berries")) {
    		
    		talkToApothecary();
    	
    	} else {

        	getItemFromObject(berries, "Cadava berries", "Cadava bush", "Pick-from");
    		
    	}
    	
        
    }

    private void talkToApothecary() throws InterruptedException {

        NPC ApothecaryNPC = getNpcs().closest("Apothecary");

        if(ApothecaryNPC == null){
            getWalking().webWalk(Apothecary);
        } else {
            completeDialog("Apothecary", ApothecaryOptions);
        }
        
    }

    private void talkToLawrence() throws InterruptedException {

        NPC LawrenceNPC = getNpcs().closest("Father Lawrence");

        if(LawrenceNPC == null){
            getWalking().webWalk(Lawrence);
        } else {
            completeDialog("Father Lawrence");
        }
        
    }

    private void talkToRomeo() throws InterruptedException {

        NPC RomeoNPC = getNpcs().closest("Romeo");

        if(RomeoNPC == null){
            getWalking().webWalk(Romeo);
        } else {
            completeDialog("Romeo", RomeoOptions);
        }
        
    }

    private void talkToJuliet() throws InterruptedException {

        NPC JulietNPC = getNpcs().closest("Juliet");

        if(JulietNPC == null){
            getWalking().webWalk(Juliet);
        } else {
            completeDialog("Juliet");
        }
        
    }
    
    private void getItemFromObject(Area place, String itemName, String objectName, String interaction) throws InterruptedException {

		if(place.contains(myPlayer())){

			RS2Object object = objects.closest(n -> n.getName().equals(objectName) && n.hasAction(interaction));

			if(object != null && object.interact(interaction))
				Sleep.sleepUntil(() -> getInventory().contains(itemName), 15000);

		} else {
			getWalking().webWalk(place);
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
        return new RomeoAndJuliet();
    }
}