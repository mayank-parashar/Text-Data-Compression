package dictionary;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Dictionary_creator {
	private static String dictionary="Dictionary.txt";//Dictionary.txt save contains words with their frequency ( raw dictionary )
	private static String pdf="Input.txt";//String save name of file from which we read data to add new word to dictionary
	private static String intermediate="IntermediateDictionary.txt";//intermediateDictionary.txt contains only words of final dictionary, word add to intermediate dictionary before adding to final dictionary
	private static String final_dictionary="FinalDictionary.txt";//this dictionary is used in compression and decompression
	
	public static void main(String args[]) throws FileNotFoundException, IOException
	{
		//Read_data_from_pdf();//Read data from input.txt file that contain data which help to enrich our dictionary
		String str=(new Scanner(System.in)).next();//take input from console to search any word in Dictionary.txt
		search(str);//search any string in dictionary and its freq
		//dictionary_tointermediate();//write data from raw dictionary to intermediate dictionary ( on the basis of any threshold frequency of words) 
		//intermediate_tofinal();// write data from intermediate dictionary to final_dictionary
	}
	//method is used to add raw dictionary to intermediate dictionary
	static void dictionary_tointermediate() throws IOException
	{
		HashMap<String,Integer> map=new HashMap<String,Integer>();
		add_dictionary_toMap(map);//Load raw dictionary to map
		Map_tointermediate(map);//write map to intermediate dictionary
	}
	//write intermediate dictionary data to final dictionary
	static void intermediate_tofinal() throws IOException
	{
		HashSet<String> set=new HashSet<String>();
		add_intermediate_toSet(set);//Load intermediate dic. data to set
		int count=0;
		int bit=(Integer.toBinaryString(set.size())).length();//contains maximum no. of bit need 
		HashMap<String,String> map=new HashMap<String,String>();
		for(String s:set)
		{
			String id=inttoBinary(count,bit);
			map.put(s,id);
			count++;
		}
		/* here map contains words as key and a unique a id(no.) in binary form for each word   */
		write_finaldictionary(map);//write map to final dictionary
	}
	
	//method take integer n as input and return a binary string of that no. in bit(int) bits//ex-if n=1 bit=7 return 0000001 
	static String inttoBinary(int n,int bit)
    {
   	 String str=Integer.toBinaryString(n);
   	 int len=str.length();
   	 String temp="";
   	 for(int i=len;i<bit;i++)
   	 {
   		 temp=temp+"0";
   	 }
   	 str=temp+str;
   	 return str;
    }
	
	//add intermediate dictionary to set
	static void add_intermediate_toSet(HashSet<String> set) throws IOException
	{
		String text = new String(Files.readAllBytes(Paths.get(intermediate)), StandardCharsets.UTF_8);
		String[] ptr=text.split(" ");// split dictionary on the basis of space ( returning array of string)
		for(String s:ptr)
		{
			set.add(s);
		}
	}
	
	//write map data to intermediate dictionary only keys(string)
	static void Map_tointermediate(HashMap<String,Integer> map)
	{

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(intermediate))) {
			StringBuilder content =new StringBuilder();
			Set<String> set=map.keySet();
			for(String s:set)//converting Map data in string(content)
			{
				if(map.get(s)>=4)//word is add to intermediate dictionary only if it has freq greater than a limit( now it is 4)
				content.append(s+" ");
			}
			bw.write(content.toString());//writing whole string(content) into dictionary
		} catch (IOException e) {

			e.printStackTrace();

		}
	}
	
	//Method search string in dictionary
	static void search(String str) throws IOException
	{
		HashMap<String,Integer> map=new HashMap<String,Integer>();
		add_dictionary_toMap(map);//Load dictionary to Map for searching
		search_inMap(map,str);
	}
	//method used to read input from input.txt and find new words from it than write them into Dictionary.txt(raw dictionary) and increase freq of already existing words
	static void Read_data_from_pdf() throws FileNotFoundException, IOException
	{
		HashMap<String,Integer> map=new HashMap<String,Integer>();//Map use to find the frequency of words so that no word add to Dictionary twice and freq of words also save with them
		add_dictionary_toMap(map);//add already existing words in Dictionary.txt to map 
		String text = new String(Files.readAllBytes(Paths.get(pdf)), StandardCharsets.UTF_8);//read whole file at once and save it in String
		Pattern p = Pattern.compile("[a-zA-Z]+");//regex to match only word(small and capital)
		Matcher m=p.matcher(text);//file which is match against regex is pass in constructor as parameter
		long count=0;
		while (m.find()) {count++;
			String str=m.group();
			if(map.containsKey(str))//if map already contains that word
			{
				int value=map.get(str);
				map.put(str,value+1);// increase frequency by one 
			}
			else // if word come first time
			{
				map.put(str,1);// add to map with frequency as 1
			}
		}
		System.out.println("No. of words in input is "+count);
		FileWriter write=new FileWriter(dictionary);//object to write data, String contains Dictionary file name pass as parameter
		write_dictionary(map);// method to write Map data in Dictionary
		System.out.println(map.size());
	}
	//Method search any string in Map
	static void search_inMap(HashMap<String,Integer> map,String str)
	{
		if(map.containsKey(str))
		{
			System.out.println("found  "+map.get(str));
		}
		else
		{
			System.out.println("not found ");
		}
	}
	
	//write map to final dictionary 
	static void write_finaldictionary(HashMap<String,String> map) throws IOException
	{
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(final_dictionary))) {
			StringBuilder content =new StringBuilder();
			Set<String> set=map.keySet();
			for(String s:set)//converting Map data in string(content)
			{
				String value=map.get(s);
				content.append(s+" ");
				content.append(value+" ");
			}
			bw.write(content.toString());//writing whole string(content) into dictionary
			
			// no need to close it.
			//bw.close();

			

		} catch (IOException e) {

			e.printStackTrace();

		}
		
	}
	
	//Method write Map data in Dictionary
	static void write_dictionary(HashMap<String,Integer> map) throws IOException
	{
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(dictionary))) {
			
			StringBuilder content =new StringBuilder();
			Set<String> set=map.keySet();
			for(String s:set)//converting Map data in string(content)
			{
				int value=map.get(s);
				content.append(s+" ");
				content.append(value+" ");
			}
			bw.write(content.toString());//writing whole string(content) into dictionary
			
			// no need to close it.
			//bw.close();

			

		} catch (IOException e) {

			e.printStackTrace();

		}
		
	}
	//Method add already existing dictionary to Map
	static void add_dictionary_toMap(HashMap<String,Integer> map) throws IOException
	{
		String text = new String(Files.readAllBytes(Paths.get(dictionary)), StandardCharsets.UTF_8);
		String[] ptr=text.split(" ");// split dictionary on the basis of space ( returning array of string)
		int status=0;//variable is used to find when string is word and when string contains integer(freq of that word) (if status is even then word else integer)
		String key="";
		for(String s:ptr)
		{
			if(status%2==0)//if string is word then store it as key
			{
				key=s.toLowerCase();
			}
			else// if string is integer then store it as value and save key,value in Map
			{
				int value=Integer.parseInt(s);
				if(map.containsKey(key))
				{
					value=value+map.get(key);
				}
				map.put(key,value);
			}
			status++;
		}
		
	}
	

	
	//Method to print Map
	static void print(HashMap<String,Integer> map)
	{
		Set<String> set=map.keySet();
		int count=0;
		for(String s:set)
		{
			
				count++;
				System.out.println(s+" "+map.get(s));
			
		}
		System.out.println(count);
	}
	
	 
	 
}
