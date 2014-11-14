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

	Dataset gesturesDir = new DefaultDataset();
	Dataset gesturesPos = new DefaultDataset();
	String value = "";
	String realValue = "";
	boolean classify = false;

	double[] prevDirectionData = new double[19*3];
	double[] instanceDirectionData = new double[19*3];
	
	double[] prevPositionData = new double[19*3];
	double[] instancePositionData = new double[19*3];

	ArrayList<String> realValues = new ArrayList<String>();
	ArrayList<String> predictedDirValues = new ArrayList<String>();
	ArrayList<String> predictedPosValues = new ArrayList<String>();

	Classifier knnDir;
	Classifier knnPos;

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

			prevDirectionData = instanceDirectionData;
			instanceDirectionData = new double[19*3];
			
			prevPositionData = instancePositionData;
			instancePositionData = new double[19*3];
			
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
						Vector tempDir = bone.direction();
						instanceDirectionData[i] = (double)tempDir.getX();
						instanceDirectionData[i+1] = (double)tempDir.getY();
						instanceDirectionData[i+2] = (double)tempDir.getZ();
						
						Vector tempPos = bone.prevJoint();
						tempPos = tempPos.minus(hand.wristPosition());
						instancePositionData[i] = (double)tempPos.getX();
						instancePositionData[i+1] = (double)tempPos.getY();
						instancePositionData[i+2] = (double)tempPos.getZ();
						
						i = i+3;
					}
				}
			}
			if (i == instanceDirectionData.length && !value.equals("") && !classify) {
				System.out.println("Entering "+ (gesturesDir.size() + 1)  +" gesture with value " + value);
				
				DenseInstance tempDir = new DenseInstance(instanceDirectionData, value);
				gesturesDir.add(tempDir);
				
				DenseInstance tempPos = new DenseInstance(instancePositionData, value);
				gesturesPos.add(tempPos);
				
				value = "";
			}
			if (classify && i == instanceDirectionData.length) {
				DenseInstance temp = new DenseInstance(instanceDirectionData);
				Object predictedDirClassValue = knnDir.classify(temp);
				
				DenseInstance temp2 = new DenseInstance(instancePositionData);
				Object predictedPosClassValue = knnPos.classify(temp2);
				
				
				System.out.print("Predicted dir value: " + (String)predictedDirClassValue);
				System.out.println(" Predicted pos value: " + (String)predictedPosClassValue);
				if (!value.equals("")) {
					System.out.println("ADDED TEST SCORE");
					realValues.add(value);
					predictedDirValues.add((String)predictedDirClassValue);
					predictedPosValues.add((String)predictedPosClassValue);
					value = "";
				}
			}
		}
	}

	public void setValue(String val) {
		this.value = val;
	}

	public void saveData(String fileName) {
		System.out.println("Saving " + gesturesDir.size() + " gestures to: " + fileName);
		try {
			FileHandler.exportDataset(gesturesDir,new File(fileName+"dir"));
			FileHandler.exportDataset(gesturesPos,new File(fileName+"pos"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadData(String fileName) {
		System.out.println("Loading gestures: " + fileName);
		try {
			Dataset loadedDirSet = FileHandler.loadDataset(new File(fileName+"dir"), 0,"\t");
			Dataset loadedPosSet = FileHandler.loadDataset(new File(fileName+"pos"), 0,"\t");
			System.out.println(loadedDirSet.size() + " gestures loaded");
			gesturesDir.addAll(loadedDirSet);
			gesturesPos.addAll(loadedPosSet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void classify() {
		if (!classify) {
			knnDir = new KDtreeKNN(5);
			knnDir.buildClassifier(gesturesDir);
			
			knnPos = new KDtreeKNN(5); //TODO Variable K
			knnPos.buildClassifier(gesturesPos);
			
			System.out.println("Classifying with traingingset of size " + gesturesDir.size());
			classify = true;
		} else {
			classify = false;
		}
	}

	public void printResult() {
		System.out.println("Predicted direction value \t Predicted position value \t Real value");
		for (int i=0; i<realValues.size(); i++) {
			System.out.print(predictedDirValues.get(i) + "\t");
			System.out.print(predictedPosValues.get(i) + "\t");
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
		controller.removeListener(listener);
	}
}

