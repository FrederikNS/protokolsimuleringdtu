package message;

public class TransmissionTimeOutChecker {
	private int[][] transmissions;
	private int size;
	
	public TransmissionTimeOutChecker() {
		resize(10);
	}

	
	private void resize(int newSize) {
		int[][] temp = new int[2][newSize];
		
		if(transmissions != null) {
			int oldLength = size;
			if(oldLength > 0) {
				int max = newSize;
				if(oldLength < newSize) {
					max = oldLength;
					size = max;
				} 
				for(int i = 0; i < max ; i++) {
					temp[i] = transmissions[i];
				}
			}
		}

		transmissions = temp;
	}
	
	public int[] getAll(int value) {
		int[] list = new int[size];
		int[] toReturn;
		int used = 0;
		for(int i = 0 ; i < size ; i++) {
			if(transmissions[1][i] >= value) {
				list[used++] = transmissions[0][i];
			}
		}
		toReturn = new int[used];
		for(int i = 0 ; i < used ; i++) {
			toReturn[i] = list[i];
		}
		return toReturn;
	}
	
	public boolean add(int id) {
		for(int i = 0 ; i < size ; i++) {
			if(transmissions[0][i] == id) {
				return false;
			}
		}
		transmissions[0][++size] = id;
		if(size == transmissions[0].length) {
			resize(size + 10);
		}
		return true;
	}
	
	public void step() {
		for(int i = 0 ; i < size ; i++) {
			++transmissions[1][i];
		}
	}
	
	public int get(int id) {
		int index = getIndexOf(id);
		if(id > -1) {
			return transmissions[1][index];
		}
		return 0;
	}
	
	public int getIndexOf(int id) {
		for(int i = 0 ; i < size ; i++) {
			if(transmissions[0][i] == id) {
				return i;
			}
		} 
		return -1;
	}
	
	public int remove(int id) {
		int index = getIndexOf(id);
		if(index < 0) {
			return 0;
		}
		int toReturn = transmissions[1][index];
		for(int i = index +1 ; i < size ; i++) {
			transmissions[0][i-1] = transmissions[0][i];
			transmissions[1][i-1] = transmissions[1][i];
		}
		--size;
		transmissions[0][index] = 0;
		transmissions[1][index] = 0;
		if(size > transmissions[0].length + 10) {
			resize(transmissions[0].length + 10);
		}
		return toReturn;
	}
}
