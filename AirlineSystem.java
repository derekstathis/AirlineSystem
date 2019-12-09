/*************************************************************************
*  An Airline management system that uses a weighted-edge directed graph 
*  implemented using adjacency lists.
*************************************************************************/

//DGS32@pitt.edu
//

import java.util.*;
import java.io.*;

public class AirlineSystem {
  private String [] cityNames = null;
  private Digraph G = null;
  private static Scanner scan = null;
  private static final int INFINITY = Integer.MAX_VALUE;


  /**
  * Test client.
  */
  public static void main(String[] args) throws IOException {
    AirlineSystem airline = new AirlineSystem();
    scan = new Scanner(System.in);
    while(true){
      switch(airline.menu()){
        case 1:
        	airline.readGraph();
        	break;
        case 2:
        	airline.printGraph();
        	break;
        case 3:
        	airline.spanningTree();
        	break;
        case 4:
        	airline.shortestHops();
        	break;
        case 5:
            airline.shortestDistance();
            break;
        case 6:
        	airline.shortestPrice();
        	break;
        case 7:
        	airline.lessThanAmount();
        	break;
        case 8:
        	airline.addRoute();
        	break;
        case 9:
        	airline.removeRoute();
        	break;
        case 10:
        	System.out.println("Goodbye.");
        	scan.close();
        	System.exit(0);
        	break;
        default:
        	System.out.println("Incorrect option.");
      }
    }
  }

  private int menu(){
    System.out.println("*********************************");
    System.out.println("Welcome to FifteenO'One Airlines!");
    System.out.println("1. Read data from a file.");
    System.out.println("2. Display all routes.");
    System.out.println("3. Display minimum spanning tree. ");
    System.out.println("4. Compute shortest path based on number of hops.");
    System.out.println("5. Compute shortest path based on distance.");
    System.out.println("6. Compute shortest path based on price.");
    System.out.println("7. Display costs less than the amount entered. ");
    System.out.println("8. Add a route to the schedule. ");
    System.out.println("9. Remove a route from the schedule. ");
    System.out.println("10. Exit.");
    System.out.println("*********************************");
    System.out.print("Please choose a menu option (1-10): ");

    int choice = Integer.parseInt(scan.nextLine());
    return choice;
  }

  private void readGraph() throws IOException {
    System.out.println("Please enter graph filename:");
    String fileName = scan.nextLine();
    Scanner fileScan = new Scanner(new FileInputStream(fileName));
    int v = Integer.parseInt(fileScan.nextLine());
    G = new Digraph(v);

    cityNames = new String[v];
    for(int i=0; i<v; i++){
      cityNames[i] = fileScan.nextLine();
    }

    while(fileScan.hasNext()){		//read in data from file
      int from = fileScan.nextInt();
      int to = fileScan.nextInt();
      int weight = fileScan.nextInt();
      double price = fileScan.nextDouble();
      G.addEdge(new WeightedDirectedEdge(from-1, to-1, weight, price));	//make undirected edges
      G.addEdge(new WeightedDirectedEdge(to-1, from-1, weight, price));
      //fileScan.nextLine();
    }
    fileScan.close();
    System.out.println("Data imported successfully.");
    System.out.print("Please press ENTER to continue ...");
    scan.nextLine();
  }

  //prints the graph
  private void printGraph() {
    if(G == null){
      System.out.println("Please import a graph first (option 1).");
      System.out.print("Please press ENTER to continue ...");
      scan.nextLine();
    } else {
    	System.out.println();
      for (int i = 0; i < G.v; i++) {
        System.out.print(cityNames[i] + ": ");
        for (WeightedDirectedEdge e : G.adj(i)) {
          System.out.print(cityNames[e.to()] + "(" + e.weight() + ",$" + e.price()+") ");
        }
        System.out.println();
      }
      System.out.println();
      System.out.print("Please press ENTER to continue ...");
      scan.nextLine();

    }
  }
  
