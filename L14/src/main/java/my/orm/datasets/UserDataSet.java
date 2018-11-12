package my.orm.datasets;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import my.orm.DataSet;

@Entity
@Table(name = "user")
public final class UserDataSet extends DataSet
{
	@Column(name = "name")
	private final String name;
	
	@Column(name = "age")
	private final short age;
	
	@OneToOne(cascade = CascadeType.ALL)
	private AddressDataSet address;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<PhoneDataSet> phones;
	
	public UserDataSet()
	{
		this("", (short)0, "", new ArrayList<>());
	}

	public UserDataSet(String name, short age, String address, List<PhoneDataSet> phones)
	{
		super(0);
		
		this.name = name;
		this.age = age;
		this.address = new AddressDataSet(address);
		this.phones = phones;
	}

	public String getName()
	{
		return name;
	}
	
	public short getAge()
	{
		return age;
	}

	public void setAddress(AddressDataSet address)
	{
		this.address = address;
	}
	
	public AddressDataSet getAddress()
	{
		return address;
	}

	public List<PhoneDataSet> getPhones()
	{
		return phones;		
	}
	
	@Override
	public String toString()
	{
		return String.format("User: id = %d, name = %s, age = %d, address = %s, phones = %s",
			getId(),
	    	name,
	    	age,
	    	address.getStreet(),
	    	phones.stream().map(p -> p.getNumber()).collect(Collectors.joining(",")));		
	}
}
