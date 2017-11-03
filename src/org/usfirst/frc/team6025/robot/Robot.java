package org.usfirst.frc.team6025.robot;


import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;

public class Robot extends IterativeRobot {
	
	private int mode = 1;
	Command autonomousCommand;
	SendableChooser autoChooser;

	private static final double kValueToInches = 0.125;
	private static final int kUltrasonicPort = 0;
	private static final double kHoldDistance = 12.0;
	private static final double kP = 0.05;
	
	private AnalogInput ultrasonic = new AnalogInput(kUltrasonicPort);
	
	Timer timer = new Timer();
	VictorSP teker1 = new VictorSP(0);
	VictorSP teker2 = new VictorSP(1);
	VictorSP teker3 = new VictorSP(2);
	VictorSP teker4 = new VictorSP(3);
	VictorSP intake = new VictorSP(5);
	VictorSP shooter = new VictorSP(8);
	VictorSP tirmanma1 = new VictorSP(6);
	VictorSP tirmanma2 = new VictorSP(7);
	VictorSP icealma = new VictorSP(4);

	Relay yesil = new Relay(0);
	RobotDrive AArobot;   

	Joystick sagkol;
	Joystick solkol;
	
	int otonom = 0;
    	int gear = 0;

	@Override
    	public void robotInit() {	
	
	
		autoChooser = new SendableChooser();
		autoChooser.addDefault("orta kirmizi cark+top+cizgi",1);
		autoChooser.addObject("orta mavi cark+top+cizgi", 2 );
		autoChooser.addObject("cizgigec", 3);
		autoChooser.addObject("cark", 4);
		autoChooser.addObject("yan cark", 5);
		SmartDashboard.putData("Otonom Secimi", autoChooser);
		
    		AArobot = new RobotDrive(teker1,teker2,teker3,teker4);
    		sagkol = new Joystick(0);
    		solkol = new Joystick(1);
    	

    	}
	
    	public void autonomousInit() {
   		timer.start();
		otonom = 0;
    	}
	
