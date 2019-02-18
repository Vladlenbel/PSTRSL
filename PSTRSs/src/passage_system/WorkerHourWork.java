package passage_system;

import java.util.Date;

public class WorkerHourWork {
	
	private String fio;
	private String title;
	private String hourWork;
	private int late;
	private int autoEx;
	private String loseTime;
	private int notCom;

	public WorkerHourWork(String fio, String title, String hourWork, int late, int autoEx, String loseTime, int notCom ) {
		this.fio = fio;
		this.title = title;
		this.hourWork = hourWork;
		this.late = late;
		this.autoEx = autoEx;
		this.loseTime = loseTime;
		this.notCom = notCom;
		
	}
	
	public String getFIO() {
		return fio;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getHourWork() {
		return hourWork;
	}
	
	public int getLate() {
		return late;
	}
	
	public int getAutoEx() {
		return autoEx;
	}
	
	public String getLoseTime() {
		return loseTime;
	}
	
	public int getNotCom() {
		return notCom;
	}
}
