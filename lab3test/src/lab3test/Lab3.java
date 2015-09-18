package lab3test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Lab3 {
	static int N = 10;

	
	static void solve(int items [], int size, int l, int rank, PrintWriter writer){	
		// передаємо: масив, його розмір, номер елемента масиву, ранг процеса, файловий потік
		
		if(rank==0 && items[0]>size/2) return;	// якщо перший процес і перше число в послідовності більше за половину від кількості
		if(rank==1 && items[0]<=size/2) return;	// якщо другий процес і перше число в послідовності менше за половину від кількості

	    int i;
	    if (l==size){	// якщо ми дійшли до кінця масиву
	        for (i=0; i<size; i++) 
				if(items[i]==i+1) return;	// перевіряємо чи є числа які стоять під своїм порядковим номером
				// якщо так - вихід
			
			 
	        for (i=0; i<size; i++)		// запис у файл
	        	writer.print(" "+items[i]);
	        writer.println();
	    }
	    else
	        for (i=l; i<size; i++){	// шукаємо наступну послідовність
	        	int j = l^i;
	            if (j!=0){
				
	                 items[l]^= items[i];
	                 items[i]^= items[l];
	                 items[l]^= items[i];
	                 solve(items, size, l+1,rank,writer);
	                 items[l]^= items[i];
	                 items[i]^= items[l];
	                 items[l]^= items[i];
	            }else {
	                solve(items, size, l+1,rank,writer);
				}
	        }
	}

	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

		long startTime, endTime;

		int arr1[] = new int[N];		// масив для знаходження першої половини послідовностей
	    int arr2[] = new int[N];	// для другої
		for(int i=0; i<N; i++) arr1[i]=arr2[i]=i+1;	// заповнюємо масиви від 1 до Н
		int b = arr2[0];		// в другому масиві міняємо місцями перше і середні в послідовності числа
		arr2[0] = arr2[N/2];
		arr2[N/2] =  b;

		
		//writer.println("The first line");
		//writer.println("The second line");
		
		startTime = System.currentTimeMillis();

		Thread thr[] = new Thread[2];
		
		Runnable r = new MyRunnable(arr1,"file1.txt",0);
		//executor.execute(r);
		Thread t = new Thread(r);
		 t.start();
		 thr[0] = t;
			
			r = new MyRunnable(arr2,"file2.txt",1);
			//executor.execute(r);
			t = new Thread(r);
			 t.start();
			 thr[1] = t;
		
		int running = 0;
		do {
			running = 0;
			for (Thread thread : thr) {
				if (thread.isAlive()) {
					running++;
				}
			}

		} while (running > 0);
		
		endTime = System.currentTimeMillis() - startTime;
		System.out.println("time1: " + endTime);
		
	}
	
	static class MyRunnable implements Runnable {

		private int arr[];
		private String file;
		private int rang;
		
		
		public MyRunnable(int arr[], String file, int rang) {
			this.arr=arr;
			this.file=file;
			this.rang=rang;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				PrintWriter writer = new PrintWriter(file, "UTF-8");
				solve(arr,N,0,rang,writer);
				writer.close();
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
		/*
		Random rand = new Random();
		//int arr[] = new int[N];
		Thread thr[] = new Thread[Z];

		double T[][];	// масив координат точок
		double maxL, l;	// максимальна відстань і відстань для порівняння
		int t1, t2;	

		T = new double[N][2];
		
		for (int i = 0; i < N; i++) {
			T[i][0] = rand.nextInt(N * 10);
			T[i][1] = rand.nextInt(N * 10);
		}

		startTime = System.currentTimeMillis();
		
		t1 = 0;	// початкові значення найвіддаленіших точок
		t2 = 1;
		maxL = getLength(T[t1][0],T[t2][0],T[t1][1],T[t2][1]);
		
		for(int i=1; i<N-1; i++) {	// шукаємо найбільу відстань
			for(int j=i+1; j<N; j++) {
				l = getLength(T[i][0],T[j][0],T[i][1],T[j][1]);
				if(l>maxL) {
					maxL = l;
					t1 = i;
					t2 = j;
				}
			}
		}
		
		endTime = System.currentTimeMillis() - startTime;
		System.out.println("time1: " + endTime + " max=" + maxL + " ("+t1+","+t2+")");

		startTime = System.currentTimeMillis();

		//ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
		for (int i = 0; i < Z; i++) {
			System.out.println("N="+N+" Z="+Z+" i="+i+" start="+(N / Z * i)+" end="+(N / Z * i + N / Z));
			Runnable r = new MyRunnable(T, N / Z * i, N / Z * i + N / Z, i);
			//executor.execute(r);
			Thread t = new Thread(r);
			 t.start();
			 thr[i] = t;
		}

		int running = 0;
		do {
			running = 0;
			for (Thread thread : thr) {
				if (thread.isAlive()) {
					running++;
				}
			}

		} while (running > 0);

		int max = 0;
		for(int i=1; i<Z; i++) {
			if(rezL[i]>rezL[0]) max=i;
		}
		maxL = rezL[max];
		t1 = rezT[max][0];
		t2 = rezT[max][1];
		
		endTime = System.currentTimeMillis() - startTime;
		System.out.println("time2: " + endTime + " max=" + maxL + " ("+t1+","+t2+")");
		
		//System.out.println("time2: " + endTime);
	}

	static class MyRunnable implements Runnable {
		private double T[][];
		private int start;
		private int end;
		private int num;

		public MyRunnable(double arr[][], int start, int end, int num) {
			this.T = arr;
			this.start = start;
			this.end = end;
			this.num = num;
		}

		public void run() {
			long startTime, endTime;
			startTime = System.currentTimeMillis();
			
			double maxL, l;	// максимальна відстань і відстань для порівняння
			int t1, t2;	
			
			t1 = start;	// початкові значення найвіддаленіших точок
			t2 = start+1;
			maxL = getLength(T[t1][0],T[t2][0],T[t1][1],T[t2][1]);
			
			for(int i=start+1; i<end-1; i++) {	// шукаємо найбільу відстань
				for(int j=i+1; j<end; j++) {
					l = getLength(T[i][0],T[j][0],T[i][1],T[j][1]);
					if(l>maxL) {
						maxL = l;
						t1 = i;
						t2 = j;
					}
				}
			}
			
			
			endTime = System.currentTimeMillis() - startTime;

			System.out.println("time "+num+": " + " (" + startTime + " - " + endTime
					+ ")"  + " max=" + maxL + " ("+t1+","+t2+")");
			
			rezL[num] = maxL;
			rezT[num][0] = t1;
			rezT[num][1] = t2;
		}*/
	
	
	
}