  //shortest number of hops from one city to another city
  private void shortestHops() {
	    if(G == null){
	      System.out.println("Please import a graph first (option 1).");
	      System.out.print("Please press ENTER to continue ...");
	      scan.nextLine();
	    } else {
	      for(int i=0; i<cityNames.length; i++){
	        System.out.println(i+1 + ": " + cityNames[i]);
	      }
	      System.out.print("Please enter source city (1-" + cityNames.length + "): ");
	      int source = Integer.parseInt(scan.nextLine());
	      System.out.print("Please enter destination city (1-" + cityNames.length + "): ");
	      int destination = Integer.parseInt(scan.nextLine());
	      source--;
	      destination--;
	      G.bfs(source);		//use bfs search
	      if(!G.marked[destination]){
	        System.out.println("There is no route from " + cityNames[source]
	                            + " to " + cityNames[destination]);
	      } else {
	        Stack<Integer> path = new Stack<>();
	        for (int x = destination; x != source; x = G.edgeTo[x])
	            path.push(x);
	        path.push(source);
	        System.out.print("The shortest route from " + cityNames[source] +
	                           " to " + cityNames[destination] + " has " +
	                           G.distTo[destination] + " hop(s): ");
	        while(!path.empty()){
	          System.out.print(cityNames[path.pop()] + " ");
	        }
	        System.out.println();

	      }
	      System.out.print("Please press ENTER to continue ...");
	      scan.nextLine();
	    }
	  }
  	
  	//shortest distance between two cities
    private void shortestDistance() {
      if(G == null){
        System.out.println("Please import a graph first (option 1).");
        System.out.print("Please press ENTER to continue ...");
        scan.nextLine();
      } else {
        for(int i=0; i<cityNames.length; i++){
          System.out.println(i+1 + ": " + cityNames[i]);
        }
        System.out.print("Please enter source city (1-" + cityNames.length + "): ");		//user input
        int source = Integer.parseInt(scan.nextLine());
        System.out.print("Please enter destination city (1-" + cityNames.length + "): ");
        int destination = Integer.parseInt(scan.nextLine());
        source--;
        destination--;
        G.dijkstrasDistance(source, destination);				//dijkstras for distance
        if(!G.marked[destination]){
          System.out.println("There is no route from " + cityNames[source]
                              + " to " + cityNames[destination]);
        } else {
          Stack<Integer> path = new Stack<>();
          for (int x = destination; x != source; x = G.edgeTo[x]){
              path.push(x);
          }
          System.out.print("The shortest route from " + cityNames[source] +
                             " to " + cityNames[destination] + " has " +
                             G.distTo[destination] + " miles: ");

          int prevVertex = source;
          System.out.print(cityNames[source] + " ");
          while(!path.empty()){
            int v = path.pop();
            System.out.print(G.distTo[v] - G.distTo[prevVertex] + " "
                             + cityNames[v] + " ");
            prevVertex = v;
          }
          System.out.println();

        }
        System.out.print("Please press ENTER to continue ...");
        scan.nextLine();
      }
  }
    
    
    //shortest distance based on price
    private void shortestPrice()
    {
    	if(G == null){
            System.out.println("Please import a graph first (option 1).");
            System.out.print("Please press ENTER to continue ...");
            scan.nextLine();
          } else {
            for(int i=0; i<cityNames.length; i++){
              System.out.println(i+1 + ": " + cityNames[i]);
            }
            System.out.print("Please enter source city (1-" + cityNames.length + "): ");	//user input
            int source = Integer.parseInt(scan.nextLine());
            System.out.print("Please enter destination city (1-" + cityNames.length + "): ");
            int destination = Integer.parseInt(scan.nextLine());
            source--;
            destination--;
            G.dijkstrasPrice(source, destination);			//dijkstras for price instead of weight
            if(!G.marked[destination]){
              System.out.println("There is no route from " + cityNames[source]
                                  + " to " + cityNames[destination]);
            } else {
              Stack<Integer> path = new Stack<>();
              for (int x = destination; x != source; x = G.edgeTo[x]){
                  path.push(x);
              }
              System.out.print("The shortest route (in price) from " + cityNames[source] +
                                 " to " + cityNames[destination] + " is $" +
                                 G.distTo[destination] + ": ");

              int prevVertex = source;
              System.out.print(cityNames[source] + " ");
              while(!path.empty()){
                int v = path.pop();
                System.out.print("$"+(G.distTo[v] - G.distTo[prevVertex]) + " "+ cityNames[v] + " | ");
                prevVertex = v;
              }
              System.out.println();

            }
            System.out.print("Please press ENTER to continue ...");
            scan.nextLine();
          }
    }
    
    private void spanningTree()
    {
    	//TODO: spanning tree
    	
    }
    
