package passage_system;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Database {
	
	private final String user = /*"root"*/ "root"/*"passagesys"*/;
    private final String url = "jdbc:mysql://localhost:3306/passage_system?useUnicode=true&characterEncoding=UTF-8";
    private final String password =/*"serverps"*/"valdistroer"/*"AstZhq4"*/;

    private Connection connection;
    private Statement statement;
    private SQLException ex = new SQLException();
    private ResultSet resultSet;
    
    private int firstStart = 0;
    
    public Database() {
    	 try {
         	// System.out.println("class foundStart");
         	 Class.forName("com.mysql.jdbc.Driver");
              //System.out.println("class foundFin");
          } catch (Exception e) {
         	 	//System.out.println("class problem1");
         	 	//System.out.println(e);
         	 	//System.out.println("class problem2");
         	 	e.toString();
         	 	System.out.println(e.toString());
         	 	System.out.println("error connection");
          }
     }
    
    private void sendQuery(String query) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
        		ex = e;
        } finally {
            try {
            	System.out.println(ex.toString()); 
                connection.close();
                statement.close();
            } catch (SQLException e) {

            }
        }
    }
    
    private void closeDB() {
        try {
            connection.close();
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
        	System.out.println("������");
        }
    }  
    
    public void addRecordTime(String  id ) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String curDate = dateFormat.format(new Date());
        
        String isNormal ;
    	if (statusWorker(curDate) == 0) {
    		 isNormal = "101";
    	}
    	else {
    		isNormal = "100";
    	}
    	//System.out.println(curDate);
    	
    	
    		String query = "INSERT INTO worker VALUES(" + "\"" +  id + "\""  + "," +
    					"\"" + curDate.toString( ) + "\"" +  "," +
    					"\"" + isNormal + "\"" + ");" ;
    		
    	sendQuery(query);
    	
    }
    
    public String getSoundName(String id) {
    	String answ = null;
    	String query = "SELECT filename FROM sound INNER JOIN workerfio ON "
    			+ "  sound.personellNumber = workerfio.personellNumber WHERE workerIdCard = '" + id + "';";
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        while (resultSet.next()) {
	        answ = resultSet.getString("filename");
	        }

    	}catch (SQLException e) {

        } finally {
            closeDB();
        }
    	return answ ;
    	
    }
    
    public String dateFinder (String dateStart, String dateFin, String surn, String statusCom, String depart) {
    //	System.out.println("FIO ="+statusCom.replaceAll("\n","")+"sgdf");
    	//if (surn.equals("null") == false) {
    	String[] surname = surn.split("\\s+");
    	int status = 0;
    	int normEntr = 0;
    	if (statusCom.equals("") == false) {
	    	if (statusCom.replaceAll("\n","").equals("���������")) {
	    		status = 101;
	    		}
	    	if (statusCom.replaceAll("\n","").equals("����")) {
	    		status = 100;
	    		normEntr = 102;
	    		}
	    	if (statusCom.replaceAll("\n","").equals("�����")) {
	    		status = 200;
	    		}
	    	if (statusCom.replaceAll("\n","").equals("���������")) {
	    		status = 201;
	    		}
	    	if (statusCom.replaceAll("\n","").equals("�� ������")) {
	    		status = 300;
	    		}
    	}
    	String answ = new String();
    	
    	String query = "SELECT * FROM (worker INNER JOIN workerfio ON worker.workerId = workerfio.personellNumber)"
    			+ "INNER JOIN department ON workerfio.department = department.id"
    			+ " WHERE eventTime >=" + "\"" + dateStart+ "\"";	
    	if (dateFin.equals("") == false) {
    		dateFin = dateFin + " 23:59:59";
    		//System.out.println(dateFin);
    		query += "AND eventTime <=" + "\"" + dateFin.replaceAll("\n","") + "\"";
    	}
    	if (surn.equals("") == false) {
    		query += "AND surname =" + "\"" + surname[0].replaceAll("\n","") + "\"";
    	}
    	if (surn.equals("") == false) {
    		query += "AND name =" + "\"" + surname[1].replaceAll("\n","") + "\"";
    	}
    	if (surn.equals("") == false) {
    		query += "AND patronymic =" + "\"" + surname[2].replaceAll("\n","") + "\"";
    	}
    	if (statusCom.equals("") == false) {
    		query += "AND statuscom =" + "\"" + status + "\"";
    	}
    	if (status == 100) {
    		query += "AND statuscom =" + "\"" + normEntr + "\"";
    	}
    	if (depart.equals("") == false) {
    		query += "AND title =" + "\"" + depart.replaceAll("\n","") + "\"";
    	}
    	query += "AND status =" + "\"" + "��������" + "\"";
    	query += "ORDER BY eventTime" ;
    	System.out.println(query);
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);;
	      //  answ += "<html><head><meta charset= \"UTF-8\" ></head>";
	        answ += "<table border= \" 1\">"+  
	        		   "<tr>"+  
	        		   "<th>ID</th>" + 
	        		  "<th>���</th>"+ 
	        		  "<th>�����</th>"+ 
	        		  "<th>������</th>"+ 
	        		  "<th>�����</th>"+
	        		  "</tr>";
	        while (resultSet.next()) {
	        	String statusWord = null;
	        		if (Integer.parseInt( resultSet.getString("statuscom")) == 101) {
	        			statusWord = "���������";
	        		}
	        		if (Integer.parseInt( resultSet.getString("statuscom")) == 100) {
	        			statusWord = "����";
	        		}
	        		if (Integer.parseInt( resultSet.getString("statuscom")) == 102) {
	        			statusWord = "����";
	        		}
	        		if (Integer.parseInt( resultSet.getString("statuscom")) == 200) {
	        			statusWord = "�����";
	        		}
	        		if (Integer.parseInt( resultSet.getString("statuscom")) == 201) {
	        			statusWord = "���������";
	        		}
	        		if (Integer.parseInt( resultSet.getString("statuscom")) == 300) {
	        			statusWord = "�� ������";
	        		}
	        		
	        	int count = 0;
	        	
	        	if  ( Integer.parseInt( resultSet.getString("statuscom")) == 300 && count == 0 && resultSet.getString("surname").equals("Unknown")) {
	        		count++;
	        	}
	        	if( Integer.parseInt( resultSet.getString("statuscom")) == 300 && count == 0   ) {
	        		
	        		String date = resultSet.getString("eventTime");
		        	String time = date.substring(10);
		        	String year = date.substring(0,4);
		        	String month = date.substring(5,7);
		        	String day = date.substring(8, 10);
		        	String resultDay = day + "-" + month + "-" + year + " "+ time;
	        	
	        		   answ += "<tr>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultSet.getString("personellNumber") +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultDay.substring(0,11) +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ statusWord +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultSet.getString("title") +" </td>";
		        	   answ += "</tr>";
		        	   count++;
	        	}
	        	
	        	if( resultSet.getString("surname").equals("Unknown") && count == 0 ) {
		        	String date = resultSet.getString("eventTime");
		        	String time = date.substring(10);
		        	String year = date.substring(0,4);
		        	String month = date.substring(5,7);
		        	String day = date.substring(8, 10);
		        	String resultDay = day + "-" + month + "-" + year + " "+ time;
	        		   answ += "<tr>";
		        	   answ += "<td style=\"color:#0000cd\">"+ resultSet.getString("personellNumber") +" </td>";
		        	   answ += "<td style=\"color:#0000cd\">"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
		        	   answ += "<td style=\"color:#0000cd\">"+ resultDay +" </td>";
		        	   answ += "<td style=\"color:#0000cd\">"+ statusWord +" </td>";
		        	   answ += "<td style=\"color:#0000cd\">"+ resultSet.getString("title") +" </td>";
		        	   answ += "</tr>";
		        	   count++;
	        	}
	        	if( Integer.parseInt( resultSet.getString("statuscom")) == 101 && count == 0) {
	        		
	        		String date = resultSet.getString("eventTime");
		        	String time = date.substring(10);
		        	String year = date.substring(0,4);
		        	String month = date.substring(5,7);
		        	String day = date.substring(8, 10);
		        	String resultDay = day + "-" + month + "-" + year + " "+ time;
	        	
	        		   answ += "<tr>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultSet.getString("personellNumber") +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultDay +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ statusWord +" </td>";
		        	   answ += "<td style=\"color:#ff0000\">"+ resultSet.getString("title") +" </td>";
		        	   answ += "</tr>";
		        	   count++;
	        	}

	        	/*if( Integer.parseInt( resultSet.getString("statuscom")) == 101 && count == 0) {
		        	
	        		   answ += "<tr>";
		        	   answ += "<td style=\"color:#ff00ff\">"+ resultSet.getString("personellNumber") +" </td>";
		        	   answ += "<td style=\"color:#ff00ff\">"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
		        	   answ += "<td style=\"color:#ff00ff\">"+ resultSet.getString("eventTime") +" </td>";
		        	   answ += "<td style=\"color:#ff00ff\">"+ statusWord +" </td>";
		        	   answ += "<td style=\"color:#ff00ff\">"+ resultSet.getString("title") +" </td>";
		        	   answ += "</tr>";
		        	   count++;
	        	}*/
	        	if( Integer.parseInt( resultSet.getString("statuscom")) == 201 && count == 0) {
	        		
	        		String date = resultSet.getString("eventTime");
		        	String time = date.substring(10);
		        	String year = date.substring(0,4);
		        	String month = date.substring(5,7);
		        	String day = date.substring(8, 10);
		        	String resultDay = day + "-" + month + "-" + year + " "+ time;
	        	
	        		   answ += "<tr>";
		        	   answ += "<td style=\"color:#ff8c00\">"+ resultSet.getString("personellNumber") +" </td>";
		        	   answ += "<td style=\"color:#ff8c00\">"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
		        	   answ += "<td style=\"color:#ff8c00\">"+ resultDay +" </td>";
		        	   answ += "<td style=\"color:#ff8c00\">"+ statusWord +" </td>";
		        	   answ += "<td style=\"color:#ff8c00\">"+ resultSet.getString("title") +" </td>";
		        	   answ += "</tr>";
		        	   count++;
	        	}

	        	if ((Integer.parseInt( resultSet.getString("statuscom")) == 100 || 
	        			Integer.parseInt( resultSet.getString("statuscom")) == 102 ||
	        				Integer.parseInt( resultSet.getString("statuscom")) == 200 ) && count == 0) {
	        		
	        		String date = resultSet.getString("eventTime");
		        	String time = date.substring(10);
		        	String year = date.substring(0,4);
		        	String month = date.substring(5,7);
		        	String day = date.substring(8, 10);
		        	String resultDay = day + "-" + month + "-" + year + " "+ time;
	        	
	        	   answ += "<tr>";
	        	   answ += "<td>"+ resultSet.getString("personellNumber") +" </td>";
	        	   answ += "<td>"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
	        	   answ += "<td>"+ resultDay +" </td>";
	        	   answ += "<td>"+ statusWord +" </td>";
	        	   answ += "<td>"+ resultSet.getString("title") +" </td>";
	        	   answ += "</tr>";
	        	}

	        
	        }

    	}catch (SQLException e) {

        } finally {
            closeDB();
        }
    	
    	 //System.out.println(answ);
    	return answ;
    }
    
    private int statusWorker (String date) {
    	int good = 0;
    	String query = "SELECT * FROM workingHours WHERE id = 1;";
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        while (resultSet.next()) {
	        	good++;
	        	//System.out.println("aaa11");
	        }
	       // System.out.println(good);
	        if (good == 0) {
	        	return 0;
	        }
	  
	        	
    	}catch (SQLException e) {

        } finally {
            closeDB();
        }   	
    
    	
    return  1;	
    }

    public String addMesForWorker (String surnMes, String soundMes, String typeCom) {
    	
    	if(surnMes.equals("")) {
    		return "<font size=\"3\" color=\"red\" face=\"Arial\">������� ������� ���������� </font>";
    	}
    	String[] fio = surnMes.split("\\s+");
    	String personalNum = new String();
    	String isCompl = new String();
		String tableNum = " SELECT distinct personellNumber FROM workerfio WHERE surname =" + "\"" + fio[0] + "\""+
    	"AND name =" + "\"" + fio[1] + "\"" + "AND patronymic =" + "\"" + fio[2] + "\"";
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(tableNum);;
	        while (resultSet.next()) {
	        	personalNum += resultSet.getString("personellNumber") ;
	        }
	        if (personalNum.equals("")) {
	        	return "<font size=\"3\" color=\"red\" face=\"Arial\">��������� c �������� " +surnMes +" �� ������ </font>";
	        }

    	}catch (SQLException e) {

        } finally {
            closeDB();
        }
        if (soundMes.equals("�������� ���������")) {
        	return "<font size=\"3\" color=\"red\" face=\"Arial\">�� ������� ���������, ������� ������� �������� </font>";
        }
        if (typeCom.equals("�������� ������ ����������")) {
        	return "<font size=\"3\" color=\"red\" face=\"Arial\">�� ������ ������ ���������� </font>";
        }
    	//System.out.println(personalNum);
    	String sendMess = "INSERT INTO message ( personellNumber,fileToPlay, notification_type,"
    			+ " isComplete) VALUES(" + "\"" +  personalNum + "\""  + "," +
				"\"" + soundMes  + "\"" +  "," + "\"" + typeCom + "\"" +  "," +
				"\"" + "20" + "\"" +  ");" ;
    	
    	sendQuery(sendMess);
		return "<font size=\"3\" color=\"green\" face=\"Arial\">��������� ��� ���������� "+surnMes+" ������� ���������"+" </font>";
    }
    
  /*  public void task() throws SchedulerException
    {
        // ��������� Schedule Factory
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        // ��������� ����������� �� schedule factory
        Scheduler scheduler = schedulerFactory.getScheduler();
         
        // ������� �����
        long ctime = System.currentTimeMillis(); 
         
        // ��������� JobDetail � ������ �������,
        // ������� ������� � ������� ������������ �������
        JobDetail jobDetail = 
            new JobDetail("jobDetail2", "jobDetailGroup2", AddRecord.class);
        // ��������� CronTrigger � ��� ������ � ������ ������
        CronTrigger cronTrigger = new CronTrigger("cronTrigger", "triggerGroup2");
        try {
            // ������������� CronExpression
            CronExpression cexp = new CronExpression("0/5 * * * * ?");
            // ����������� CronExpression CronTrigger'�
            cronTrigger.setCronExpression(cexp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ��������� ������� � ������� JobDetail � Trigger
        scheduler.scheduleJob(jobDetail, cronTrigger);
         
        // ��������� �����������
        scheduler.start();
    }*/
   public String getLastName() {
    	String lastName = new String();
    	int index = 0;
    	String query = "SELECT *  FROM workerfio;";
    	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        while (resultSet.next()) {
	        	lastName += "<option value='";
	        	//System.out.println(resultSet.getString("surname"));
	        		lastName+= resultSet.getString("surname");
	        		lastName+= "'>";
	        		lastName+= resultSet.getString("surname");
	        		lastName+= "</option>";
	        	//index++;
	        }
	        	
    	}catch (SQLException e) {

        } finally {
            closeDB();
        } 
    	// System.out.println(lastName);
    	return lastName;
    }
   
   public String getDepar() {
   	String depar = new String();
   	int index = 0;
   	String query = "SELECT *  FROM department;";
   	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        int kol = 0;
	        while (resultSet.next()) {
	        	if (kol != 0) {
	        		depar += ",";
	        	}
	        	depar+= resultSet.getString("title");
	        	kol++;
	        	/*depar += "<option value='";
	        	//System.out.println(resultSet.getString("surname"));
	        	depar+= resultSet.getString("title");
	        	depar+= "'>";
	        	depar+= resultSet.getString("title");
	        	depar+= "</option>";
	        	//index++;*/
	        }
	        	
   	}catch (SQLException e) {

       } finally {
           closeDB();
       } 
   	 //System.out.println(depar);
   	return depar;
   }
   public String getFIO() {
	   if (firstStart == 0) {
		   new TimeListener().start();
		   firstStart++;
	   }
	   	String fio = new String();
	   	int index = 0;
	   	String query = "SELECT *  FROM workerfio;";
	   	try{
		        connection = DriverManager.getConnection(url, user, password);
		        statement = connection.createStatement();
		        resultSet = statement.executeQuery(query);
		        int kol = 0;
		        while (resultSet.next()) {
		        	if (kol != 0) {
		        		fio += ",";
		        	}
		        	fio+= resultSet.getString("surname");
		        	fio+= " ";
		        	fio+= resultSet.getString("name");
		        	fio+= " ";
		        	fio+= resultSet.getString("patronymic");
		        	   kol++;
		        	/*fio += "<option value='";
		        	//System.out.println(resultSet.getString("surname"));
		        	fio+= resultSet.getString("surname");
		        	fio+= " ";
		        	fio+= resultSet.getString("name");
		        	fio+= " ";
		        	fio+= resultSet.getString("patronymic");
		        	fio+= "'>";
		        	fio+= resultSet.getString("surname");
		        	fio+= " ";
		        	fio+= resultSet.getString("name");
		        	fio+= " ";
		        	fio+= resultSet.getString("patronymic");
		        	fio+= "</option>";
		        	//index++;*/
		        }
		        	
	   	}catch (SQLException e) {

	       } finally {
	           closeDB();
	       } 
	   	
	   	// System.out.println(fio);
	   	return fio;
	   }
   
   public String getStatus() {
	   	String status = new String();
	   	
	   	String query = "SELECT DISTINCT statuscom  FROM worker;";
	   	try{
		        connection = DriverManager.getConnection(url, user, password);
		        statement = connection.createStatement();
		        resultSet = statement.executeQuery(query);
		        int kol = 0;
		        while (resultSet.next()) {
		        	if (resultSet.getString("statuscom").equals("102") == false) {
		        		if (kol != 0 ) {
		        		status += ",";
		        		}
		        	}
		        	
		    	    	if (resultSet.getString("statuscom").equals("101")) {
		    	    		status += "���������";
		    	    		}
		    	    	if (resultSet.getString("statuscom").equals("100")) {
		    	    		status += "����";
		    	    		}
		    	    	if (resultSet.getString("statuscom").equals("200")) {
		    	    		status += "�����";
		    	    		}
		    	    	if (resultSet.getString("statuscom").equals("201")) {
		    	    		status += "���������";
		    	    		}
		    	    	if (resultSet.getString("statuscom").equals("300")) {
		    	    		status += "�� ������";
		    	    		}
		        	
		        	//status+= resultSet.getString("statuscom");
		        	kol++;
		        	/*depar += "<option value='";
		        	//System.out.println(resultSet.getString("surname"));
		        	depar+= resultSet.getString("title");
		        	depar+= "'>";
		        	depar+= resultSet.getString("title");
		        	depar+= "</option>";
		        	//index++;*/
		        }
		        	
	   	}catch (SQLException e) {

	       } finally {
	           closeDB();
	       } 
	   	 System.out.println(status);
	   	return status;
	   }
   
   String notCom (String date) {
	   String answ = new String();
	   
   	//int good = 0;
   	String query = "select * from workerfio inner join department ON workerfio.department = department.id"
   			+ " where workerIdCard != \"null\" "
   			+ "and personellNumber NOT IN "
   			+ "(select workerId from worker where eventTime >="+ "\"" + date+ "\""+" group by workerId)";
   	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        answ += "<table border= \" 1\">"+  
	        		   "<tr>"+  
	        		   "<th>ID</th>" + 
	        		  "<th>���</th>"+ 
	        		  "<th>�����</th>"+
	        		  "</tr>";
	        while (resultSet.next()) {
	        	answ += "<tr>";
	        	   answ += "<td>"+ resultSet.getString("personellNumber") +" </td>";
	        	   answ += "<td>"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
	        	   answ += "<td>"+ resultSet.getString("title") +" </td>";
	        	   answ += "</tr>";
	  
	        }
   	}catch (SQLException e) {

       } finally {
           closeDB();
       }  
	   
	   
	   return answ;
   }
   
   public String birthday () {
	   getHoursWorked("1","���","1","1");
	   String answ = new String();
	   
	   SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM"); //HH:mm:ss");
       String curDate = dateFormat.format(new Date());
	   
       
       String query = "select * from workerfio inner join department"
       		+ " ON workerfio.department = department.id where birthday like '" + curDate + "%';";
       
       
      	try{
	        connection = DriverManager.getConnection(url, user, password);
	        statement = connection.createStatement();
	        resultSet = statement.executeQuery(query);
	        
	       
	       int isEmpty = 0;
	       while (resultSet.next()) {
	    	   isEmpty++;
	       }
	       System.out.println(isEmpty);
	        //System.out.println(resultSet.wasNull());
	        if (isEmpty != 0) {
	        	resultSet.beforeFirst();
	        answ += "<div class =\"birtdayTitle\">";	
	        answ += "<font size=\"5\" color=\"black\" face=\"Arial\">������� ���� �������� </font>";
	        answ += "</div>";
	        answ += "<div class =\"birtdayTable\">";
	        answ += "<table border= \" 1\">"+  
	        		   "<tr>"+  
	        		  "<th>���</th>"+ 
	        		  "<th>�����</th>"+
	        		  "<th>���������</th>" +
	        		  "<th>�����������</th>" +
	        		  "</tr>";
	        while (resultSet.next()) {
	        	answ += "<tr>";
	        	   answ += "<td>"+ resultSet.getString("surname")+" "+ resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" </td>";
	        	   answ += "<td>"+ resultSet.getString("title") +" </td>";
	        	   answ += "<td>"+ resultSet.getString("position") +" </td>";
	        	   
	        	   SimpleDateFormat todayYear = new SimpleDateFormat("YYYY"); //HH:mm:ss");
	               String curYear = todayYear.format(new Date());
	               int curYearInt = Integer.parseInt(curYear);
	               String answYear =  resultSet.getString("birthday");
	               answYear = answYear.substring(6);
	               int answYaerInt = Integer.parseInt(answYear);
	               answYaerInt = curYearInt - answYaerInt;
	               answ += "<td>"+ Integer.toString(answYaerInt) +" </td>";
	        	   answ += "</tr>";
	        	  // System.out.println("Answer" + answ);
	        }
	     }
	        answ += "</div>";  
	        
	        
	       /* answ += "<div class =\"birtdayNextDayTitle\">";	
	        answ += "<font size=\"5\" color=\"black\" face=\"Arial\">������ ���� �������� </font>";
	        answ += "</div>";*/
   	}catch (SQLException e) {

       } finally {
           closeDB();
       }  
	   
      	System.out.println("Answer" + answ);
       return answ;
   }
   
   
   public void findNotCom(String date) {  //����������� ������� �� ������
   	String query = "select * from workerfio inner join department ON workerfio.department = department.id"
      			+ " where workerIdCard != \"null\" "
      			+ "and personellNumber NOT IN "
      			+ "(select workerId from worker where eventTime >="+ "\"" + date+ "\""+" group by workerId)";
      	try{
   	        connection = DriverManager.getConnection(url, user, password);
   	        statement = connection.createStatement();
   	        resultSet = statement.executeQuery(query);
   	        while (resultSet.next()) {
   	        	
   	        	String insrtQuery = "INSERT INTO worker VALUES(" + 
   	    				"\"" +  resultSet.getString("workerIdCard") + "\""  + "," +
   	    				"\"" + date.toString()+" 23:59:00" + "\"" +  "," +
   	    				"\"" + resultSet.getString("personellNumber")   + "\""  + "," +
   	    				"\"" + 300 + "\"" + ");" ;
   	    	   
   	        	sendQuery(insrtQuery);
   	        	
   	        	 //resultSet.getString("personellNumber") ;
   	        	// resultSet.getString("workerIdCard");
   	        	   
   	  
   	        }
      	}catch (SQLException e) {

          } finally {
              closeDB();
          }  
   	   
   }
   
   public void getHoursWorked(String typeTime, String department, String fioWorker,String month) {
	   String hoursWorked = new String();
	   
	   int mounthInt = 1;
	   
	   int dayMonth = 31;
	   
	 /*  Date now = new Date();     // Gets the current date and time
	   int year = now.getYear();
	   */
	   
	   Calendar nowYear = Calendar.getInstance();
	    // 
	   int year = nowYear.get(Calendar.YEAR);
	    System.out.println("Current Year is : " + nowYear.get(Calendar.YEAR));
	   
	String dateStart = Integer.toString(year) +"-"+ month +"-"+ "1";
	String dateFinish = Integer.toString(year) +"-"+ month +"-"+ "31";
	//   if (department.equals("") == false) {
		   
		   /*String query = "SELECT * FROM (worker INNER JOIN workerfio ON worker.workerId = workerfio.personellNumber)"
	    			+ "INNER JOIN department ON workerfio.department = department.id";*/
		   
		   String query = "SELECT * FROM workerfio INNER JOIN department ON workerfio.department = department.id ";
		   		   
		   if (department.equals("") == false) {
	    		query += "where title =" + "\"" + department.replaceAll("\n","") + "\"";
	    	}
	    			/*+ "INNER JOIN department ON workerfio.department = department.id";*/
		   	try{
			        connection = DriverManager.getConnection(url, user, password);
			        statement = connection.createStatement();
			        resultSet = statement.executeQuery(query);
			        int kol = 0;
			        while (resultSet.next()) {
			        	
				     	   String queryTime = "SELECT * FROM worker "
				     		+ " WHERE eventTime >=" + "\"" + dateStart.replaceAll("\n","")+ "\"";	
				    
				      		//System.out.println(dateFin);
				     	  queryTime += "AND eventTime <=" + "\"" + dateFinish.replaceAll("\n","") + "\"";
				     	  queryTime += "AND personellNumber <=" + "\"" + resultSet.getString("personellNumber") + "\"";
				   		   
						   	try{
							        connection = DriverManager.getConnection(url, user, password);
							        statement = connection.createStatement();
							        resultSet = statement.executeQuery(query);
							        //int kol = 0;
							        while (resultSet.next()) {
							        	
							        	
							        }
						   	}catch (SQLException e) {
	
						       } finally {
						           closeDB();
						       } 
						   
				        	
				        	/*System.out.println( resultSet.getString("surname")+" "+
				        	resultSet.getString("name")+ " " +resultSet.getString("patronymic") +" " +
				         	resultSet.getString("title") + " "+
		        	    	resultSet.getString("personellNumber") );*/
				        }
			        	
		   	}catch (SQLException e) {

		       } finally {
		           closeDB();
		       } 
		   
	   
	   
		   if (typeTime == "month") {
			   
		   }
	   
	//   }
	   
	   
	  // return hoursWorked;
   }
   
   public void autoExit (String date) {
	   String id1 = new String();
	   String stat1 = new String();
	   String id2 = new String();
	   String stat2 = new String();
	   System.out.println("I'm here");
	   String query = "SELECT * FROM (worker INNER JOIN workerfio ON worker.workerId = workerfio.personellNumber)"
   			+ "INNER JOIN department ON workerfio.department = department.id"
   			+ " WHERE eventTime >=" + "\"" + date+ "\""+" ORDER BY workerId, eventTime";
	  /* String query = "SELECT * FROM  worker WHERE eventTime >" 
     			+ "\"" + date + "\""+" ORDER BY workerId";*/
	   System.out.println(date + " 00:00:00");
	   System.out.println(query);
     	try{
  	        connection = DriverManager.getConnection(url, user, password);
  	        statement = connection.createStatement();
  	        resultSet = statement.executeQuery(query);
  	        
  	        resultSet.next();
  	        
  	        id1 = resultSet.getString("workerID");
        	stat1 = resultSet.getString("statuscom");
        	
        	
  	        while (resultSet.next()) {
 	        	id2 = resultSet.getString("workerID");
  	        	stat2 = resultSet.getString("statuscom");
  	        	System.out.println("first rec "+ "id1: " +  id1 + " stat1:" + stat1);
  	        	System.out.println("second rec " +"id2: " +  id2+ " stat2:"  + stat2);
  	        	//System.out.println(date);
  	        	//System.out.println(resultSet.getString("workerID"));
  	        	

 
  	        		if (id1.equals(id2)) {
  	        			System.out.println("first if");
  	        			id1 = id2;
  	        			stat1 = stat2;
  	        		}
  	        		else if (Integer.parseInt(stat1) < 200){
  	        			resultSet.previous();
  	        			System.out.println("Else if write table");
  	        			String queryOut = "INSERT INTO worker VALUES(" + 
  	     	    				"\"" +  resultSet.getString("workerIdCard") + "\""  + "," +
  	     	    				"\"" + date.toString( ) + " 17:30:00" + "\"" +  "," +
  	     	    				"\"" + resultSet.getString("personellNumber")   + "\""  + "," +
  	     	    				"\"" + 201 + "\"" + ");" ;
  	        			System.out.println(queryOut);
  	        			sendQuery(queryOut);
  	        			resultSet.next();
  	        			id1 = id2;
  	        			stat1 = stat2;
  	        			
  	        		}
  	        		else {
  	        			System.out.println("Last else");
  	        			id1 = id2;
  	        			stat1 = stat2;
  	        		}
  	        		
  	  	        	if(resultSet.isLast()) {
  	  	        		if (Integer.parseInt(stat2) < 200){
  	  	        			System.out.println("Else if write table");
  	  	        			String queryOut = "INSERT INTO worker VALUES(" + 
  	  	     	    				"\"" +  resultSet.getString("workerIdCard") + "\""  + "," +
  	  	     	    				"\"" + date.toString( ) + " 17:30:00" + "\"" +  "," +
  	  	     	    				"\"" + resultSet.getString("personellNumber")   + "\""  + "," +
  	  	     	    				"\"" + 201 + "\"" + ");" ;
  	  	        			System.out.println(queryOut);
  	  	        			sendQuery(queryOut);
  	  	        		}
  	  	        	}
  	        	
  	  
  	        }
  	        
     	 }catch (SQLException e) {

         } finally {
             closeDB();
         }  
  	   
   }
   
 
   
}
