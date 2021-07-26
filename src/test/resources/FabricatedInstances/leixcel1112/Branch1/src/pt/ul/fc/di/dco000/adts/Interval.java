package pt.ul.fc.di.dco000.adts;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class Interval<T extends Successorable<T>> implements IInterval<T> {

		private T min, max;
		public Interval(T min, T max){
			this.min = min;
			this.max = max;
		}
		public T min() {
			return min;
		}
		public T max() {
			return max;
		}
		public boolean intersects(IInterval<T> other) {
			return (min.compareTo(other.max())<=0 && max.compareTo(other.min())>=0)
				|| (other.min().compareTo(max)<=0 && other.max().compareTo(min)>=0);
		}
		SortedSet<T> elements() {
			SortedSet<T> result = new TreeSet<T>();
			T el=min;
			while (el.compareTo(max)<=0){
				result.add(el);
				el = el.suc();
			}
			return result;
		}
		public String toString(){
			return "["+min+","+max+"]";
		}
		@Override
		public Iterator<T> iterator() {
			return elements().iterator();
		}
		
}
