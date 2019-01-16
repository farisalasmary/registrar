
/*
    @author
          ______         _                  _
         |  ____|       (_)           /\   | |
         | |__ __ _ _ __ _ ___       /  \  | | __ _ ___ _ __ ___   __ _ _ __ _   _
         |  __/ _` | '__| / __|     / /\ \ | |/ _` / __| '_ ` _ \ / _` | '__| | | |
         | | | (_| | |  | \__ \    / ____ \| | (_| \__ \ | | | | | (_| | |  | |_| |
         |_|  \__,_|_|  |_|___/   /_/    \_\_|\__,_|___/_| |_| |_|\__,_|_|   \__, |
                                                                              __/ |
                                                                             |___/
            Email: farisalasmary@gmail.com
            Date:  Apr 7, 2016
*/


import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.logging.*;

import org.jnativehook.*;
import org.jnativehook.keyboard.*;

public class Registrar implements NativeKeyListener{
	
	private Robot robot;
	private String CRNs[]; 
	private boolean isAutoEnter = true;

	public Registrar(String CRNs[]){
		this.setCRNs(CRNs);
		this.initializeRobotInstance();
	}	
		
	public boolean startMonitoring(){
		// Clear previous logging configurations.
		LogManager.getLogManager().reset();

		// Get the logger for "org.jnativehook" and set the level to off.
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
		
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		GlobalScreen.addNativeKeyListener(this);
		return true;
	}
	
	public boolean stopMonitoring(){
		try {
			GlobalScreen.unregisterNativeHook();
		} catch (NativeHookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		GlobalScreen.removeNativeKeyListener(this);
		return true;
	}
	
	private void initializeRobotInstance(){
		 try {
			this.robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sendKeys(String str) {

		for(int i = 0; i < str.length(); i++){
			int c = getKey(str.charAt(i));
			if(c != -1){
				robot.keyPress(c);
				robot.keyRelease(c);
			}
		}

		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_TAB);
	}
						
	private int getKey(char c){
		switch(c){
			case '0':
				return KeyEvent.VK_0;
			case '1':
				return KeyEvent.VK_1;
			case '2':			
				return KeyEvent.VK_2;
			case '3':
				return KeyEvent.VK_3;
			case '4':
				return KeyEvent.VK_4;
			case '5':
				return KeyEvent.VK_5;
			case '6':
				return KeyEvent.VK_6;
			case '7':
				return KeyEvent.VK_7;
			case '8':
				return KeyEvent.VK_8;
			case '9':
				return KeyEvent.VK_9;
		}
		return -1;
	}
	   
	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
	
		if(e.getKeyCode() == 57){ // Tab = 15, space = 57
			robot.keyPress(KeyEvent.VK_TAB); // to send Tab to move to the second cell because if we keep it in   
			robot.keyRelease(KeyEvent.VK_TAB); // the first cell then the first CRN will not be printed completely
			
			for(String crn: this.CRNs)
				sendKeys(crn);
			if(this.isAutoEnter()){
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
			}
		}
//		}else if(e.getKeyCode() == 1){ //ESCAPE
//			System.out.println("Goodbye!");
//			this.stopMonitoring();
//		}				
	}
 
	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {}
	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {}

	public String[] getCRNs() {
		return deepCopy(this.CRNs);
	}

	public void setCRNs(String CRNs[]) {
		if(CRNs == null)
			throw new NullPointerException();
		this.CRNs = deepCopy(CRNs);
	}
	
	private String[] deepCopy(String CRNs[]){
		String newCRNs[] = new String[CRNs.length];
		for(int i = 0; i < CRNs.length; i++)
			newCRNs[i] = new String(CRNs[i]);
		return newCRNs;
	}

	public boolean isAutoEnter() {
		return isAutoEnter;
	}

	public void setAutoEnter(boolean isAutoEnter) {
		this.isAutoEnter = isAutoEnter;
	}
	
//	public static void main(String[] args) {
//		
//		String CRNs[] = {"10119", "10122", "10124", "10509"};
//
//		Registrar reg = new Registrar(CRNs);
//
//		if(reg.startMonitoring())
//			System.out.println("Monitoring has been started!!");
//												
//	}
}

