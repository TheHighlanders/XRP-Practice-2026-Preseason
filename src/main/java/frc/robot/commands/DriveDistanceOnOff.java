// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class DriveDistanceOnOff extends Command {
  private final Drivetrain m_drive;
  private final double m_distance;
  private final double m_speed;

  private final double TURN_EFFORT = 0.5;
  private final double TURN_RANGE = 0.2;


  /**
   * Creates a new DriveDistance. This command will drive your your robot for a desired distance at
   * a desired speed.
   *
   * @param speed The speed at which the robot will drive
   * @param inches The number of inches the robot will drive
   * @param drive The drivetrain subsystem on which this command will run
   */
  public DriveDistanceOnOff(double speed, double inches, Drivetrain drive) {
    m_distance = inches;
    m_speed = speed;
    m_drive = drive;
    addRequirements(drive);

    SmartDashboard.putNumber("TURN_EFFORT", TURN_EFFORT);
    SmartDashboard.putNumber("TURN_RANGE", TURN_RANGE);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_drive.arcadeDrive(0, 0);
    m_drive.resetEncoders();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double steer, error, left, right;
    left = m_drive.getLeftDistanceInch();
    right = m_drive.getRightDistanceInch();
    error = left - right;
    SmartDashboard.putNumber("LEFT_DIST", left);
    SmartDashboard.putNumber("RIGHT_DIST", right);
    SmartDashboard.putNumber("ERROR_DIST", error);

    double turnEffort = SmartDashboard.getNumber("TURN_EFFORT", TURN_EFFORT);
    double turnRange = SmartDashboard.getNumber("TURN_RANGE", TURN_RANGE);

    // error 20, pointed right, left is ahead
    // WANT TO STEER???? want to turn left
    if(error > turnRange) {
      steer = turnEffort;
    } else if(error < -turnRange) {
      steer = -turnEffort;
    } else {
      steer = 0;
    }
    SmartDashboard.putNumber("STEER", steer);

    m_drive.arcadeDrive(m_speed, steer);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_drive.arcadeDrive(0, 0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // Compare distance travelled from start to desired distance
    return Math.abs(m_drive.getAverageDistanceInch()) >= m_distance;
  }
}
