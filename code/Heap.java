import java.util.HashMap;

public class Heap
{
	Path heap[];
	HashMap<Integer,Integer> heapindex;
	HashMap<Integer,Integer> trueindex;
	int n;

	public Heap(Path[] d) 
	{
		n=0;
		int m=d.length+1;
		heap=new Path[m];
		heapindex=new HashMap<Integer,Integer>(m);
		trueindex=new HashMap<Integer,Integer>(m);
		heap[0]=new Path();
		heap[0].weight=Integer.MIN_VALUE;
		
		for(int i=1;i<m;i++)
		{
			heap[i]=d[i-1];
			heapindex.put(i, i);
			trueindex.put(i, i);
			n++;
		}
		//for (int i = n/2; i >= 1;i--)
		//{
		//	percDown(i, heap[i],trueindex.get(i));
			//System.out.println(i+" "+trueindex.get(i));
		//}
	}

	public int min() 
	{
		int min=trueindex.get(1)-1;
		deleteMin();
		return min;
	}
	
	public void deleteMin()
	{
		n--;
		heap[1]=heap[n+1];
		heapindex.put(trueindex.get(n+1),1);
		trueindex.put(1,trueindex.get(n+1));		
		percDown(1,heap[1],trueindex.get(1));
	}	

	public void update(int i, int weight)
	{
		int j= heapindex.get(i+1);
		heap[j].weight=weight;
		percUp(j,heap[j],i+1);
	}
	
	private void percUp(int i,Path x,int k)
	{
		
		if(i==0)
		{
			return;
		}
		else
		{
			if(heap[i/2].weight>heap[i].weight)
			{
				heap[i]= heap[i/2];
				heapindex.put(trueindex.get(i/2),i);
				trueindex.put(i,trueindex.get(i/2));
				heap[i/2]=x;
				heapindex.put(k, i/2);
				trueindex.put(i/2, k);
				//System.out.println(i/2+" "+k);
				percUp(i/2, x, k);
			}
			else
			{
				if (heap[i/2].weight == x.weight && heap[i/2].moves > x.moves)
				{
					heap[i] = heap[i/2];
					heapindex.put(trueindex.get(i/2),i);
					trueindex.put(i,trueindex.get(i/2));
					heap[i/2] = x;
					heapindex.put(k, i/2);
					trueindex.put(i/2, k);
					percUp(i/2, x, k);
					return;
				}
				heap[i]= x;
				heapindex.put(k,i);
				trueindex.put(i,k);
			}
		}
	}
	
	private void percDown(int i, Path x,int k)
	{
		int j;
		if(2*i > n) 
		{
			heap[i]= x;
			heapindex.put(k, i);
			trueindex.put(i, k);
		}
		else if(2*i == n)
		{
			if (heap[2*i].weight < x.weight) 
			{
				heap[i] = heap[2*i];
				heapindex.put(trueindex.get(i*2),i);
				trueindex.put(i,trueindex.get(i*2));
				heap[2*i] = x;
				heapindex.put(k, 2*i);
				trueindex.put(2*i, k);
			}
			else 
			{
				if (heap[2*i].weight == x.weight && heap[2*i].moves < x.moves)
				{
					heap[i] = heap[2*i];
					heapindex.put(trueindex.get(i*2),i);
					trueindex.put(i,trueindex.get(i*2));
					heap[2*i] = x;
					heapindex.put(k, 2*i);
					trueindex.put(2*i, k);
					return;
				}
				heap[i] = x;
				heapindex.put(k,i);
				trueindex.put(i,k);
			}
		}
		else if(2*i < n)
		{ 
			if(heap[2*i].weight < heap[2*i+1].weight)
			{
				j = 2*i; 
			}
			else 
				j = 2*i+1; 
			if(heap[j].weight < x.weight)
			{
				heap[i] = heap[j];
				heapindex.put(trueindex.get(j),i);
				trueindex.put(i,trueindex.get(j));
				percDown(j,x,k); 
			}
			else 
			{
				if (heap[j].weight == x.weight && heap[j].moves < x.moves)
				{
					heap[i] = heap[j];
					heapindex.put(trueindex.get(j),i);
					trueindex.put(i,trueindex.get(j));
					heap[j] = x;
					heapindex.put(k, j);
					trueindex.put(j, k);
					percDown(j,x,k); 
				}
				heap[i] = x;
				heapindex.put(k,i);
				trueindex.put(i,k);
			}
		}
	}
}