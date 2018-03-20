package dictionary;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Dictionary_creator {
	public static void main(String args[]) throws FileNotFoundException, IOException
	{
		Read_data_from_pdf();
	}
	
	static void Read_data_from_pdf() throws FileNotFoundException, IOException
	{
		String pdf="Input.txt";//String save name of file from which we read data and add to Dictionary
		String dictionary="Dictionary.txt";//String save name of Dictionary file
		HashMap<String,Integer> map=new HashMap<String,Integer>();//Map use to find the frequency of words so that no word add to Dictionary twice and freq of words also save with them
		add_dictionary_toMap(dictionary,map);
		String text = new String(Files.readAllBytes(Paths.get("Input.txt")), StandardCharsets.UTF_8);//read whole file at once and save it in String
		Pattern p = Pattern.compile("[a-zA-Z]+");//regex to match only word(small and capital)
		Matcher m=p.matcher(text);//file which is match against regex is pass in constructor as parameter
		long count=0;
		while (m.find()) {
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
		
		FileWriter write=new FileWriter(dictionary);//object to write data, String contains Dictionary file name pass as parameter
		write_dictionary(map,dictionary);// method to write Map data in Dictionary
		System.out.println(map.size());
		//print(map);
	}
	
	//Method write Map data in Dictionary
	static void write_dictionary(HashMap<String,Integer> map,String dictionary) throws IOException
	{
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(dictionary))) {

			StringBuilder content =new StringBuilder();
			Set<String> set=map.keySet();
			for(String s:set)
			{
				int value=map.get(s);
				content.append(s+" ");
				content.append(value+" ");
			}
			bw.write(content.toString());
			
			// no need to close it.
			//bw.close();

			

		} catch (IOException e) {

			e.printStackTrace();

		}
		
	}
	
	static void add_dictionary_toMap(String dictionary,HashMap<String,Integer> map) throws IOException
	{
		String text = new String(Files.readAllBytes(Paths.get(dictionary)), StandardCharsets.UTF_8);
		String[] ptr=text.split(" ");
		int status=0;
		String key="";
		for(String s:ptr)
		{
			if(status%2==0)
			{
				key=s;
			}
			else
			{
				int value=Integer.parseInt(s);
				map.put(key, value);
			}
			status++;
		}
		
	}
	
	//Method to print Map
	static void print(HashMap<String,Integer> map)
	{
		Set<String> set=map.keySet();
		for(String s:set)
		{
			System.out.println(s+" "+map.get(s));
		}
	}
	
	 
	 
}
