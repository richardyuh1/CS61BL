public class NBody {
	
	public static double readRadius(String s) {
		In in = new In(s);
		int firstItem = in.readInt();
		double secondItem = in.readDouble();
		return secondItem; 
	}

	public static Planet[] readPlanets(String s) {
		In in = new In(s);
		int firstItem = in.readInt();
		double secondItem = in.readDouble();
		Planet[] array = new Planet[firstItem];
		for (int i = 0; i < firstItem; i++) {
			array[i] = new Planet(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readString());
		}
		return array; 
	}

	public static void main(String[] args) {
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]); 
		String filename = args[2]; 
		double radius = readRadius(filename);
		Planet[] planets = readPlanets(filename); 
		StdDraw.setScale(-radius, radius);
		StdDraw.clear();
		StdDraw.picture(0.0, 0.0, "./images/starfield.jpg");
		for (int i = 0; i < planets.length; i++){
			planets[i].draw(); 
		}
		StdDraw.enableDoubleBuffering(); 
		double time = 0; 
		while (time < T){
			Double[] xForces = new Double[planets.length];
			Double[] yForces = new Double[planets.length];
			for (int j = 0; j < planets.length; j++) {
				xForces[j] = planets[j].calcNetForceExertedByX(planets);
				yForces[j] = planets[j].calcNetForceExertedByY(planets); 
			}
			for (int k = 0; k < planets.length; k++){
				planets[k].update(dt, xForces[k], yForces[k]);
			}
			StdDraw.picture(0.0, 0.0, "./images/starfield.jpg");
			for (int i = 0; i < planets.length; i++){
				planets[i].draw(); 
			}
			StdDraw.show();
			StdDraw.pause(10);
			StdDraw.enableDoubleBuffering(); 
			time+=dt;
		}
		StdOut.printf("%d\n", planets.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < planets.length; i += 1) {
    	StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                  planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                  planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
		}
	}
}