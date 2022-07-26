package com.stackroute.datamunger;

/*There are total 5 DataMungertest files:
 * 
 * 1)DataMungerTestTask1.java file is for testing following 3 methods
 * a)getSplitStrings()  b) getFileName()  c) getBaseQuery()
 * 
 * Once you implement the above 3 methods,run DataMungerTestTask1.java
 * 
 * 2)DataMungerTestTask2.java file is for testing following 3 methods
 * a)getFields() b) getConditionsPartQuery() c) getConditions()
 * 
 * Once you implement the above 3 methods,run DataMungerTestTask2.java
 * 
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getLogicalOperators() b) getOrderByFields()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 * 
 * 4)DataMungerTestTask4.java file is for testing following 2 methods
 * a)getGroupByFields()  b) getAggregateFunctions()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask4.java
 * 
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

import java.util.ArrayList;
import java.util.Locale;

public class DataMunger {

	/*This method will split the query string based on space into an array of words and display it on console*/

	public String[] getSplitStrings(String queryString) {

		String[] splitQuery = queryString.split(" ");
		String[] finalQuery = new String[splitQuery.length];

		ArrayList<String> splitList = new ArrayList<String>();

		for(int i = 0; i < splitQuery.length; i++) {
			finalQuery[i] = splitQuery[i].toLowerCase().trim();
		}

		return finalQuery;
	}

	/*Extract the name of the file from the query. File name can be found after a space after "from" clause. Note: ----- CSV file can contain a field that
	 * contains from as a part of the column name. For eg: from_date,from_hrs etc. Please consider this while extracting the file name in this method.*/

	public String getFileName(String queryString) {

		String fileName;
		String [] splitQuery = getSplitStrings(queryString);

		for(int i =0; i < splitQuery.length; i++){
			if(splitQuery[i].toLowerCase().equals("from")) {
				fileName = splitQuery[i + 1];
				return fileName.trim();
			}
		}

		return null;
	}

	/*
	 * This method is used to extract the baseQuery from the query string. BaseQuery
	 * contains from the beginning of the query till the where clause
	 * 
	 * Note: ------- 1. The query might not contain where clause but contain order
	 * by or group by clause 2. The query might not contain where, order by or group
	 * by clause 3. The query might not contain where, but can contain both group by
	 * and order by clause
	 */
	
	public String getBaseQuery(String queryString) {

		String [] splitQuery = getSplitStrings(queryString);
		String fileName = getFileName(queryString);

		int fileNameIndex;

		String finalBaseQuery = "";

		for(int i = 0; i < splitQuery.length; i++) {
			if (splitQuery[i].toLowerCase().equals(fileName)) {
				fileNameIndex = i;
				for (int x = 0; x <= fileNameIndex; x++) {
					finalBaseQuery += splitQuery[x] + " ";
				}
				if(finalBaseQuery.length() > 0){return finalBaseQuery.trim();}
			}
		}

		return null;
	}

	/*
	 * This method will extract the fields to be selected from the query string. The
	 * query string can have multiple fields separated by comma. The extracted
	 * fields will be stored in a String array which is to be printed in console as
	 * well as to be returned by the method
	 * 
	 * Note: 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The field
	 * name can contain '*'
	 * 
	 */
	
	public String[] getFields(String queryString) {

		String[] splitQuery = getSplitStrings(queryString);
		String[] fs = splitQuery[1].trim().split(",");

		//currently saying the length is as long second index length (23 char)
//		String[] fields = new String[splitQuery[1].length()];

//		String[] finalFields;
//		String x;

// 		for(int i =0; i < splitQuery.length; i++){
//			if(i == 1){
//				fields[0] = splitQuery[i];
//				x = fields[0];
//
////				System.out.println(splitQuery.length);
//			}
//		}
//		 for(String ee: fs) {
//			 System.out.println(ee);
//		 }

		return queryString.length() > 0 ? fs : null;
	}

	/*
	 * This method is used to extract the conditions part from the query string. The
	 * conditions part contains starting from where keyword till the next keyword,
	 * which is either group by or order by clause. In case of absence of both group
	 * by and order by clause, it will contain till the end of the query string.
	 * Note:  1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
	 * might not contain where clause at all.
	 */
	
	public String getConditionsPartQuery(String queryString) {

		//split the queryString into an array based on the spaces
		String [] splitQuery = getSplitStrings(queryString);

		//store the result of calling getFileName in var to the compare in traversal to find index
		String fileName = getFileName(queryString);

		//initialize var to store index of filename, to then take what is after that index (the conditions)
		int fileNameIndex;

		//initialize var to later store and return the conditional partquery as a string
		String finalConditionQuery = "";

		//loop through the split query
		for(int i = 0; i < splitQuery.length; i++) {
			//if current split query item is = to the stored filename
			if (splitQuery[i].toLowerCase().equals(fileName)) {
				//set the filenameindex to then later use
				fileNameIndex = i;
				//loop through split query again
				for (int x = 0; x < splitQuery.length; x++) {
					//if current position is beyond fileNAMEindex, and does not = where keyword
					if (x > fileNameIndex && !(splitQuery[x].equals("where"))) {
						//so long as the current word is not order or group
						if(!(splitQuery[x].equals("order")) && !(splitQuery[x].equals("group"))){
							//add that word to the finalConditionQuery output with a space
							finalConditionQuery += splitQuery[x] + " ";
						} else if(!(splitQuery[x].equals("order")) || !(splitQuery[x].equals("group"))){
							//otherwise return output before group and order by clauses
							System.out.println("Final Conditions Query: "+finalConditionQuery);
							System.out.println("Length: " + finalConditionQuery.length());
							System.out.println(queryString);
							return finalConditionQuery.trim();
							}
					}
				}
			}
			//if the output is not void essentially, then return finalConditionQuery and trim the end space lol
			if(finalConditionQuery.length() > 0){
				return finalConditionQuery.trim();}
		}
		return null;
	}

	/*
	 * This method will extract condition(s) from the query string. The query can
	 * contain one or multiple conditions. In case of multiple conditions, the
	 * conditions will be separated by AND/OR keywords. for eg: Input: select
	 * city,winner,player_match from ipl.csv where season > 2014 and city
	 * ='Bangalore'
	 * 
	 * This method will return a string array ["season > 2014","city ='bangalore'"]
	 * and print the array
	 * 
	 * Note: ----- 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
	 * might not contain where clause at all.
	 */

	public String[] getConditions(String queryString) {

		String conditions = getConditionsPartQuery(queryString);

 		String[] conditionsArr;
		String[] newCondArr;

		String x = " and ";
		String y = " or ";

		if(conditions != null){
		if(!conditions.contains(x) && !conditions.contains(y)){
			conditionsArr = new String[1];
			conditionsArr[0] = conditions.trim();
			return conditionsArr;
		}
		else if(conditions.contains(x) && !conditions.contains(y)){
			conditionsArr = conditions.split(x);
			newCondArr = new String[conditionsArr.length];
			for(int i = 0; i < conditionsArr.length; i++) {
				newCondArr[i] = conditionsArr[i].trim();
			}
			return newCondArr;
		}
		else if(conditions.contains(y) && !conditions.contains(x)){
			conditionsArr = conditions.split(y);
			newCondArr = new String[conditionsArr.length];
			for(int i = 0; i < conditionsArr.length; i++) {
				newCondArr[i] = conditionsArr[i].trim();
			}
			return newCondArr;
		} else if(conditions.contains(x) && conditions.contains(y)){
			String newCond = conditions.replaceAll(x, y);
			conditionsArr = newCond.split(y);
			newCondArr = new String[conditionsArr.length];
//			System.out.println("has both!");
			for(int i = 0; i < conditionsArr.length; i++) {
				newCondArr[i] = conditionsArr[i].trim();
			}
			return newCondArr;
		}
		}

		System.out.println("**get conditions method: " + conditions);

		return null;

	}

	/*
	 * This method will extract logical operators(AND/OR) from the query string. The
	 * extracted logical operators will be stored in a String array which will be
	 * returned by the method and the same will be printed Note:  1. AND/OR
	 * keyword will exist in the query only if where conditions exists and it
	 * contains multiple conditions. 2. AND/OR can exist as a substring in the
	 * conditions as well. For eg: name='Alexander',color='Red' etc. Please consider
	 * these as well when extracting the logical operators.
	 * 
	 */

	public String[] getLogicalOperators(String queryString) {

		//extract only the condition query
		String conditions = getConditionsPartQuery((queryString));

		//split the condition query into an array to then traverse
		String[] splitArr = getSplitStrings(conditions);

		//initialize operators to store any logical operators if they occur
		ArrayList<String> operators = new ArrayList<String>();

		//initialize opCount to count how many (if any) logical operators, to then use when creating opArr
		int opCount = 0;

		String and = "and";
		String or = "or";

		//for loop to count how many occurrences of log ops, if its an operator add to list
		for(int i = 0; i < splitArr.length; i++){
			if(splitArr[i].equals(and) || splitArr[i].equals(or)){
				operators.add(splitArr[i]);
				opCount += 1;
			}
		}

		//if logical operators exist,
		if(opCount > 0) {
			//then create an opArr based on op array list size
			String[] opArr = new String[operators.size()];
			//set oparr equal to converting list to arr
			opArr = operators.toArray(opArr);
			//return opArr of logical operators
			return opArr;
		}

		return null;

	}

	/*
	 * This method extracts the order by fields from the query string. Note: 
	 * 1. The query string can contain more than one order by fields. 2. The query
	 * string might not contain order by clause at all. 3. The field names,condition
	 * values might contain "order" as a substring. For eg:order_number,job_order
	 * Consider this while extracting the order by fields
	 */

	public String[] getOrderByFields(String queryString) {

		//split query by order by
		String[] splitOrderFieldsArr = queryString.split("order by");
		String[] orderFields;

		//if an orderby clause is found, splitOrderFieldsArr will have 2 indexes
		if(splitOrderFieldsArr.length > 1){
			//assign fields that follow order by (the second index) to string
			String fields = splitOrderFieldsArr[1].trim();
			//split the fields by space (for if there are multi order statements)
			String[] finalOrderFields = fields.split(" ");
			return finalOrderFields;
		}

		return null;
	}

	/*
	 * This method extracts the group by fields from the query string. Note:
	 * 1. The query string can contain more than one group by fields. 2. The query
	 * string might not contain group by clause at all. 3. The field names,condition
	 * values might contain "group" as a substring. For eg: newsgroup_name
	 * 
	 * Consider this while extracting the group by fields
	 */

	public String[] getGroupByFields(String queryString) {

		//split query by order by
		String[] splitGroupFieldsArr = queryString.split("group by");
		String[] groupFields;

		//if a groupby clause is found, splitGroupFieldsArr will have 2 indexes
		if(splitGroupFieldsArr.length > 1){
			//assign fields that follow group by (the second index) to string
			String fields = splitGroupFieldsArr[1].trim();
			//split the fields by space (for if there are multi group statements)
			String[] finalGroupFields = fields.split(" ");
			return finalGroupFields;
		}

		return null;
	}

	/*
	 * This method extracts the aggregate functions from the query string. Note:
	 *  1. aggregate functions will start with "sum"/"count"/"min"/"max"/"avg"
	 * followed by "(" 2. The field names might
	 * contain"sum"/"count"/"min"/"max"/"avg" as a substring. For eg:
	 * account_number,consumed_qty,nominee_name
	 * 
	 * Consider this while extracting the aggregate functions
	 */

	public String[] getAggregateFunctions(String queryString) {


		//split query into array
		String[] splitQueryString = queryString.split(" ");

		//set agFun = to the first index where aggregate functions reside
		String agFun = splitQueryString[1];

		//split agfun string by commas into new array
		String[] agFunArr = agFun.split(",");
		String[] finalOutput;

		//initialize count of ag funcs
		int numOfFuncs = 0;

		//set aggregate variables
		String s = "sum(";
		String c = "count(";
		String mi = "min(";
		String ma = "max(";
		String a = "avg(";

		//if there indexes
		if(agFunArr.length > 0) {
			for (String word : agFunArr) {
				//and if there are agg functions to begin with
				if(word.contains(s) || word.contains(c) || word.contains(a) || word.contains(mi) || word.contains(ma)){
					//concat a string of only those functions
					String funOnlyStr = "";
			for (String item : agFunArr) {
				if (item.contains(s) || item.contains(c) || item.contains(mi) || item.contains(ma) || item.contains(a)) {
					//count the numbder of functs to create final out put arr
					numOfFuncs += 1;
					//concat sentence here
					funOnlyStr = funOnlyStr + item + " ";
				}
			}

			//create finalOutput array based on numOfFuncs
			finalOutput = funOnlyStr.split(" ");

			return finalOutput;
			}
		}
		}

		return null;

	}

	//----------------------------------------------------------------------------------------------------------------------

}