public class Test{
	public void method() {
		int index = 0;
		for(int i=0;i<100;i++) {
			while(i<50) {
				System.out.println("hello");
				if(i == 25) {
					System.out.println("world");
				}
			}
		}
		index ++;
		System.out.println(index+"");
		if(index == 1) {
			System.out.println("hello 3");
		}
	}
}