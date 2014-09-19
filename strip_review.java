/*
 * Name: strip_review.java
 * Description: This class read train.csv and calculate each word's frequency.
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
	static int MAX_LINE = 2000;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String csvFile = args[0];
		MAX_LINE = Integer.parseInt(args[1]);
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		try {
			int i = 0;
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null&&i < MAX_LINE) {
				String[] review_line = line.split(cvsSplitBy);
				process_review(review_line);
				i++;
				write_file();
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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
	
	/*
	 *  process one line of review, get all the text before "positive" or negative
	 *  @param review_line: String array store one line, split by ','
	 */
	
	public static void process_review(String[] review_line){
		for(int i = 0; i < review_line.length; i++){
			if(review_line[i].equals("positive")){
				get_positive_words(review_line,i);
				return;
			}
			if(review_line[i].equals("negative")){
				get_negative_words(review_line,i);
				return;
			}
		}
	}
	
	/*
	 * process a single positive review, get all the word and put them into positive_words map
	 * @param: String[] review, a single positive review, splited by ','
	 */
    public static void get_positive_words(String[] review, int i){
		for(int j = 0; j < i; j++){
			String[] tem_review = review[j].split(" ");
			for(int k = 0; k < tem_review.length;k++){
				if(tem_review[k].equals("a")||tem_review[k].equals("the")||tem_review[k].equals("it")||
						tem_review[k].equals("that")||tem_review[k].equals("of")||tem_review[k].equals("this")||
						tem_review[k].equals("is")||tem_review[k].equals("are")||tem_review[k].equals("I")||
						tem_review[k].equals("an")||tem_review[k].equals("for")||tem_review[k].equals("movie")){
					continue;
				}
				if(positive_word.containsKey(tem_review[k])){
					positive_word.put(tem_review[k], positive_word.get(tem_review[k])+1);
				}
				else{
					positive_word.put(tem_review[k], 1);
				}
			}
		}
	}
    
	/*
	 * process a single negative review, get all the word and put them into positive_words map
	 * @param: String[] review, a single negative review, splited by ','
	 */
    public static void get_negative_words(String[] review, int i){
		for(int j = 0; j < i; j++){
			String[] tem_review = review[j].split(" ");
			for(int k = 0; k < tem_review.length;k++){
				if(tem_review[k].equals("a")||tem_review[k].equals("the")||tem_review[k].equals("it")||
						tem_review[k].equals("that")||tem_review[k].equals("of")||tem_review[k].equals("this")||
						tem_review[k].equals("is")||tem_review[k].equals("are")||tem_review[k].equals("I")||
						tem_review[k].equals("an")||tem_review[k].equals("for")||tem_review[k].equals("movie")
						||tem_review[k].equals("\"")||tem_review[k].equals(".")){
					continue;
				}
				if(negative_word.containsKey(tem_review[k])){
					negative_word.put(tem_review[k], negative_word.get(tem_review[k])+1);
				}
				else{
					negative_word.put(tem_review[k], 1);
				}
			}
		}
	}
    
    /*
     * This read hashmap's word info and write them to two excels
     * Using jexcelapi http://sourceforge.net/projects/jexcelapi/files/jexcelapi/2.6.12/
     */
    public static void write_file(){
    	try {
			WritableWorkbook workbook_pos = Workbook.createWorkbook(new File("positive_output.xls"));
			WritableWorkbook workbook_neg = Workbook.createWorkbook(new File("negative_output.xls"));
			WritableSheet sheet_pos = workbook_pos.createSheet("First Sheet", 0);
			WritableSheet sheet_neg = workbook_neg.createSheet("First Sheet", 0);
			int cur_line = 0;
			for(Map.Entry<String, Integer> entry : positive_word.entrySet()){
				Label label = new Label(0, cur_line, entry.getKey()); 
				sheet_pos.addCell(label); 
				Number number = new Number(1, cur_line, entry.getValue()); 
				sheet_pos.addCell(number);
				cur_line++;
			}
			workbook_pos.write(); 
			workbook_pos.close();
			cur_line = 0;
			for(Map.Entry<String, Integer> entry : negative_word.entrySet()){
				Label label = new Label(0, cur_line, entry.getKey()); 
				sheet_neg.addCell(label); 
				Number number = new Number(1, cur_line, entry.getValue()); 
				sheet_neg.addCell(number);
				cur_line++;
			}
			workbook_neg.write(); 
			workbook_neg.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
