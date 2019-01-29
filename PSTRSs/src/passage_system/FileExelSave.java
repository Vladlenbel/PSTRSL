package passage_system;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class FileExelSave {

public  void mainSave() throws ParseException {
	System.out.println("mainSave start");
	// TODO Auto-generated method stub
	/*BufferedReader br = new BufferedReader(
	new InputStreamReader(System.in));
	Scanner in = new Scanner(System.in);
	System.out.println("Enter");
	String str;	
	
	do {
		str = in.toString();
				System.out.println(str);
	}while(!str.equalsIgnoreCase("stop"));*/
	
	
	
	
	        
	        // �������� ������ excel ����� � ������
	        HSSFWorkbook workbook = new HSSFWorkbook();
	        // �������� ����� � ��������� "������ ����"
	        HSSFSheet sheet = workbook.createSheet("������ ����");
	 
	        // ��������� ������ ������-�� �������
	        List<DataModel> dataList = fillData();
	 
	        // ������� ��� �����
	        int rowNum = 0;
	 
	        // ������� ������� � �������� (��� ����� ������ ������� � ����� Excel �����)
	        Row row = sheet.createRow(rowNum);
	        row.createCell(0).setCellValue("���");
	        row.createCell(1).setCellValue("�������");
	        row.createCell(2).setCellValue("�����");
	        row.createCell(3).setCellValue("��������");
	 
	        // ��������� ���� �������
	        for (DataModel dataModel : dataList) {
	            createSheetHeader(sheet, ++rowNum, dataModel);
	        }
 
	        // ���������� ��������� � ������ Excel �������� � ����
	        try (FileOutputStream out = new FileOutputStream(new File("/var/lib/tomcat7/common/File.xls"))) {
	            workbook.write(out);
	        } catch (IOException e) {
	        	System.out.println(e.toString());
	            e.printStackTrace();
	        }
	      /*  try {
                File file = new File("/home/vladislavb/Files.xls");
                BufferedWriter output = new BufferedWriter(new FileWriter(file));
                //workbook.write(output);
                //output.write(text);
                output.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }*/
	        System.out.println("Excel ���� ������� ������!");
	    }
	 
	    // ���������� ������ (rowNum) ������������� ����� (sheet)
	    // �������  �� dataModel ���������� � ������ Excel �����
	    private static void createSheetHeader(HSSFSheet sheet, int rowNum, DataModel dataModel) {
	        Row row = sheet.createRow(rowNum);
	 
	        row.createCell(0).setCellValue(dataModel.getName());
	        row.createCell(1).setCellValue(dataModel.getSurname());
	        row.createCell(2).setCellValue(dataModel.getCity());
	        row.createCell(3).setCellValue(dataModel.getSalary());
	    }
	 
	    // ��������� ������ ���������� �������
	    // � �������� ����������� ������ ����� �� �� ��� ���������
	    private static List<DataModel> fillData() {
	        List<DataModel> dataModels = new ArrayList<>();
	        dataModels.add(new DataModel("Howard", "Wolowitz", "Massachusetts", 90000.0));
	        dataModels.add(new DataModel("Leonard", "Hofstadter", "Massachusetts", 95000.0));
	        dataModels.add(new DataModel("Sheldon", "Cooper", "Massachusetts", 120000.0));
	 
	        return dataModels;
	    }
	}

