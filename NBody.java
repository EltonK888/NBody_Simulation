import java.io.File;
import java.util.Scanner;
import java.util.Arrays;

public class NBody {

    /* To use: java NBody 157788000.0(Total time) 25000.0(Time step) data\planets.txt(filename) */
    public static void main(String[] args) {
        // read in the time and file
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        // create the bodies and the universe radius from the file
        double radius = readRadius(filename);
        Body[] bodies = readBodies(filename);
        // draw the universe background
        StdDraw.setScale(-radius, radius);
        StdDraw.picture(0, 0, "images/starfield.jpg");
        System.out.println(radius);
        System.out.println(Arrays.toString(bodies));
        // draw each of the bodies in the universe
        for (Body i: bodies) {
            if (i == null) {
                continue;
            }
            i.draw();
        }
        StdDraw.enableDoubleBuffering();
        /* loop to update the bodies at each time step based on the forces
         * they exert on each other
         */
        double curT = 0;
        while (curT <= T) {
            // create an array that will hold net X and Y forces for each body
            double[] xForces = new double[bodies.length];
            double[] yForces = new double[bodies.length];
            // update the net force exerted on each of the bodies and store in the array
            for (int i=0; i<bodies.length; i++) {
                if (bodies[i] == null) {
                    continue;
                }
                xForces[i] = bodies[i].calcNetForceExertedByX(bodies);
                yForces[i] = bodies[i].calcNetForceExertedByY(bodies);
            }
            // change the position of the body based on the net X and Y forces
            for (int j=0; j<bodies.length; j++) {
                if (bodies[j] == null) {
                    continue;
                }
                bodies[j].update(dt, xForces[j], yForces[j]);
            }
            StdDraw.picture(0, 0, "images/starfield.jpg");
            // draw updated body
            for (Body b: bodies) {
                if (b == null) {
                    continue;
                }
                b.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            curT += dt;
        }
        // print out the ending state of the universe. Position/Velocity/Mass of each body
        System.out.println(bodies.length);
        System.out.println(radius);
        for (int i = 0; i < bodies.length; i++) {
            if (bodies[i] == null) {
                continue;
            }
            System.out.println(bodies[i].xxPos + " " + bodies[i].yyPos + " " + bodies[i].xxVel + " " +
                    bodies[i].yyVel + " " + bodies[i].mass + " " + bodies[i].imgFileName);
        }
    }

    /* Takes a file name and reads in the radius from the file which will
     * be on the second line
     */
    public static double readRadius(String filename) {
        File file = new File(filename);
        double radius = 0.0;
        try{
            Scanner sc = new Scanner(file);
            for (int i = 0; i < 2; i++) {
                radius = Double.parseDouble(sc.nextLine().trim());
            }
        } finally {
            return radius;
        }

    }

    /* Takes in a file name and returns an array of Bodies for each
     * of the bodies in the file
     */
    public static Body[] readBodies(String filename) {
        File file = new File(filename);
        String[] bodyLine;
        try {
            Scanner sc = new Scanner(file);
            String line;
            // make sure to remove leading and trailing whitespaces for each line
            line = sc.nextLine().trim();
            // get the number of bodies from the first line and initialze an array
            // based off the number of bodies
            int numBodies = Integer.parseInt(line);
            Body[] bodies = new Body[numBodies];
            line = sc.nextLine();
            // loop to find where the lines of bodies will start
            while (line.trim().split("\\s+").length != 6){
                System.out.println(Arrays.toString(line.trim().split("\\s+")));
                line = sc.nextLine();
                System.out.println(line);
            }
            // create an array of bodies
            for (int i = 0; i < numBodies; i++) {
                bodyLine = line.trim().split("\\s+");
                System.out.println(Arrays.toString(bodyLine));
                // only make the bodies if the line is formatted right
                if (bodyLine.length == 6) {
                    Body temp = new Body(Double.parseDouble(bodyLine[0]), Double.parseDouble(bodyLine[1]),
                                            Double.parseDouble(bodyLine[2]), Double.parseDouble(bodyLine[3]),
                                            Double.parseDouble(bodyLine[4]), bodyLine[5]);
                    bodies[i] = temp;
                }
                if (sc.hasNextLine()) {
                    line = sc.nextLine();
                }
            }
            return bodies;
        } catch (Exception e) {
            return null;
        }
    }
}