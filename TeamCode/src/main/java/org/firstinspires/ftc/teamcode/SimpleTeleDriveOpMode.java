/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a PushBot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Simple 2 Motor OpMode", group="Linear Opmode")  // @Autonomous(...) is the other common choice
//@Disabled
public class SimpleTeleDriveOpMode extends LinearOpMode {

    float speedScaling;
    boolean dPadPressed ;


    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
     DcMotor leftMotor = null;
     DcMotor rightMotor = null;
    Servo armServo = null;

    @Override
    public void runOpMode() throws InterruptedException {
        speedScaling = 2.0f;
        dPadPressed = false;
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        leftMotor  = hardwareMap.dcMotor.get("left_drive");
        rightMotor = hardwareMap.dcMotor.get("right_drive");
        armServo = hardwareMap.servo.get("arm");
        armServo.setPosition(0.5);
        // eg: Set the drive motor directions:
        // "Reverse" the motor that runs backwards when connected directly to the battery
        // leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        // rightMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
//            telemetry.update();

            // eg: Run wheels in tank mode (note: The joystick goes negative when pushed forwards)
            Log.i("DRW", String.format("Old scalar: %f", speedScaling));
            if (gamepad1.dpad_down && !dPadPressed) {
                speedScaling *= 2.0;
                dPadPressed = true;
            }
            if (gamepad1.dpad_up && !dPadPressed) {
                speedScaling /= 2.0;
                dPadPressed = true;
            }
            if (!gamepad1.dpad_down && !gamepad1.dpad_up) {
                dPadPressed = false;
            }
            Log.i("DRW", String.format("New scalar: %f", speedScaling));
            // our motors are reversed so we are swapping left and right. 12/2016
            double rightPower = -gamepad1.left_stick_y/speedScaling;
            double leftPower = +gamepad1.right_stick_y/speedScaling;
            if (leftPower <= -1.0 )
            {
                leftPower = -0.99;
            }
            if (rightPower <= -1 )
            {
                rightPower = -0.99;
            }
            if (leftPower >= 1 )
            {
                leftPower = 0.99;
            }
            if (rightPower >= 1 )
            {
                rightPower = 0.99;
            }
            leftMotor.setPower(leftPower);
            rightMotor.setPower(rightPower);
            if (gamepad1.dpad_right)
                armServo.setPosition(1.0);
            if (gamepad1.dpad_left)
                armServo.setPosition(0.0);
            telemetry.addData("Servo Position", armServo.getPosition());
            telemetry.addData("Left Power", leftPower);
            telemetry.addData("right Power", rightPower);
            telemetry.addData("speedScaling", speedScaling);
            telemetry.update();

            idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
        }
    }
}
