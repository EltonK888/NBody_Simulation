import java.lang.Math;

public class Body {

    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    public static final double g = 6.67e-11;

    public Body(double xP, double yP, double xV,
                double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Body(Body b) {
        xxPos = b.xxPos;
        yyPos = b.yyPos;
        xxVel = b.xxVel;
        yyVel = b.yyVel;
        mass = b.mass;
        imgFileName = b.imgFileName;

    }

    /* Return a double that is the distance between the two bodies */
    public double calcDistance(Body b) {
        double distX = this.xxPos - b.xxPos;
        double distY = this.yyPos -b.yyPos;
        return Math.sqrt(((distX * distX) + (distY * distY)));
    }

    /* Return a double that is the force exerted by Body b on this */
    public double calcForceExertedBy(Body b) {
        return ((g * this.mass * b.mass)/(Math.pow(this.calcDistance(b), 2)));
    }

    /* Return a double that is the X-axis force exerted by Body b on this */
    public double calcForceExertedByX(Body b) {
        double dx = b.xxPos - this.xxPos;
        return ((this.calcForceExertedBy(b) * dx) / this.calcDistance(b));
    }

    /* Return a double that is the Y-axis force exerted by Body b on this */
    public double calcForceExertedByY(Body b) {
        double dy = b.yyPos - this.yyPos;
        return ((this.calcForceExertedBy(b) * dy) / this.calcDistance(b));
    }

    /* Return a double that is the net X-axis force exerted by all the bodies in arrayB on this */
    public double calcNetForceExertedByX(Body[] arrayB) {
        double netFX = 0;
        for (Body b : arrayB) {
            if (b == null || this == b) {
                continue;
            }
            netFX += this.calcForceExertedByX(b);
        }
        return netFX;
    }

    /* Return a double that is the net Y-axis force exerted by all the bodies in arrayB on this */
    public double calcNetForceExertedByY(Body[] arrayB) {
        double netFY = 0;
        for (Body b : arrayB) {
            if (b == null || this == b) {
                continue;
            }
            netFY += this.calcForceExertedByY(b);
        }
        return netFY;
    }

    /* Update the body position and new velocity based on the net X and Y axes forces */
    public void update(double dt, double fX, double fY) {
        double aX = fX/this.mass;
        double aY = fY/this.mass;
        this.xxVel = this.xxVel + (dt*aX);
        this.yyVel = this.yyVel + (dt*aY);
        this.xxPos = this.xxPos + (dt*this.xxVel);
        this.yyPos = this.yyPos + (dt*this.yyVel);
    }

    /* Draw the body on the canvas */
    public void draw() {
        StdDraw.picture(this.xxPos, this.yyPos, "images/"+this.imgFileName);
    }
}