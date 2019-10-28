import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.util.StringTokenizer;

import java.util.*;


public class DonutRack {
	private static InputReader in;
    private static PrintWriter out;
	
	public static void main(String[] args) throws IOException {
		InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        readInput();//execution
        out.close();
	}
	
	private static void readInput() throws IOException {
		String[] tmp;
		rackDonut rack = new rackDonut();
		
		//asking for basic input 
		int lineAmount = in.nextInt();
		for(int i=0;i<lineAmount;i++){ 
			String[] lineInput = in.nextLine().split(" ");
			int donutAmount = Integer.parseInt(lineInput[0]);
			LinkedListDonut line = new LinkedListDonut();//mewakili barisan
			for(int j=0;j<donutAmount;j++) {
				Donut donut = new Donut(Integer.parseInt(lineInput[j+1]));
				line.addBack(donut);
			}
			rack.addLine(line);
		}
		
		//asking for command input
		int commandAmount = in.nextInt();
		for(int i=0;i<commandAmount;i++) {
			try{
				String command = in.nextLine();
				rack.executeCommand(command);
			}
			catch(Exception e) {
				
			}
		}
		
		for(int i=0; i<rack.lineContainer.size();i++) {
			LinkedListDonut currentLine = rack.lineContainer.get(i);
			ListNodeDonut tmp1 = currentLine.header.next;
			while(tmp1.next!= null) {
				if(tmp1.next.next==null) {
					out.print(tmp1.donut.chips);
					tmp1 = tmp1.next;
				}
				else {
					out.print(tmp1.donut.chips);
					out.print(" ");
					tmp1 = tmp1.next;
				}
			}
			out.println();
		}
	}
	
	static class rackDonut {
		ArrayList<LinkedListDonut> lineContainer;
		
		rackDonut() {
			lineContainer = new ArrayList<LinkedListDonut>();
		}
		
		public String toString() {
			String result = "";
			for(int i=0; i<lineContainer.size();i++) {
				if(i==lineContainer.size()-1) {
					result = result+lineContainer.get(i);
				}
				else {
					result = result+lineContainer.get(i)+"\n";
				}
			}
			return result;
		}

		public void executeCommand(String input) {
			String[] fullCommand = input.split(" ");
			String command = fullCommand[0];
			
			if(command.equals("IN_FRONT")) {
				int chips = Integer.parseInt(fullCommand[1]);
				int line  = Integer.parseInt(fullCommand[2])-1;
				
				Donut donut = new Donut(chips);
				lineContainer.get(line).addFront(donut);
				
				lineContainer.add(lineContainer.get(line));
				lineContainer.remove(line);
			}
			else if(command.equals("OUT_FRONT")) {
				int line = Integer.parseInt(fullCommand[1])-1;
				
				lineContainer.get(line).removeFront();
				
				if(lineContainer.get(line).isEmpty()){
					lineContainer.remove(line);
				}

				lineContainer.add(lineContainer.get(line));
				lineContainer.remove(line);
			}
			else if(command.equals("IN_BACK")) {
				int chips = Integer.parseInt(fullCommand[1]);
				int line  = Integer.parseInt(fullCommand[2])-1; 
				
				Donut donut = new Donut(chips);
				lineContainer.get(line).addBack(donut);
				
				lineContainer.add(lineContainer.get(line));
				lineContainer.remove(line);
			}
			else if(command.equals("OUT_BACK")) {
				int line = Integer.parseInt(fullCommand[1])-1;
				
				lineContainer.get(line).removeBack();
				
				if(lineContainer.get(line).isEmpty()){
					lineContainer.remove(line);
				}
				
				lineContainer.add(lineContainer.get(line));
				lineContainer.remove(line);
			}
			else if(command.equals("MOVE_FRONT")) {
				int senderLine = Integer.parseInt(fullCommand[1])-1;
				int receiverLine = Integer.parseInt(fullCommand[2])-1;
				LinkedListDonut sender = lineContainer.get(senderLine);
				LinkedListDonut receiver = lineContainer.get(receiverLine);
				
				lineContainer.add(receiver);
				LinkedListDonut.transferFront(sender, lineContainer.get(lineContainer.size()-1));
				lineContainer.remove(sender);
				lineContainer.remove(receiver);
				
			}
			else if(command.equals("MOVE_BACK")) {
				int senderLine = Integer.parseInt(fullCommand[1])-1;
				int receiverLine = Integer.parseInt(fullCommand[2])-1;
				LinkedListDonut sender = lineContainer.get(senderLine);
				LinkedListDonut receiver = lineContainer.get(receiverLine);
				
				lineContainer.add(receiver);
				LinkedListDonut.transferBack(sender, lineContainer.get(lineContainer.size()-1));
				lineContainer.remove(sender);
				lineContainer.remove(receiver);
			}
			else if(command.equals("NEW")) {
				int chips = Integer.parseInt(fullCommand[1]);
				
				Donut donut = new Donut(chips);
				LinkedListDonut newLine = new LinkedListDonut();
				newLine.addFront(donut);
				
				this.lineContainer.add(newLine);	
			}
			
			//sort
			if(lineContainer.size()==2) {
				sort(1);
			}
			else if(lineContainer.size()>1) {
				sort(lineContainer.size()-1);
			}
		}
		
