package passage_system;


import java.util.ArrayList;
import java.util.List;

public class WorkerHourWorkList {
	
	private List<WorkerHourWork> workHowrList;

    public WorkerHourWorkList() {

    	workHowrList = new ArrayList<>();
    }

    public void addWorkerHourWorkToList(WorkerHourWork workerHour) {
    	workHowrList.add(workerHour);
    }

    public List<WorkerHourWork> getWorkerHourWorkList() {
        return workHowrList;
    }

}
