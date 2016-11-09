package ritik.project.dummy;



import java.util.Arrays;
import java.util.HashMap;
/**
 * Created by SuperUser on 09-11-2016.
 */
public class helper {

    static String[] Year={"16","15","14","13","12"};
    static String[] Branch={"IEC","ICS","ICE","IBT","IAR","IME","IEE","IFT"};




    public static Boolean isRoll(String roll){
        if(roll.length()!=10)
            return false;
        HashMap<String,String> roll_no=HashRoll(roll);
        if(Arrays.asList(Year).contains(roll_no.get("year"))&&Arrays.asList(Branch).contains(roll_no.get("branch"))&&(roll_no.get("rno").length()==3))
            return true;
        else
            return false;
    }
    public static HashMap<String,String> HashRoll(String roll){
        HashMap<String,String> roll_no=new HashMap<String,String>();
        roll_no.put("year",roll.substring(0,2) );
        roll_no.put("branch", roll.substring(3,6).toUpperCase());
        roll_no.put("rno", roll.substring(7,10));
        return roll_no;
    }

}
