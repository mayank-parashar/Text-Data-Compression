package dictionary;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
class Node
{
    char ch;
    int freq;
    Node left = null, right = null;

    Node(char ch, int freq)
    {
        this.ch = ch;
        this.freq = freq;
    }

    public Node(char ch, int freq, Node left, Node right) {
        this.ch = ch;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }
};
public class Huffman_coding {
	
public static void encode(Node root, String str,
        Map<Character, String> huffmanCode)
{
if (root == null)
return;

// found a leaf node
if (root.left == null && root.right == null) {
huffmanCode.put(root.ch, str);
}


encode(root.left, str + "0", huffmanCode);
encode(root.right, str + "1", huffmanCode);
}

public  void buildHuffmanTree(String text)
{
    // count frequency of appearance of each character
    // and store it in a map
    Map<Character, Integer> freq = new HashMap<>();
    for (int i = 0 ; i < text.length(); i++) {
        if (!freq.containsKey(text.charAt(i))) {
            freq.put(text.charAt(i), 0);
        }
        freq.put(text.charAt(i), freq.get(text.charAt(i)) + 1);
    }

    // Create a priority queue to store live nodes of Huffman tree
    // Notice that highest priority item has lowest frequency
    PriorityQueue<Node> pq = new PriorityQueue<>((l, r) -> l.freq - r.freq);

    // Create a leaf node for each character and add it
    // to the priority queue.
    for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
        pq.add(new Node(entry.getKey(), entry.getValue()));
    }

    // do till there is more than one node in the queue
    while (pq.size() != 1)
    {
        // Remove the two nodes of highest priority
        // (lowest frequency) from the queue
        Node left = pq.poll();
        Node right = pq.poll();

        // Create a new internal node with these two nodes as children 
        // and with frequency equal to the sum of the two nodes
        // frequencies. Add the new node to the priority queue.
        int sum = left.freq + right.freq;
        pq.add(new Node('\0', sum, left, right));
    }

    // root stores pointer to root of Huffman Tree
    Node root = pq.peek();

    // traverse the Huffman tree and store the Huffman codes in a map
    Map<Character, String> huffmanCode = new HashMap<>();
    encode(root, "", huffmanCode);

    // print the Huffman codes
    String data=new String();
    //System.out.println("Huffman Codes are :\n");
    int count=0;
    for (Map.Entry<Character, String> entry : huffmanCode.entrySet()) {
    	count++;
        //System.out.println(entry.getKey() + " " + entry.getValue());
        if((int)entry.getKey()==32)
        	data=data+"32"+" "+entry.getValue()+" ";
        else
        data=data+entry.getKey()+" "+entry.getValue()+" ";
    }
    //System.out.println("\nOriginal string was :\n" + text);
    write_data(data,"miss_datacode.txt");
    data="";
    // print encoded string
    StringBuilder sb = new StringBuilder();
    for (int i = 0 ; i < text.length(); i++) {
        sb.append(huffmanCode.get(text.charAt(i)));
    }

    //System.out.println("\nEncoded string is :\n" + sb);
    data=data+sb.toString();
    write_data(data,"miss_data.txt");
}
private void write_data(String data,String file)
{
	
	try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
		
		bw.write(data);//writing whole string(content) into dictionary
		
		// no need to close it.
		//bw.close();

		

	} catch (IOException e) {

		e.printStackTrace();

	}
	
}

public String decode() throws IOException
{
	HashMap<String,String> map=new HashMap<String,String>();
	load_map(map,"miss_datacode.txt");
	String text = new String(Files.readAllBytes(Paths.get("miss_data.txt")), StandardCharsets.UTF_8);
	String content=original_String(map,text);
	return content;
}

private void load_map(HashMap<String,String> map,String file) throws IOException
{
	String text = new String(Files.readAllBytes(Paths.get(file)), StandardCharsets.UTF_8);
	String code[]=text.split(" ");
	int status=0;
	String value=new String();
	String key=new String();
	for(String s:code)
	{
		if(status%2==0)
		{
			value=s;
		}
		else
		{
			key=s;
			map.put(key,value);
		}
		status++;
	}
}

private String original_String(HashMap<String,String> map,String data)
{
	int len=data.length();
	int from=0,to=1;
	StringBuilder content=new StringBuilder();
	for(;to<=len;to++)
	{
		String s=data.substring(from,to);
		if(map.containsKey(s))
		{
			String value=map.get(s);
			if(value.equals("32"))
			{
				value=" ";
			}
			content.append(value);
			from=to;
		}
		
	}
	return content.toString();
}
}
