package Constants;

import java.util.HashMap;

public class LevelSpeeds {

    private static HashMap<Integer, Integer> LEVELS;

    public static int getLevel(int l){
        if(LEVELS == null){
            initializeLevels();
        }
        return LEVELS.get(l);
    }

    private static void initializeLevels(){
        LEVELS = new HashMap();
        int currSpeed = 1500;
        for(int i = 0; i <= 25; i++){
            LEVELS.put(i, currSpeed);
            currSpeed -= 50;
        }
    }


}
