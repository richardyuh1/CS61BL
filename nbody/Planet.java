public class Planet {
	double xxPos;
	double yyPos;
	double xxVel;
	double yyVel;
	double mass; 
	String imgFileName; 
	private static final double gConstant = 6.67 * Math.pow(10,-11);

	public Planet(double xP, double yP, double xV, double yV, double m, String img) {
		xxPos = xP;
		yyPos = yP;
		xxVel = xV; 
		yyVel = yV;
		mass = m;
		imgFileName = img; 
	}

	public Planet(Planet p) {
		this.xxPos = p.xxPos;
		this.yyPos = p.yyPos;
		this.xxVel = p.xxVel;
		this.yyVel = p.yyVel;
		this.mass = p.mass;
		this.imgFileName = p.imgFileName;
	}

	public double calcDistance(Planet p){
		double rDistance = 0;
		double xDistance = p.xxPos - this.xxPos;
		double yDistance = p.yyPos - this.yyPos;
		rDistance = Math.sqrt(xDistance * xDistance + yDistance * yDistance);
		return rDistance;
	}

	public double calcForceExertedBy(Planet p) {
		return (gConstant * this.mass * p.mass)/(Math.pow(calcDistance(p),2)); 
	}

	public double calcForceExertedByX(Planet p){
		return calcForceExertedBy(p) * (p.xxPos - this.xxPos) / calcDistance(p);
	}

	public double calcForceExertedByY(Planet p){
		return calcForceExertedBy(p) * (p.yyPos - this.yyPos) / calcDistance(p); 
	}

	public double calcNetForceExertedByX(Planet[] allPlanets){
		double netForceX = 0; 
		for (int i = 0; i<allPlanets.length; i++){
			if (this.equals(allPlanets[i])) {
				continue;
			}
			netForceX += calcForceExertedByX(allPlanets[i]);
		}
		return netForceX; 
	}

	public double calcNetForceExertedByY(Planet[] allPlanets){
		double netForceY = 0; 
		for (int i = 0; i<allPlanets.length; i++) {
			if (this.equals(allPlanets[i])) {
				continue;
			}
			netForceY += calcForceExertedByY(allPlanets[i]);
		}
		return netForceY; 
	}

	public void update(double dt, double fX, double fY) {
		double aX = fX/(this.mass);
		double aY = fY/(this.mass);
		this.xxVel += dt * aX; 
		this.yyVel += dt * aY; 
		this.xxPos += dt * xxVel; 
		this.yyPos += dt * yyVel; 
	}

	public void draw() {
		StdDraw.picture(xxPos, yyPos, "./images/" + imgFileName); 
	}

}