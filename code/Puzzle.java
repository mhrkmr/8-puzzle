import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Puzzle
{
	int n=9*8*7*6*5*4*3*2;
	HashMap<String,Integer> index=new HashMap<String,Integer>(n);
	HashMap<Integer,Vertex> vertex=new HashMap<Integer,Vertex>(n);
	int[] mass= new int[8];
	
	public Puzzle()
	{
		setVertices();
		setEdges();
	}
	
	public void setVertices()
	{
		String str="12345678G";
		Set<String> allStates=generatePerm(str);
		int i=0;
		for(String s : allStates)
		{
			index.put(s, i);
			vertex.put(i, new Vertex(s));
			i++;
		}
		if(i!=n)
		{
			System.out.println("incorrect");
		}
	}
	
	private Set<String> generatePerm(String str)
	{
		Set<String> set = new HashSet<String>();
		char a = str.charAt(0);
		if (str.length() > 1)
		{
		str = str.substring(1);
		Set<String> permSet = generatePerm(str);
			for (String x : permSet)
			{
				for (int i = 0; i <= x.length(); i++)
				{
					set.add(x.substring(0, i) + a + x.substring(i));
				}
			}
		}
		else
		{
			set.add(a + "");
		}
		return set;
	}
	
	public void setEdges()
	{
		for(int i=0;i<n;i++)
		{
			Vertex v=vertex.get(i);
			setUp(v,i);
			setDown(v,i);
			setLeft(v,i);
			setRight(v,i);
		}
	}
	
	private void setRight(Vertex v,int i) 
	{
		int x=v.state.indexOf('G');
		
		if(x%3==2)
		{
			v.edge[0]=-1;
			return;
		}
		
		int y= x+1;
		
		char arr[] = v.state.toCharArray();
		
		v.pt[0].moves=1;
		v.pt[0].weight=arr[y]-'1';
		v.pt[0].str=arr[y]+"L";
		
		char tmp = arr[x];
		arr[x] = arr[y];
		arr[y] = tmp;
		
		v.edge[0]= index.get(new String(arr));
	}

	private void setLeft(Vertex v, int i) 
	{
		int x=v.state.indexOf('G');
		
		if(x%3==0)
		{
			v.edge[1]=-1;
			return;
		}
		
		int y= x-1;
		char arr[] = v.state.toCharArray();
		
		v.pt[1].moves=1;
		v.pt[1].weight=arr[y]-'1';
		v.pt[1].str=arr[y]+"R";
		
		char tmp = arr[x];
		arr[x] = arr[y];
		arr[y] = tmp;
		
		v.edge[1]= index.get(new String(arr));
	}

	private void setDown(Vertex v, int i) 
	{
		int x=v.state.indexOf('G');
		
		if(x>=6 && x<=8)
		{
			v.edge[2]=-1;
			return;
		}
		
		int y= x+3;
		char arr[] = v.state.toCharArray();
		
		v.pt[2].moves=1;
		v.pt[2].weight=arr[y]-'1';
		v.pt[2].str=arr[y]+"U";
		
		char tmp = arr[x];
		arr[x] = arr[y];
		arr[y] = tmp;
		
		v.edge[2]= index.get(new String(arr));
	}

	private void setUp(Vertex v, int i) 
	{
		int x=v.state.indexOf('G');
		
		if(x>=0 && x<=2)
		{
			v.edge[3]=-1;
			return;
		}
		
		int y= x-3;
		char arr[] = v.state.toCharArray();
		
		v.pt[3].moves=1;
		v.pt[3].weight=arr[y]-'1';
		v.pt[3].str=arr[y]+"D";
		
		char tmp = arr[x];
		arr[x] = arr[y];
		arr[y] = tmp;
		
		v.edge[3]= index.get(new String(arr));
	}

	public Path getPath(String source,String dest)
	{
		Path[] d=new Path[n];
		boolean[] allNodes=new boolean[n];
		for(int i=0;i<n;i++)
		{
			d[i]=new Path();
			d[i].moves=Integer.MAX_VALUE;
			d[i].weight=Integer.MAX_VALUE;
			d[i].str="";
			allNodes[i]=false;
		}
		
		Heap minheap=new Heap(d);
		
		int sind=index.get(source);
		d[sind].moves=0;
		d[sind].weight=0;
		minheap.update(sind, 0);
		
		boolean x=checkPossibility(source,dest);
		if(x==false)
		{
			Path notP=new Path();
			notP.moves=-1;
			notP.weight=-1;
			return notP;
		}
		
		//System.out.println(index.get(source)+" "+index.get(dest));
		for(int i=0;i<n;i++)
		{
			int minI=minheap.min();
			//if(i<15000)
			//System.out.println(d[minI].weight);
			//int minI=minDistance2(d, allNodes);
			/*if(i==0||i==1)
				System.out.println(i+" "+minI);
			*/
			if(minI==index.get(dest))
				break;
			allNodes[minI]=true;
			
			for(int j=0;j<4;j++)
			{
				if(vertex.get(minI).edge[j]==-1)
					continue;
				if( d[vertex.get(minI).edge[j]].weight > d[minI].weight + mass[vertex.get(minI).pt[j].weight] 
						&& !allNodes[vertex.get(minI).edge[j]] && d[minI].weight!=Integer.MAX_VALUE)
				{
					//System.out.println("yo");
					//	||||||||  ||	|| ||||||| ||||||||  ||   //
					//	||		  ||	|| ||	   ||		 ||  //
					//	||		  ||	|| ||	   ||		 || //
					//	||		  |||||||| ||||	   ||		 ||//
					//	||		  ||	|| ||	   ||		 ||  \\
					//	||||||||  ||	|| ||||||| ||||||||  ||   \\
					d[vertex.get(minI).edge[j]].weight = d[minI].weight + mass[vertex.get(minI).pt[j].weight];
					minheap.update(vertex.get(minI).edge[j],d[minI].weight + mass[vertex.get(minI).pt[j].weight]);
					d[vertex.get(minI).edge[j]].moves = d[minI].moves + vertex.get(minI).pt[j].moves;
					d[vertex.get(minI).edge[j]].str = d[minI].str + " " + vertex.get(minI).pt[j].str;
				}
			}
		}
		
		return d[index.get(dest)];
	}

	private boolean checkPossibility(String source, String dest) 
	{
		int i=numInversions(source);
		int j=numInversions(dest);
		if(i%2!=j%2)
		{
			return false;
		}
		return true;
	}

	private int numInversions(String str) 
	{
		char sarr[]=str.toCharArray();
		int inv=0;
		for(int i=0;i<9;i++)
		{
			int ch=sarr[i];
			for(int j=i+1;j<9;j++)
			{
				int ch2=sarr[j];
				if(ch2<ch && sarr[i]!='G' && sarr[j]!='G')
					inv++;
			}
		}
		
		return inv;
	}

	int minDistance2(Path d[], boolean node[])
	{
		int min = Integer.MAX_VALUE, min_index = 0,minmove=Integer.MAX_VALUE;
		
		for (int i = 0; i < n; i++)
		{
			if(node[i] == false && d[i].weight <= min)
			{
				if(d[i].moves>minmove && d[i].weight==min)
					continue;
				min = d[i].weight;
				min_index = i;
			}
		}
		return min_index;
	}
	
	
	public static void main(String args[])
	{
		try 
		{
			long startTime=System.currentTimeMillis();
			Scanner sin = new Scanner(new File(args[0]));
			OutputStream sout = new FileOutputStream(new File(args[1]));
			
			Puzzle pgraph=new Puzzle();
			
			int n = sin.nextInt();
			for(int i=0;i<n;i++)
			{
				String source=sin.next();
				String destination=sin.next();
				int[] arr=new int[9];
				for(int j=0;j<8;j++)
				{
					arr[j]=sin.nextInt();
				}
				pgraph.mass=arr;
				
				/*int q=pgraph.index.get(source);
				System.out.println(source+" "+ q);
				int z=pgraph.vertex.get(q).edge[];
				System.out.println(pgraph.vertex.get(z).state+" "+z+ " " +pgraph.vertex.get(q).pt[3].str);
				*/
				Path result=pgraph.getPath(source, destination);
				
				sout.write((result.moves+" "+result.weight+"\n").getBytes());
				sout.write((result.str+"\n").getBytes());
				//System.out.println(result.moves+" "+result.weight);
				//System.out.println(result.str.trim());
			}
			long time=System.currentTimeMillis()-startTime;
	        System.out.println("time: "+time+" millis");
			sin.close();
			sout.close();
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}