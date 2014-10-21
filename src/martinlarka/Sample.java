package martinlarka;

import java.io.IOException;
import java.util.Scanner;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;

import com.leapmotion.leap.*;

class SampleListener extends Listener {
	
	Dataset gestures = new DefaultDataset();
	String value = null;
	
    public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    public void onFrame(Controller controller) {
		
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
        
        //Get hands
        for(Hand hand : frame.hands()) {
            String handType = hand.isLeft() ? "Left hand" : "Right hand";

            // Get the hand's normal vector and direction
            Vector normal = hand.palmNormal();
            Vector direction = hand.direction();

            // Get arm bone
            Arm arm = hand.arm();

            double[] instanceData = new double[20*3];
            int i = 0;
            
            // Get fingers
            for (Finger finger : hand.fingers()) {
                /*System.out.println("    " + finger.type() + ", id: " + finger.id()
                                 + ", length: " + finger.length()
                                 + "mm, width: " + finger.width() + "mm"); */

                //Get Bones
                for(Bone.Type boneType : Bone.Type.values()) {
                    Bone bone = finger.bone(boneType);
                    //System.out.println(finger.type() + " "+ bone.type());
                    Vector temp = bone.direction();
                    instanceData[i] = (double)temp.getX();
                    instanceData[i+1] = (double)temp.getY();
                    instanceData[i+2] = (double)temp.getZ();
                    i = i+3;
                }
            }
            if (i == 60 && !value.equals("")) {
            	System.out.println("Entering "+ (gestures.size() + 1)  +" gesture with value " + value);
            	DenseInstance temp = new DenseInstance(instanceData, value);
            	gestures.add(temp);
            	value = "";
            }

        }

        if (!frame.hands().isEmpty()) {
            //System.out.println();
        }
    }

	public void setValue(String val) {
		this.value = val;
	}
    
}

class Sample {
    public static void main(String[] args) {
    	
    	
        // Create a sample listener and controller
        SampleListener listener = new SampleListener();
        Controller controller = new Controller();

        // Have the sample listener receive events from the controller
        controller.addListener(listener);
        

        // Keep this process running until Enter is pressed
        System.out.println("Enter quit to quit...");

        Scanner keyboard = new Scanner(System.in);
		String value = "";
		
		while (!value.equals("quit")) {
			System.out.println("enter a sign value");
			value = keyboard.nextLine();
			listener.setValue(value);
		}
		keyboard.close();
        // Remove the sample listener when done
        //controller.removeListener(listener);
    }
}

