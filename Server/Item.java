package com.yummyornot;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;

@PersistenceCapable
public class Item {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private Blob photo;
	@Persistent
	private int hour_score;
	@Persistent
	private int hour_votes;
	@Persistent
	private double hour_average;
	@Persistent
	private String notes;
	@Persistent
	private int score;
	@Persistent
	private int votes;
	@Persistent
	private double average;
	
	public Long getID() {
		return id;
	}

	public byte[] getPhoto() {
		return photo != null ? photo.getBytes() : new byte[0];
	}

	public void setPhoto(byte[] photo) {
		this.photo = new Blob(photo != null ? photo : new byte[0]);
	}
	
	
	public int getHour_Score() {
		return hour_score;
	}
	
	public int getHour_Votes() {
		return hour_votes;
	}
	
	public double getHour_Average() {
		return hour_average;
	}
	
	public void setNote(String note) {
		this.notes = note;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getVotes() {
		return votes;
	}
	
	public double getAverage() {
		return average;
	}
	
	public void newPhoto() {
		this.score = 0;
		this.votes = 0;
		this.average = 0;
		this.hour_score = 0;
		this.hour_average = 0;
		this.hour_votes = 0;
	}
	
	public void vote(int stars) {
		this.votes++;
		this.hour_votes++;
		this.hour_score = this.hour_score + stars;
		this.score = this.score + stars;
	}
	
	public int hour_score() {
		return this.hour_score;
	}
	
	public int score() {
		return this.score();
	}
	
	public void resetHour() {
		this.hour_votes = 0;
		this.hour_score = 0;
		this.hour_average = 0;
	}
	
	public void doAverage() {
		if(this.votes != 0)
			this.average = this.score / this.votes;
		else
			this.average = 0;
		if(this.hour_votes != 0)
			this.hour_average = this.hour_score / this.hour_votes;
		else
			this.hour_average = 0;
	}
	


}