    private void lessThanAmount()
    {
          double maxCost;
          scan  = new Scanner(System.in);
          System.out.printf("Enter the cost: $");
          
          maxCost = scan.nextDouble();


        // loop for each starting vertice
        for (int ver=1; ver<G.v; ver++) {

          routesBelow(ver,maxCost);
        }
        
        System.out.println();
        
        System.out.print("Please press ENTER to continue ...");
        scan.nextLine();
        scan.nextLine();

    }
    // setup for recursive calls
   private void routesBelow(int ver, double maxPrice)
   {
     boolean[] alreadyUsed = new boolean[G.v + 1];
     alreadyUsed[ver] = true;
     WeightedDirectedEdge[] edgeTo = new WeightedDirectedEdge[G.v + 1];
     
     // begin recursion
     routesRecurse(ver, edgeTo, 0, maxPrice, alreadyUsed);
     alreadyUsed[ver] = false;
   }

   // recursive pruning method for finding combinations of edges
  private void routesRecurse(int ver,WeightedDirectedEdge[] edgeTo, double price,
      double priceLimit, boolean[] used)
    {
	  for (WeightedDirectedEdge e : G.adj(ver)) 
	  {

		  int w = e.w; // gets the to
        
		  if (!used[w] && price + e.price() <= priceLimit) 
		  { 							// if valid
			  edgeTo[w] = e;
			  used[w] = true;
			  System.out.println(cityNames[w]+" "+(price+e.price())+" "+cityNames[e.v]);
			  System.out.println();
			  
			  routesRecurse(w, edgeTo, price+e.price(), priceLimit, used);		//recursive call
			  edgeTo[w] = null;
			  used[w] = false;
		  }
	  }
    }
    
    private void addRoute()
    {
    	
    	for(int i = 0; i<cityNames.length;i++)
    	{
    		System.out.println(i+1 + ": " + cityNames[i]);
    	}
    	System.out.print("Please enter first city (1-" + cityNames.length + "):");	//user input for route
    	int source = Integer.parseInt(scan.nextLine());
    	System.out.print("Please enter second city (1-" + cityNames.length + "):");
    	int destination = Integer.parseInt(scan.nextLine());
    	
    	source--;
    	destination--;
    	
    	System.out.print("Enter the distance in miles: ");	//more user input
    	int miles = scan.nextInt();
    	
    	System.out.print("Enter the cost of the ticket: ");
    	double cost = scan.nextDouble();
    	scan.nextLine();

    	
    	G.addEdge(new WeightedDirectedEdge(source,destination,miles,cost));	//makes undirected route
    	G.addEdge(new WeightedDirectedEdge(destination,source,miles,cost));
    	
    	System.out.println();
    	System.out.println("Route added: "+cityNames[source] + " to "+cityNames[destination]+" ("+miles+",$"+cost+")");
    	System.out.println();		//print new route
    	
    	System.out.print("Please press ENTER to continue ...");
        scan.nextLine();
    	
    }
    
    //remove a route from the schedule
    private void removeRoute()
    {
    	for(int i = 0; i<cityNames.length;i++)
    	{
    		System.out.println(i+1 + ": " + cityNames[i]);
    	}
    	System.out.print("Please enter first city (1-" + cityNames.length + "):");	//which route to remove from user
    	int source = Integer.parseInt(scan.nextLine());
    	System.out.print("Please enter second city (1-" + cityNames.length + "):");
    	int destination = Integer.parseInt(scan.nextLine());
    	
    	source--;
    	destination--;
    	LinkedList<WeightedDirectedEdge> temp = new LinkedList<WeightedDirectedEdge>();
    	for(WeightedDirectedEdge e : G.adj(source))	//loop through all entries
    	{
    		if(e.to()!=destination)
    		{
    			temp.add(e);
    		}
    			
    	}
    	G.adj[source] = temp;
    	
    	temp = new LinkedList<WeightedDirectedEdge>();
    	for(WeightedDirectedEdge e : G.adj(destination))
    	{
    		if(e.to()!=source)
    		{
    			temp.add(e);
    		}
    	}
    	G.adj[destination]=temp;
    }
    
    

  /**
  *  The <tt>Digraph</tt> class represents an directed graph of vertices
  *  named 0 through v-1. It supports the following operations: add an edge to
  *  the graph, iterate over all of edges leaving a vertex.Self-loops are
  *  permitted.
  */
  private class Digraph {
    private final int v;
    private int e;
    private LinkedList<WeightedDirectedEdge>[] adj;
    private boolean[] marked;  // marked[v] = is there an s-v path
    private int[] edgeTo;      // edgeTo[v] = previous edge on shortest s-v path
    private int[] distTo;      // distTo[v] = number of edges shortest s-v path


