package fr.eurecom.mobserv.arianna.mapobject;

import com.google.gson.annotations.Expose;

public class JavascriptState {
	@Expose
	private String curr;
	@Expose
	private String succ;
	@Expose
	private GPoint currNode;
	
	public JavascriptState(){
	}
	
	public JavascriptState(String curr, String succ, GPoint currNode) {
		this.curr = curr;
		this.succ = succ;
		this.currNode = currNode;
	}
	/**
	 * @return the curr
	 */
	public String getCurr() {
		return curr;
	}
	/**
	 * @param curr the curr to set
	 */
	public void setCurr(String curr) {
		this.curr = curr;
	}
	/**
	 * @return the succ
	 */
	public String getSucc() {
		return succ;
	}
	/**
	 * @param succ the succ to set
	 */
	public void setSucc(String succ) {
		this.succ = succ;
	}
	/**
	 * @return the currNode
	 */
	public GPoint getCurrNode() {
		return currNode;
	}
	/**
	 * @param currNode the currNode to set
	 */
	public void setCurrNode(GPoint currNode) {
		this.currNode = currNode;
	}
	
	
}
