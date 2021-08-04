package logic;

public abstract class LogicFactory {

    private static final String PACKAGE = "logic.";
    private static final String SUFFIX = "Logic";

    private LogicFactory() {
    }

    //TODO this code is not complete, it is just here for sake of programe working. need to be changed ocmpletely
    public static < T> T getFor( String entityName ) {
        //this casting wont be needed.
        
        // THIS IS TEMPORARLY 
        // JUST FOR THE SAKE OF TESTING
        switch (entityName){// THIS IS TEMPORARLY 
            case "Account":// THIS IS TEMPORARLY 
                return (T)new AccountLogic();// THIS IS TEMPORARLY 
            case "DonationRecord":// THIS IS TEMPORARLY 
                return (T)new DonationRecordLogic();// THIS IS TEMPORARLY
            default:// THIS IS TEMPORARLY 
                return (T)new AccountLogic();// THIS IS TEMPORARLY 
        }// THIS IS TEMPORARLY 

    }
}
