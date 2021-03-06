package org.usfirst.frc.team5687.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team5687.robot.commands.AutonomousDoNothing;
import org.usfirst.frc.team5687.robot.commands.AutonomousDriveOnly;
import org.usfirst.frc.team5687.robot.commands.AutonomousLiftAndDrive;
import org.usfirst.frc.team5687.robot.commands.AutonomousResetAndDrive;
import org.usfirst.frc.team5687.robot.commands.AutonomousResetLiftAndDrive;
import org.usfirst.frc.team5687.robot.commands.AutonomousResetOnly;
import org.usfirst.frc.team5687.robot.subsystems.DriveTrain;
import org.usfirst.frc.team5687.robot.subsystems.Stacker;

/**
 * Main robot class
 */
public class Robot extends IterativeRobot {

	
	public static DriveTrain driveTrain;
	public static Stacker stacker;
	public static OI oi;
	

    Command autonomousCommand;
    SendableChooser autoChooser;
    
    CameraServer server;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		driveTrain = new DriveTrain();
		stacker = new Stacker();
		oi = new OI();
    	
    	// Set up the autonomous choices...
    	autoChooser = new SendableChooser();
    	
    	// By default, do nothing at all.  This is the safest choice :P
    	autoChooser.addDefault("Default - None", new AutonomousDoNothing());
        
    	// Options are displayed in increasing order of risk
		autoChooser.addObject("Reset Stacker ONLY", new AutonomousResetOnly());
		autoChooser.addObject("Reset and Drive ONLY", new AutonomousResetAndDrive());
		autoChooser.addObject("Lift and Drive ONLY", new AutonomousLiftAndDrive());
		autoChooser.addObject("Drive ONLY", new AutonomousDriveOnly());
		autoChooser.addObject("Reset, Drive and Lift", new AutonomousResetLiftAndDrive());

    	// Add the chooser to the dashboard - NOT TESTED YET
		SmartDashboard.putData("Autonomous Mode Chooser", autoChooser);
		
    	// Setup camera streaming, working
        try {
    		server = CameraServer.getInstance();
    		server.setQuality(50);
    		server.startAutomaticCapture("cam2"); 
    				
    	} catch (Exception e) {
    		
    	}
        
        // end of camera stuff. 
    	
		
		updateDashboard();
		
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

    public void autonomousInit() {
        // schedule the autonomous command (example)
    	autonomousCommand = (Command)autoChooser.getSelected();
    	if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        updateDashboard();
    }

    public void teleopInit() {
		// Stop autonomous commands when teleop starts
        if (autonomousCommand != null) autonomousCommand.cancel();
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        updateDashboard();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
    
    private void updateDashboard()
    {
    	SmartDashboard.putData(this.driveTrain);
    	SmartDashboard.putData(this.stacker);
    }
}
