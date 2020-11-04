package me.ichmagomaskekse.de.geometric;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Circle {
	
	public double durchmesser = 2.0d;
	public double radius = durchmesser / 2;
	public double winkel = 23.3d;
	public double loc_x = 0d;
	public double loc_y = 0d;
	public double loc_z = 0d;
	public double calc_x = 0d;
	public double calc_y = 0d;
	public double calc_z = 0d;
	
	public Circle(double durchmesser) {
		this.durchmesser = durchmesser;
		this.radius = durchmesser / 2;
	}
	
	public Vector getLocationAtAngle(Location loc, double angle) {
		this.loc_x = loc.clone().getX();
		this.loc_y = loc.clone().getY();
		this.loc_z = loc.clone().getZ();
		/*
		 * radWinkel:= 40 / 180 * Pi;            // radWinkel = 0.698131...
  			x_koordinate:= cos( radWinkel ) * 5;  // x_koordinate = 3,830222...
  			y_koordinate:= sin( radWinkel ) * 5;  // y_koordinate = 3,213938...
		 */
		calc_x = Math.cos(angle) * (radius);
		calc_z = Math.sin(angle) * (radius);
		calc_y = 0;
		return new Vector(loc_x+calc_x, loc_y+calc_y, loc_z+calc_z);
	}
	
	public static Vector rotateAroundAxisX(Vector v, double angle) {
        angle = Math.toRadians(angle);
        double y, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        y = v.getY() * cos - v.getZ() * sin;
        z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

	public static Vector rotateAroundAxisY(Vector v, double angle) {
        angle = -angle;
        angle = Math.toRadians(angle);
        double x, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos + v.getZ() * sin;
        z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

	public static Vector rotateAroundAxisZ(Vector v, double angle) {
        angle = Math.toRadians(angle);
        double x, y, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos - v.getY() * sin;
        y = v.getX() * sin + v.getY() * cos;
        return v.setX(x).setY(y);
    }
	
}
