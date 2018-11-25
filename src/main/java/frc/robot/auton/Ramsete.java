package frc.robot.auton;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.Drivetrain;
import frc.util.auton.RamseteUtil;
import frc.util.kinematics.pos.Pose2d;
import frc.util.logging.Loggable;

public class Ramsete extends RamseteUtil implements Runnable{

    private static Ramsete instance;
    public static Ramsete getInstance(){
        if (instance == null)
            instance = new Ramsete(Robot.kRamseteTimestep);
        return instance;
    }

    private Notifier mNotifier;

    public Loggable mPathLogger;
    private final double kTimestep;

    public Ramsete(double timestep){
        super(Constants.kWheelBase, timestep);
        mNotifier = new Notifier(this);
        setupLogger();
        kTimestep = timestep;
    }

    public void start(){
        mNotifier.startPeriodic(kTimestep);
    }

    public void stop(){
        mNotifier.stop();
    }

    @Override
    public Pose2d getPose2d(){
        return Drivetrain.masterPos;
    }

    @Override
    public void run() {
        this.update();

        Drivetrain.getInstance().setVelocity(this.getVels().getLeft(), this.getVels().getRight());
        mPathLogger.log();
    }

    private void setupLogger(){
        mPathLogger = new Loggable(){
            @Override
            protected LogObject[] collectData() {
                return new LogObject[]{
                    new LogObject("Time", Timer.getFPGATimestamp()),
                    new LogObject("Type", "P"),
                    new LogObject("X Pos", mGoal.getPos().getTranslation().x()),
                    new LogObject("Y Pos", mGoal.getPos().getTranslation().y()),
                    new LogObject("Heading", mGoal.getPos().getRotation().getRadians())
                };
            }
        };
    }
}