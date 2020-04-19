package pimpmyoauth.beans;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pimpmyoauth.core.Core;
import pimpmyoauth.utils.Fonctions;

public class Servlet {
	
	String name;
	String endPoint;
	Integer nbAccess=0;
	Boolean available;
	Long responseTime=-1l;
	Long startTime=0l;
	
	
	
	Map<String,Integer> accessByOrigin=new HashMap<String,Integer>();
	
	public Servlet()
	{
		startTime=new Date().getTime();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	public Integer getNbAccess() {
		return nbAccess;
	}
	public void setNbAccess(Integer nbAccess) {
		this.nbAccess = nbAccess;
	}
	public Boolean getAvailable() {
		return available;
	}
	public void setAvailable(Boolean available) {
		this.available = available;
	}
	public Map<String, Integer> getAccessByOrigin() {
		return accessByOrigin;
	}
	public void setAccessByOrigin(Map<String, Integer> accessByOrigin) {
		this.accessByOrigin = accessByOrigin;
	}
	public Long getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Long responseTime) {
		this.responseTime = responseTime;
	}
	
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public void check()
	{
		Long start=new Date().getTime();
		Fonctions.trace("DBG", "Probing " + Core.getInstance().getUrlAccess() +  getEndPoint() +"?stats=true", "Servlet");
		setAvailable(Fonctions.isUrlAvailable(Core.getInstance().getUrlAccess() +  getEndPoint()+"?stats=true"));
		if ( getAvailable() )
		{
			Long stop=new Date().getTime();
			setResponseTime(stop - start);
		}else
		{
		    setResponseTime(-1l);
		}
		Fonctions.trace("DBG", getEndPoint() + " responseTime " + getResponseTime(), "Servlet");
	}
	
	public synchronized void addAccess()
	{
		nbAccess++;
	}
	
	public synchronized void addAccessForOrigin(String origin)
	{
		Integer access=0;
		if ( accessByOrigin.containsKey(origin))
		{
			access=accessByOrigin.get(origin);
		}
		access++;
		accessByOrigin.put(origin, access);
	}
}
