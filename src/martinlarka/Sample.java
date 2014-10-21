package martinlarka;

import java.io.IOException;
import java.util.ArrayList;

import com.leapmotion.leap.*;

import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

class SampleListener extends Listener {
	
	Hand tempHand;
	
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
        	tempHand = hand;
            String handType = hand.isLeft() ? "Left hand" : "Right hand";
            System.out.println("  " + handType + ", id: " + hand.id()
                             + ", palm position: " + hand.palmPosition());

            // Get the hand's normal vector and direction
            Vector normal = hand.palmNormal();
            Vector direction = hand.direction();

            // Calculate the hand's pitch, roll, and yaw angles
            System.out.println("  pitch: " + Math.toDegrees(direction.pitch()) + " degrees, "
                             + "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
                             + "yaw: " + Math.toDegrees(direction.yaw()) + " degrees");

            // Get arm bone
            Arm arm = hand.arm();
            System.out.println("  Arm direction: " + arm.direction()
                             + ", wrist position: " + toHandCoor(arm.wristPosition())
                             + ", elbow position: " + toHandCoor(arm.elbowPosition()));

            VectorInstance[] instances = new VectorInstance[24];
            int i = 0;
            
            // Get fingers
            for (Finger finger : hand.fingers()) {
                System.out.println("    " + finger.type() + ", id: " + finger.id()
                                 + ", length: " + finger.length()
                                 + "mm, width: " + finger.width() + "mm");

                //Get Bones
                for(Bone.Type boneType : Bone.Type.values()) {
                    Bone bone = finger.bone(boneType);
                    
                    instances[i] = new VectorInstance(bone.direction());
                    i++;
                }
            }

        }

        if (!frame.hands().isEmpty()) {
            System.out.println();
        }
    }
    
    Vector toHandCoor(Vector vec) {
    	return vec.minus(tempHand.palmPosition());
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
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the sample listener when done
        controller.removeListener(listener);
    }
}

