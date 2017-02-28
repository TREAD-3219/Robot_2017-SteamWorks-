package org.usfirst.frc3219.TREAD.commands.shooter;

import org.usfirst.frc3219.TREAD.Robot;
import org.usfirst.frc3219.TREAD.subsystems.Sensors;
import org.usfirst.frc3219.TREAD.subsystems.Turntable;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PIDCommand;

public class VisionAim extends PIDCommand {
	public static final double P = 0.1;
	public static final double I = 0.1;
	public static final double D = 0.0;
	
	private double turnRate;
	private double goalAngle;
	private double initialDegrees;
	
	public VisionAim() {
		super(P, I, D);
    	requires(Robot.sensors);
    	requires(Robot.shooter);
    	turnRate = 0;
    	goalAngle = 0;
    	initialDegrees = 0;
    }

    protected void initialize() {
    	this.setTimeout(10);
    	turnRate = 0;
    	goalAngle = 0;
    	initialDegrees = 0;
    }

    protected void execute() {
    	Robot.turntable.turnDirection(turnRate);
    }

    protected boolean isFinished() {
        return this.isTimedOut();
    }

    protected void end() {
    	Robot.turntable.turnDirection(0.0);
    }

    protected void interrupted() {
    	end();
    }
    
    private void checkAngle() {
    	double temp = (Robot.sensors.getTargetX() - Sensors.CAMERA_WIDTH/2.0) / Sensors.DEGREES_PER_PIXEL;
    	if (goalAngle != temp) {
    		goalAngle = temp;
    		initialDegrees = Robot.turntable.getAngle();
    	}
    }

	@Override
	protected double returnPIDInput() {
		checkAngle();
		return Robot.turntable.getAngle() - initialDegrees - goalAngle;
	}

	@Override
	protected void usePIDOutput(double output) {
		if (output > Turntable.TURNTABLE_FORWARD) {
			output = Turntable.TURNTABLE_FORWARD;
		} else if (output < Turntable.TURNTABLE_BACKWARD) {
			output = Turntable.TURNTABLE_BACKWARD;
		}
		turnRate = output;
	}
}