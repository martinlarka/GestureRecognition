package martinlarka;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.leapmotion.leap.Vector;

import net.sf.javaml.core.Instance;

public class VectorInstance implements Instance {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8351686247696405523L;
	private Vector vec;
	private String value;
	
	public VectorInstance(Vector vec) {
		super();
		this.vec = vec;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Double get(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double put(Integer key, Double value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAll(Map<? extends Integer, ? extends Double> m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<Double> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<java.util.Map.Entry<Integer, Double>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Double> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instance add(Instance arg0) {
		return null;
	}

	@Override
	public Instance add(double arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object classValue() {
		return (Object)this.value;
	}

	@Override
	public Instance copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instance divide(double arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instance divide(Instance arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SortedSet<Integer> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instance minus(Instance arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instance minus(double arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instance multiply(double arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instance multiply(Instance arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int noAttributes() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeAttribute(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAttributes(Set<Integer> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setClassValue(Object arg0) {
		String val = (String)arg0;
		this.value = val;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Instance sqrt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double value(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
