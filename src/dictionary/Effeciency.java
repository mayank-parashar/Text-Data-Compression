package dictionary;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Effeciency {
	String compressed;
	HashMap<String,String> miss_map;
	String original_data;
	String miss_data="miss_data.txt";
Effeciency(String compressed,String original_data) throws IOException
{
	miss_map=new HashMap<String,String>();
	this.compressed=compressed;
	add_dictionary_toMap(miss_map,miss_data);
	this.original_data=original_data;
}
protected void compration_ratio() throws IOException
{
	double s1=original_size();
	double s2=miss_data_size();
	double s3=binary_file_size();//it is binary file size
	double s4=miss_dataccode_size();
	System.out.println("Original file size is "+s1+" binary compressed file size is "+s3+" miss data size is "+s2+" miss datacode size is "+s4);
	System.out.println((s2+s3+s4*8)/(s1));
	
}


private int miss_dataccode_size() throws IOException
{
	String text = new String(Files.readAllBytes(Paths.get("miss_datacode.txt")), StandardCharsets.UTF_8);
	return text.length();
}

protected int binary_file_size()
{
	return compressed.length();
}

protected int miss_data_size() throws IOException
{
	String text = new String(Files.readAllBytes(Paths.get("miss_data.txt")), StandardCharsets.UTF_8);
	return text.length();
}

protected int original_size()
{
	return original_data.length();
}

private void add_dictionary_toMap(HashMap<String,String> map,String str) throws IOException
{
	String text = new String(Files.readAllBytes(Paths.get(str)), StandardCharsets.UTF_8);
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