    	public void autonomousPeriodic() {
    		mode = (int) autoChooser.getSelected();
		
  	 	switch(mode) {
         		case 1: {
        	 
        			while ( otonom == 0 ) { //gidis
        				if (timer.get() < 2.65) {
            					AArobot.drive(0.3, 0.0); // drive towards heading 0
        				}
    					else{
    			 			AArobot.drive(0.0, 0.0);
    			 			timer.delay(1.5);
    			 			otonom ++;
    	        				timer.reset();

    					}
    				}
				
        			while (isAutonomous() && otonom == 1 ) { //geri gelis
        				if (timer.get() < 0.5) {
            					AArobot.drive(-0.4, 0.0);     
        				}
    					else {
						AArobot.drive(0.0, 0.0); 
    			  			timer.delay(0.5);
    			 			otonom ++;
    			 			timer.reset();
    					}
        			}
        	
   	 			while (isAutonomous() && otonom == 2 ) { // donus
      					if (timer.get() < 0.55) {      
						teker1.set(-0.5);
						teker2.set(-0.5);
						teker3.set(-0.5);
						teker4.set(-0.5);
      					}
  					else {
  			  			AArobot.drive(0.0, 0.0); 
  			  			timer.delay(0.5);
  			 			otonom ++;
  			 			timer.reset();
  					}
				}

	 			while (isAutonomous() && otonom == 3 ) { // gidis
      					if (timer.get() < 1.1) {      
						AArobot.drive(-0.7, 0);
      					}
  					else {
  			  			AArobot.drive(0.0, 0.0); 
  			 			otonom ++;
  			 			timer.delay(0.5);
  			 			timer.reset();
  					}
				}
	 
	 			while (isAutonomous() && otonom == 4 ) { // tekrar donus
	      				if (timer.get() < 0.45) {      
						teker1.set(0.3);
						teker2.set(0.3);
						teker3.set(0.3);
						teker4.set(0.3);	

	      				}
	  				else{
	  			  		AArobot.drive(0.0, 0.0); 
	  			  		timer.delay(0.5);
	  			 		otonom ++;
	  			 		timer.reset();
	  			 		shooter.set(0.74);
	  				}
				}

	 			while (isAutonomous() && otonom == 5 ) { // atis
					double currentDistance = ultrasonic.getValue() * kValueToInches;
					double currentSpeed = (kHoldDistance - currentDistance) * kP;
					SmartDashboard.putNumber("Uzaklik (inc) ",currentDistance);
			
					if (timer.get() < 5) {
				
		
		    				if (currentDistance > 144) {
	    						 AArobot.drive(-0.2, 0.0);
		    				}
						else {
							AArobot.stopMotor();
							timer.delay(0.5);		    	
							yesil.set(Relay.Value.kOn);
		    			    		intake.set(1);
		    		    			icealma.set(1);
					    		shooter.set(-0.745);	
	 					}
					}
					else {
						otonom++;
						intake.set(0);
		    				icealma.set(0);
			    			shooter.set(0);	
			    			timer.reset();
					}
				}
			 	while (isAutonomous() && otonom == 6 ) {
			      		if (timer.get() < 0.45) {      
			      			AArobot.drive(0.5, 0.5);
			      		}
			  		else {
			  		  	AArobot.drive(0.0, 0.0); 
			  		 	otonom ++;
			  		 	timer.reset();
			  		}
				}
				while (isAutonomous() && otonom == 7 ) {
					if (timer.get() < 1.5) {      
						AArobot.drive(0.7, 0);
					}
					else {
					  	AArobot.drive(0.0, 0.0); 
					 	otonom ++;
					 	timer.reset();
						AArobot.stopMotor();
					}
					  		
				}
			 	
				break;
	 		}
			case 2: {
        	 		while (isAutonomous() && otonom == 0 ) {
             				if (timer.get() < 2.65) {
                 				AArobot.drive(0.3, 0.0); // drive towards heading 0
             				}
         				else {
         					AArobot.drive(0.0, 0.0);
         			 		timer.delay(1.5);
         			 		otonom ++;
         	        			timer.reset();

         				}
         			}
             			while (isAutonomous() && otonom == 1 ) {
             				if (timer.get() < 1) {
                 				AArobot.drive(-0.4, 0.0);     
             				}
         				else{
         			  		AArobot.drive(0.0, 0.0); 
         			  		timer.delay(0.5);
         			 		otonom ++;
         			 		timer.reset();
         				}
            			}
             	
        	 		while (isAutonomous() && otonom == 2 ) {
           				if (timer.get() < 0.55) {      
						teker1.set(0.5);
					     	teker2.set(0.5);
					     	teker3.set(0.5);
					     	teker4.set(0.5);

           				}
       					else{
						AArobot.drive(0.0, 0.0); 
						timer.delay(0.5);
       			 			otonom ++;
       			 			timer.reset();
       					}
     				}

     	 			while (isAutonomous() && otonom == 3 ) {
					if (timer.get() < 1.1) {      
						AArobot.drive(-0.7, 0);
					}
					else {
						AArobot.drive(0.0, 0.0); 
						otonom ++;
						timer.delay(0.5);
						timer.reset();
					}
     				}
     	 
				while (isAutonomous() && otonom == 4 ) {
					if (timer.get() < 0.45) {      
						teker1.set(-0.3);
						teker2.set(-0.3);
						teker3.set(-0.3);
						teker4.set(-0.3);	

					}
					else {
						AArobot.drive(0.0, 0.0); 
						timer.delay(0.5);
						otonom ++;
						timer.reset();
						shooter.set(0.74);
					}
				}

				while (isAutonomous() && otonom == 5 ) {
					double currentDistance = ultrasonic.getValue() * kValueToInches;
					double currentSpeed = (kHoldDistance - currentDistance) * kP;
					SmartDashboard.putNumber("Uzaklik (inc) ",currentDistance);

					if (timer.get() < 5) {
						if (currentDistance > 144) {

							AArobot.drive(-0.2, 0.0);
						}
						else {
							AArobot.stopMotor();
							timer.delay(0.5);		    	
							yesil.set(Relay.Value.kOn);
							intake.set(1);
							icealma.set(1);
							shooter.set(-0.745);	
						}
					}
					else {
						otonom++;
						intake.set(0);
						icealma.set(0);
						shooter.set(0);	
						timer.reset();
					}
				}
				while (isAutonomous() && otonom == 6 ) {
					if (timer.get() < 0.45) {      
						AArobot.drive(-0.5, -0.5);
					}
					else {
						AArobot.drive(0.0, 0.0); 
						otonom++;
						timer.reset();
					}
				}
				while (isAutonomous() && otonom == 7 ) {
					if (timer.get() < 1.5) {      
						AArobot.drive(-0.7, 0);
					}
					else{
						AArobot.drive(0.0, 0.0); 
						otonom++;
						timer.reset();
						AArobot.stopMotor();
					}

				}
			
				break;
			}
     	 	
         		case 3: {
        	 
				while (isAutonomous()) {

					if (timer.get() < 3.5) {
						AArobot.drive(-0.5, 0.0); // drive forwards half speed
					} else {
						AArobot.drive(0.0, 0.0); // stop robot
					}
				}
				break;
			}


			case 4: {
				while (isAutonomous() && otonom == 0 ) {
					if (timer.get() < 2.0) {
						AArobot.drive(0.5, 0.0); // drive towards heading 0
					}
					else {
						otonom ++;
						AArobot.drive(0.0, 0.0);
					}
				}
				break;
			}
		}
	}
	public void teleopInit(){
    
    	}

 
    	public void teleopPeriodic() {

    		while (isOperatorControl() && isEnabled()) {
    			
    			yesil.set(Relay.Value.kOff);
    			double currentDistance = ultrasonic.getValue() * kValueToInches;
    			double currentSpeed = (kHoldDistance - currentDistance) * kP;
    		    	SmartDashboard.putNumber("Uzaklik (inc) ",currentDistance);
    		    
    		    	if (currentDistance < 155) {
    		    		if (currentDistance > 145) {
    		    			yesil.set(Relay.Value.kOn);
    		    		}
    		    		else {
    		    			yesil.set(Relay.Value.kOff);
    		    		}
    		    	}
		
    		    
			AArobot.arcadeDrive(sagkol);
			AArobot.setMaxOutput(0.4);
		
			if (solkol.getRawButton(3)) {
				intake.set(1);
			}
			
			else {
				intake.set(0);
				//icealma.set(0);
			}
			if (solkol.getRawButton(1)) {
				shooter.set(-0.745);
			}
			else if (sagkol.getRawButton(1)) {
				shooter.set(0.745);
			}
			
			else {
				shooter.set(0);
			}
       
			if (solkol.getRawButton(5)) {
				icealma.set(0.4);
			
			//	tirmanma1.set(0.7);
            		//	tirmanma2.set(-0.7);
			}
			else {
				icealma.set(0);
				//tirmanma1.set(0);
            			//tirmanma2.set(0);
			}
   
			if (solkol.getRawButton(6)) {
			            	
			        tirmanma1.set(1);
			       	tirmanma2.set(1);
						
			}
			if (sagkol.getRawButton(6)) {
			            	
				tirmanma1.set(-1);
				tirmanma2.set(-1);
						
			}
			else {
				tirmanma1.set(0);
            			tirmanma2.set(0);
			}
           
			if (sagkol.getRawButton(4)) {
				intake.set(-0.4);
				Timer.delay(0.23);
			 	if(sagkol.getRawButton(4)) {
					intake.set(0);
					Timer.delay(2);
				}
			}
	

			if (solkol.getRawButton(2)) {
				AArobot.setMaxOutput(1); 
			}
			if (sagkol.getRawButton(2)) { // gearayar
				if (gear <  10) {
				
					teker1.set(-0.4);
					teker2.set(-0.4);
					teker3.set(-0.4);
					teker4.set(-0.4);
				
				}
				else {
					teker1.set(0);
					teker2.set(0);
					teker3.set(0);
					teker4.set(0);
					AArobot.drive(0, 0);
					Timer.delay(0.5);
					gear = 0;
				
				}
			}
		
			if (solkol.getRawButton(10)) {
		            	
		            	tirmanma1.set(-0.3);
				tirmanma2.set(-0.3);
					
			}
			if (sagkol.getRawButton(10)) {
		            	
		            	tirmanma1.set(0.3);
		            	tirmanma2.set(0.3);	
			}
		}
			/*
			 * Nitro
			 */
	}
	
    
    
   
    	public void testPeriodic() {
    
    	}
    
}