		public void addLine(LinkedListDonut line) {
			this.lineContainer.add(line);
		}
		
		public void removeLine(LinkedListDonut line) {
			int index = this.lineContainer.indexOf(line);
			this.lineContainer.remove(index);
		}
		
		//insertion sort
		public void sort(int start) {
			insertionSort(lineContainer, start);
		}
		
		public static void insertionSort (ArrayList<LinkedListDonut> lines, int start) {
			for (int ii = start; ii < lines.size(); ii++) {
				int jj = ii;
				
				ListNodeDonut tmp1 = lines.get(jj).header.next;
				ListNodeDonut tmp2 = lines.get(jj-1).header.next;
				
				
				while (( jj > 0) && (tmp1.donut.chips <= tmp2.donut.chips)) {
					if(tmp1.donut.chips < tmp2.donut.chips) {
						LinkedListDonut temp = lines.get(jj);
						lines.set(jj, lines.get(jj-1));
						lines.set(jj-1, temp);
						jj--;
						if(jj>0) {
							tmp1 = lines.get(jj).header.next;
							tmp2 = lines.get(jj-1).header.next;
						}
						continue;
					}
					if(tmp1.donut.chips == tmp2.donut.chips) {
						tmp1 = tmp1.next;
						tmp2 = tmp2.next;
					}
				}
			}
		}
	}
		
	static class LinkedListDonut {
		ListNodeDonut header;
		ListNodeDonut tail;
		
		LinkedListDonut () {
			header = new ListNodeDonut(null);
			tail = new ListNodeDonut(new Donut(0));//aslinya null
			header.next = tail;
			tail.prev = header;
			
		}

		public boolean isEmpty() {
			if(header.next.next == null){
				return true;
			}
			if(tail.prev.prev == null) {
				return true;
			}
			return false;
		}
		
		public void addFront(Donut donut) {
			ListNodeDonut tmp = new ListNodeDonut(donut, header.next, header);
			this.header.next.prev = tmp;
			this.header.next = tmp;
		}
		
		public void removeFront() {
			this.header.next.next.prev = this.header;
			this.header.next = this.header.next.next;
		}
		
		
		public void addBack(Donut donut) {
			ListNodeDonut tmp = new ListNodeDonut(donut, tail, tail.prev);
			this.tail.prev.next = tmp;
			this.tail.prev = tmp;
		}
		
		public void removeBack() {
			this.tail.prev.prev.next = this.tail;
			this.tail.prev = this.tail.prev.prev;
		}
		
		public static void transferFront(LinkedListDonut sender, LinkedListDonut receiver) {
			sender.header.next.prev = receiver.header; 
			sender.tail.prev.next = receiver.header.next; 
			
			receiver.header.next.prev = sender.tail.prev;
			receiver.header.next = sender.header.next;
		}			
		
		public static void transferBack(LinkedListDonut sender, LinkedListDonut receiver) {
			sender.header.next.prev = receiver.tail.prev;
			sender.tail.prev.next = receiver.tail;  
			
			receiver.tail.prev.next = sender.header.next; 
			receiver.tail.prev = sender.tail.prev; 
		}
		
		public String toString() {
			String result = "";
			ListNodeDonut tmp = header.next;
			while(tmp.next!=null) {
				if(tmp.next.next==null) {
					result = result+tmp.donut;
					tmp = tmp.next;
				}
				else {
					result = result+tmp.donut+" ";
					tmp = tmp.next;
				}
			}
			return result;
		}
	}
	
	static class ListNodeDonut {
		Donut donut;
		ListNodeDonut next;
		ListNodeDonut prev;
		
		ListNodeDonut (Donut donut, ListNodeDonut next, ListNodeDonut prev){
			this.donut = donut;
			this.next = next;
			this.prev = prev;
		}
		
		ListNodeDonut (Donut donut, ListNodeDonut next){
			this.donut = donut;
			this.next = next;
		}
		
		ListNodeDonut (Donut donut) {
			this.donut = donut;
		}
		
		ListNodeDonut () {
			this(null,null);
		}
	}
	
	static class Donut {
		int chips;
		
		public Donut(int chips) {
			this.chips = chips;
        }
		
		public String toString() {
			return String.valueOf(chips);
		}	
	}
	
	private static void printOutput(String answer) throws IOException {
        out.println(answer);
    }
	
	private static class InputReader {
        // taken from https://codeforces.com/submissions/Petr
        public BufferedReader reader;
        public StringTokenizer tokenizer;
		
        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public String nextLine() throws IOException{
            return reader.readLine();
        }
    }
}

