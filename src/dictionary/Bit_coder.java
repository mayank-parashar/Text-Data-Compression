package dictionary;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
public class Bit_coder {

  void binarytocode(String file) throws IOException
{
	String text = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
	text=text+"1";
	BitSet bs=new BitSet();
	int counter=0;
	for(char c:text.toCharArray())
	{
		if(c=='1')
		{
			bs.set(counter);
		}
		counter++;
	}
	byte[] data=bs.toByteArray();
	int size=data.length;
	System.out.println();
	try (FileOutputStream fos = new FileOutputStream(file)) {
		   fos.write(data);
		   //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
		}
}

  void codetoBinary(String file) throws IOException
{
	byte[] b=(Files.readAllBytes(Paths.get(file)));
	int size=b.length;
	
	BitSet binary=new BitSet();
	binary=BitSet.valueOf(b);
	StringBuilder binarystring=new StringBuilder();
	for(int i=0;i<binary.length()-1;i++)
	{
		if(binary.get(i))
		{
			binarystring.append('1');
		}
		else
		{
			binarystring.append('0');
		}
	}
	System.out.println(binarystring);
	try (BufferedWriter bw = new BufferedWriter(new FileWriter(file)))
	{
		bw.write(binarystring.toString());
	}
	catch (IOException e) {

		e.printStackTrace();

	}
}
}
