package dictionary;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Decompression {
private String input="compressed.txt";
private String output="output.txt";
private String dictionary="FinalDictionary.txt";
private String miss_dictionary="miss_data.txt";
private int max_dictionary_size;
private HashMap<String,String> map;//hold final Dictionary data
private HashMap<String,String> miss_map;//hold miss data

public Decompression() throws IOException {
	map=new HashMap<String,String>();
	miss_map=new HashMap<String,String>();
	add_dictionary_toMap(map,dictionary);
	max_dictionary_size=map.size();
	add_missdictionary_toMap(miss_map);
}

public static void main(String args[]) throws IOException
{
	Decompression d=new Decompression();
	d.decompress();
	Effeciency e=new Effeciency( new String(Files.readAllBytes(Paths.get(d.input)), StandardCharsets.UTF_8),  new String(Files.readAllBytes(Paths.get("uncompressed.txt")), StandardCharsets.UTF_8));
	e.compration_ratio();
}

private void decompress() throws IOException
{
	String text = new String(Files.readAllBytes(Paths.get(input)), StandardCharsets.UTF_8);
	String[] code=split(text,14/*bit per word, need to update on update in final dictionary*/);
	StringBuilder output=new StringBuilder();
	for(int i=0;code[i]!=null;i++)
	{
		String value="";
		if(dictionary_contains(code[i]))
		{
			 value=get_dictionary(code[i]);
		}
		else
		{
			value=get_miss_data(code[i]);
		}
		output.append(" "+value);
	}
	System.out.println(output);
	write_output(output);
}
private void write_output(StringBuilder data)
{
	try (BufferedWriter bw = new BufferedWriter(new FileWriter(output)))
	{
		bw.write(data.toString());
	}
	catch (IOException e) {

		e.printStackTrace();

	}
}
private String get_miss_data(String code)
{
	return miss_map.get(code);
}

private boolean dictionary_contains(String code)
{
	if(map.containsKey(code))
		return true;
	return false;
}

private String get_dictionary(String code)
{
	return map.get(code);
}

private String[] split(String content,int bit)
{
	int len=content.length();
	String[] ptr=new String[(len/bit)+1];
	int count=0;
	for(int i=0;i<len;i=i+bit,count++)
	{
		ptr[count]=content.substring(i,i+bit);
	}
	return ptr;
}

private void add_dictionary_toMap(HashMap<String,String> map,String str) throws IOException
{
	String text = new String(Files.readAllBytes(Paths.get(str)), StandardCharsets.UTF_8);
	String[] ptr=text.split(" ");// split dictionary on the basis of space ( returning array of string)
	int status=0;//variable is used to find when string is word and when string contains integer(freq of that word) (if status is even then word else integer)
	String key="";
	String value="";
	for(String s:ptr)
	{
		if(status%2==0)//if string is word then store it as key
		{
			value=s;
		}
		else// if string is integer then store it as value and save key,value in Map
		{
			 key=s;
			map.put(key,value);
		}
		status++;
	}
	
}

private void add_missdictionary_toMap(HashMap<String,String> map) throws IOException
{
	Huffman_coding h=new Huffman_coding();
	String text=h.decode();
	String[] words=text.split(" ");
	for(String s:words)
	{
		String id=generate_id();
		map.put(id,s);
	}
}

private String generate_id()
{
	int bit=(Integer.toBinaryString(max_dictionary_size)).length();
	String id=inttoBinary(max_dictionary_size+1,bit);
	max_dictionary_size++;
	return id;
}
private static String inttoBinary(int n,int bit)
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
}
