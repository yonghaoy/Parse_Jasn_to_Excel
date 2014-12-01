/*
 * Name: txt_to_excel.java
 * Description: This class convert txt game_review to excel. Then we can use lightside to process the data
 *   and store the result in two excel, positive and negative
 * Author: Yonghao Yu
 * Email: yyhao1@gmail.com
 */
package processTrainText;

import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import jxl.*; 
import jxl.write.*; 
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;
public class strip_review {

	//map store all words shows in positive review. Key is the word, value is appearing times
	public static Map<String,Integer> positive_word = new HashMap<String,Integer>();
	
	//map store all words shows in negative review. Key is the word, value is appearing times
	public static Map<String,Integer> negative_word = new HashMap<String,Integer>();
	
	//maximium process lines in train file
	static int MAX_LINE = 50000;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String csvFile = "test.txt";
	//	MAX_LINE = Integer.parseInt(args[1]);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(csvFile));
			WritableWorkbook workbook = Workbook.createWorkbook(new File("game_review.xls"));
			WritableSheet sheet = workbook.createSheet("First Sheet", 0);
			int cur_line = 0;
			while (cur_line < MAX_LINE) {
				//read first 5 lines for each review
				for(int j = 0; j < 5; j++){
					br.readLine();
				}
				//process helpfullness
				String str = br.readLine();
				//System.out.println(str);
				String helpfullness = str.substring(20);
				String[] str_arr = helpfullness.split("/");
				Label label_1 = new Label(0, cur_line, str_arr[0]); 
				Label label_2 = new Label(1, cur_line, str_arr[1]); 
				sheet.addCell(label_1); 
				sheet.addCell(label_2); 
				
				//Number number = new Number(1, cur_line, entry.getValue()); 
				
				//process score
				String str_score = br.readLine();
				str_score = str_score.substring(14);
				Label label_3 = new Label(2, cur_line, str_score); 
				sheet.addCell(label_3); 

				//process review
				br.readLine();
				br.readLine();
				String review_text = br.readLine();
				review_text = review_text.substring(12);
				Label label_4 = new Label(3, cur_line, review_text); 
				sheet.addCell(label_4); 
				cur_line++;
				br.readLine();
					
				// read helpfullness
			}
			workbook.write(); 
			workbook.close();
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 
		System.out.println("Done");
	}

}
