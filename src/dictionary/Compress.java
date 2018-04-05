package dictionary;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Compress {
private static String input="uncompressed.txt";//file which need to be compressed
private static String output="compressed.txt";//file which contains compressed data ( in binary form )
private static String Dictionary="FinalDictionary.txt";
private int max_dictionary_size;
private String miss_string=new String();
private HashMap<String,String> miss_map;
private  HashMap<String,String> map;
private String miss_dictionary="miss_data.txt";
Compress() throws IOException
{
	miss_map=new HashMap<String,String>();
	map=new HashMap<String,String>();
	add_dictionary_toMap(map);
	max_dictionary_size=map.size();
}
public static void main(String[] args) throws IOException
{
	Compress c=new Compress();
	c.compress();
}
	public void compress() throws IOException
	{
		String text = new String(Files.readAllBytes(Paths.get(input)), StandardCharsets.UTF_8);
		Pattern p = Pattern.compile("[^ \t\n\r\f\\s]+");//regex to match to match words separated by tab,space 
		Matcher m=p.matcher(text);//file which is match against regex is pass in constructor as parameter
		StringBuilder output=new StringBuilder();
		int count=0;
		while (m.find()) {
		String key=m.group().toLowerCase();
		String value="";
		count=count+key.length();
		if(find_in_dictionary(key))
		{
			 value=get_value_dictionary(key);
		}
		else
		{
			value=miss(key);
		}
		output.append(value);
	}
		//System.out.println(output);
		System.out.println(output.length());
		System.out.println(miss_map.size());
		System.out.println(count);
		write_output(output);
		write_missData();
	}

	private void write_missData()
	{
			StringBuilder content =new StringBuilder();
			Set<String> set=miss_map.keySet();
			for(String s:set)//converting Map data in string(content)
			{
				content.append(s+" ");
			}
			Huffman_coding h=new Huffman_coding();
			h.buildHuffmanTree(miss_string);
	}
	
	private void write_output(StringBuilder compressed_data) throws IOException
	{
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(output)))
		{
			bw.write(compressed_data.toString());
		}
		catch (IOException e) {

			e.printStackTrace();

		}
	}
	//return binary string for miss word
	private String miss(String str)
	{
		if(miss_map.containsKey(str))
		{
			return miss_map.get(str);
		}
		else
		{
			int bit=(Integer.toBinaryString(max_dictionary_size)).length();
			String id=inttoBinary(max_dictionary_size+1,bit);
			max_dictionary_size++;
			miss_map.put(str,id);
			miss_string=miss_string+str+" ";
		}
		return miss_map.get(str);
	}
	
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
	private boolean find_in_dictionary(String str)
	{
		if(map.containsKey(str))
			return true;
		return false;
	}
	
	private String get_value_dictionary(String str)
	{
		return map.get(str);
	}
	
static void add_dictionary_toMap(HashMap<String,String> map) throws IOException
{
	String text = new String(Files.readAllBytes(Paths.get(Dictionary)), StandardCharsets.UTF_8);
	String[] ptr=text.split(" ");// split dictionary on the basis of space ( returning array of string)
	int status=0;//variable is used to find when string is word and when string contains integer(freq of that word) (if status is even then word else integer)
	String key="";
	for(String s:ptr)
	{
		if(status%2==0)//if string is word then store it as key
		{
			key=s;
		}
		else// if string is integer then store it as value and save key,value in Map
		{
			String value=s;
			map.put(key,value);
		}
		status++;
	}
	
}

}
