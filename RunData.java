package com.example.speedup;

public class RunData {
	public double time, distance;

	RunData(double d, double e) {
		this.time = d;
		this.distance = e;
	}

	public double getTime() {
		return time;
	}

	public double getDistance() {
		return distance;
	}
	
	public double getSpeed() {
		return distance/time;
	}
	
	public String toStringTime() {
		return time + ", " + distance + ", " + time/distance;
	}
	public String toStringDistance() {
		return distance + ", " + time + ", " + time/distance;
	}
	public String toStringSpeed() {
		return distance/time + ", " + distance + ", " + time;
	}
}