    /**
    * Create an empty digraph with v vertices.
    */
    public Digraph(int v) {
      if (v < 0) throw new RuntimeException("Number of vertices must be nonnegative");
      this.v = v;
      this.e = 0;
      @SuppressWarnings("unchecked")
      LinkedList<WeightedDirectedEdge>[] temp =
      (LinkedList<WeightedDirectedEdge>[]) new LinkedList[v];
      adj = temp;
      for (int i = 0; i < v; i++)
        adj[i] = new LinkedList<WeightedDirectedEdge>();
    }

    /**
    * Add the edge e to this digraph.
    */
    public void addEdge(WeightedDirectedEdge edge) {
      int from = edge.from();
      adj[from].add(edge);
      
      e++;
    }


    /**
    * Return the edges leaving vertex v as an Iterable.
    * To iterate over the edges leaving vertex v, use foreach notation:
    * <tt>for (WeightedDirectedEdge e : graph.adj(v))</tt>.
    */
    public Iterable<WeightedDirectedEdge> adj(int v) {
      return adj[v];
    }

    public void bfs(int source) {
      marked = new boolean[this.v];
      distTo = new int[this.e];
      edgeTo = new int[this.v];

      Queue<Integer> q = new LinkedList<Integer>();
      for (int i = 0; i < v; i++){
        distTo[i] = INFINITY;
        marked[i] = false;
      }
      distTo[source] = 0;
      marked[source] = true;
      q.add(source);

      while (!q.isEmpty()) {
        int v = q.remove();
        for (WeightedDirectedEdge w : adj(v)) {
          if (!marked[w.to()]) {
            edgeTo[w.to()] = v;
            distTo[w.to()] = distTo[v] + 1;
            marked[w.to()] = true;
            q.add(w.to());
          }
        }
      }
    }
    
    //dijkstras for distance (price) for each edge
    public void dijkstrasPrice(int source, int destination) {
        marked = new boolean[this.v];
        distTo = new int[this.v];
        edgeTo = new int[this.v];


        for (int i = 0; i < v; i++){
          distTo[i] = INFINITY;
          marked[i] = false;
        }
        distTo[source] = 0;
        marked[source] = true;
        int nMarked = 1;

        int current = source;
        while (nMarked < this.v) {
          for (WeightedDirectedEdge w : adj(current)) {
            if (distTo[current]+w.price() < distTo[w.to()]) {
              edgeTo[w.to()] = current;
              distTo[w.to()] = (int) (distTo[current]+w.price());
            }
          }
          //Find the vertex with minimim path distance
          //This can be done more effiently using a priority queue!
          int min = INFINITY;
          current = -1;

          for(int i=0; i<distTo.length; i++){
            if(marked[i])
              continue;
            if(distTo[i] < min){
              min = distTo[i];
              current = i;
            }
          }
          if(current != -1){
            marked[current] = true;
            nMarked++;
          } else //graph is disconnected
            break;
        }
      }
    
    //dijkstras for distance (weight) of edges
    public void dijkstrasDistance(int source, int destination) {
        marked = new boolean[this.v];
        distTo = new int[this.v];
        edgeTo = new int[this.v];


        for (int i = 0; i < v; i++){
          distTo[i] = INFINITY;
          marked[i] = false;
        }
        distTo[source] = 0;
        marked[source] = true;
        int nMarked = 1;

        int current = source;
        while (nMarked < this.v) {
          for (WeightedDirectedEdge w : adj(current)) {
            if (distTo[current]+w.weight() < distTo[w.to()]) {
              edgeTo[w.to()] = current;
              distTo[w.to()] = (distTo[current]+w.weight());
            }
          }
          //Find the vertex with minimim path distance
          //This can be done more effiently using a priority queue!
          int min = INFINITY;
          current = -1;

          for(int i=0; i<distTo.length; i++){
            if(marked[i])
              continue;
            if(distTo[i] < min){
              min = distTo[i];
              current = i;
            }
          }
          if(current != -1){
            marked[current] = true;
            nMarked++;
          } else //graph is disconnected
            break;
        }
      }
    
    
    }
  

  /**
  *  The <tt>WeightedDirectedEdge</tt> class represents a weighted edge in an directed graph.
  */

  private class WeightedDirectedEdge {	//price variable added to lab code
    private final int v;
    private final int w;
    private int weight;
    private double price;
    /**
    * Create a directed edge from v to w with given weight.
    */
    public WeightedDirectedEdge(int v, int w, int weight, double price) {
      this.v = v;
      this.w = w;
      this.weight = weight;
      this.price = price;
    }

    public int from(){
      return v;
    }

    public int to(){
      return w;
    }

    public int weight(){
      return weight;
    }
    
    public double price()
    {
    	return price;
    }
  }
}