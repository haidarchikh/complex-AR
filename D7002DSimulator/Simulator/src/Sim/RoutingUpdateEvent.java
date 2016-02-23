package Sim;

public class RoutingUpdateEvent implements Event {
	private Node _oldNode;
	private Node _newNode;
	private Link _newLink;
	private int _newInterface;
	
	public RoutingUpdateEvent(Node oldNode , Node newNode){
		_oldNode = oldNode;
		_newNode = newNode;
	}
	public RoutingUpdateEvent(Node oldNode ,Node newNode , Link newLink ,int newInterface){
		_oldNode = oldNode;
		_newNode = newNode;
		_newLink = newLink;
		_newInterface = newInterface;
	}
	
	public void entering(SimEnt locale){}
	
	public Node oldNode() {
		return _oldNode;
	}
	public Node newNode() {
		return _newNode;
	}
	public Link newLink() {
		return _newLink;
	}
	public int newInterface() {
		return _newInterface;
	}
}