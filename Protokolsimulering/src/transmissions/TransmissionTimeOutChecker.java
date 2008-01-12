package transmissions;

public class TransmissionTimeOutChecker {
	private int[] time;
	private Transmission[] transmissions;
	private int size;
	
	public TransmissionTimeOutChecker() {
		resize(10);
	}

	
	private void resize(int newSize) {
		Transmission[] tempTrans = new Transmission[newSize];
		int[] tempTime = new int[newSize];
		
		if(transmissions != null) {
			int oldLength = size;
			if(oldLength > 0) {
				int max = newSize;
				if(oldLength < newSize) {
					max = oldLength;
					size = max;
				} 
				for(int i = 0; i < max ; i++) {
					tempTrans[i] = transmissions[i];
					tempTime[i] = time[i];
				}
			}
		}

		transmissions = tempTrans;
		time = tempTime;
	}
	
	public Transmission[] getAll(int value) {
		Transmission[] list = new Transmission[size];
		Transmission[] toReturn;
		int used = 0;
		for(int i = 0 ; i < size ; i++) {
			if(time[i] >= value) {
				list[used++] = transmissions[i];
			}
		}
		toReturn = new Transmission[used];
		for(int i = 0 ; i < used ; i++) {
			toReturn[i] = list[i];
		}
		return toReturn;
	}
	
	public boolean add(Transmission msg) {
		int id = msg.getReceiver();
		for(int i = 0 ; i < size ; i++) {
			if(transmissions[i].getReceiver() == id) {
				return false;
			}
		}
		transmissions[++size] = msg;
		time[size] = 0;
		if(size == transmissions.length) {
			resize(size + 10);
		}
		return true;
	}
	
	public void step() {
		for(int i = 0 ; i < size ; i++) {
			++time[i];
		}
	}
	
	public Transmission get(int id) {
		int index = getIndexOf(id);
		if(id > -1) {
			return transmissions[index];
		}
		return null;
	}
	
	public int getIndexOf(int id) {
		for(int i = 0 ; i < size ; i++) {
			if(transmissions[i].getReceiver() == id) {
				return i;
			}
		} 
		return -1;
	}
	
	public boolean remove(int id) {
		int index = getIndexOf(id);
		if(index < 0) {
			return false;
		}
		for(int i = index +1 ; i < size ; i++) {
			transmissions[i-1] = transmissions[i];
			time[i-1] = time[i];
		}
		transmissions[size] = null;
		time[size] = 0;
		--size;
		if(size > transmissions.length + 10) {
			resize(transmissions.length + 10);
		}
		return true;
	}
}
