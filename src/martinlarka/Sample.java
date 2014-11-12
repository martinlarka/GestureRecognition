package martinlarka;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import net.sf.javaml.classification.Classifier;
import net.sf.javaml.classification.KDtreeKNN;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.FileHandler;

import com.leapmotion.leap.*;

class SampleListener extends Listener {

	Dataset gestures = new DefaultDataset();
	String value = "";
	String realValue = "";
	boolean classify = false;

	double[] prevData = new double[19*3];
	double[] instanceData = new double[19*3];
	
	ArrayList<String> realValues = new ArrayList<String>();
	ArrayList<String> predictedValues = new ArrayList<String>();
	
	Classifier knn;

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

			prevData = instanceData;
			instanceData = new double[19*3];
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
					if ((bone.type().compareTo(Bone.Type.TYPE_METACARPAL) != 0) || (finger.type().compareTo(Finger.Type.TYPE_THUMB) != 0)) {
						Vector temp = bone.direction();
						instanceData[i] = (double)temp.getX();
						instanceData[i+1] = (double)temp.getY();
						instanceData[i+2] = (double)temp.getZ();
						i = i+3;
					}
				}
			}
			if (i == instanceData.length && !value.equals("") && !classify) {
				System.out.println("Entering "+ (gestures.size() + 1)  +" gesture with value " + value);
				DenseInstance temp = new DenseInstance(instanceData, value);
				gestures.add(temp);
				value = "";
			}
			if (classify && i == instanceData.length) {
				DenseInstance temp = new DenseInstance(instanceData);
				Object predictedClassValue = knn.classify(temp);
				System.out.println("Predicted value: " + (String)predictedClassValue);
				if (!value.equals("")) {
					System.out.println("ADDED TEST SCORE");
					realValues.add(value);
					predictedValues.add((String)predictedClassValue);
					value = "";
				}
			}
		}
	}

	public void setValue(String val) {
		this.value = val;
	}

	public void saveData(String fileName) {
		System.out.println("Saving " + gestures.size() + " gestures to: " + fileName);
		try {
			FileHandler.exportDataset(gestures,new File(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadData(String fileName) {
		System.out.println("Loading gestures: " + fileName);
		try {
			gestures = FileHandler.loadDataset(new File(fileName), 0,"\t");
			System.out.println(gestures.size() + " gestures loaded");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void classify() {
		if (!classify) {
			knn = new KDtreeKNN(5);
        	knn.buildClassifier(gestures);
        	System.out.println("Classifying with traingingset of size " + gestures.size());
        	classify = true;
		} else {
			classify = false;
		}
	}
	
	public void printResult() {
		System.out.println("Predicted value \t Real value");
		for (int i=0; i<predictedValues.size(); i++) {
			System.out.print(predictedValues.get(i) + "\t");
			System.out.println(realValues.get(i));
		}
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
		String lastValue = "";
		
		boolean quitBool = false;

		while (!quitBool) {
			System.out.println("enter a sign value");
			value = keyboard.nextLine();
			if (value.equals("quit")) {
				quitBool = true;
			} else if (value.startsWith("load")) {
				listener.loadData(value.substring(4));
			} else if (value.startsWith("save")) {
				listener.saveData(value.substring(4));				
			} else if (value.equals("classify")) {
				listener.classify();				
			} else if (value.equals("print")) {
				 listener.printResult();
			} else if (value.equals("")) {
				listener.setValue(lastValue);
			} else {
				listener.setValue(value);
				lastValue = value;
			}
		}
		keyboard.close();

		// Remove the sample listener when done
		//controller.removeListener(listener);
	}
}

