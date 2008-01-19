package transmissions;

public class NetworkData extends Data {

	private int distance;
	private int link;
	
	public NetworkData(int distance, int link) {
		this.distance = distance;
		this.link = link;
		this.dataType = Data.TYPE_NETWORK;
	}
	
	public int getLink(){
		return link;
	}
	
	public int getDistance() {
		return distance;
	}
	
	public NetworkData nextGenerationData(int newLink) {
		return new NetworkData(this.distance+1, newLink);
	}
	
	@Override
	public String toString() {
		return "Net, link: " + link + ", dist: " + distance;
	}
}
