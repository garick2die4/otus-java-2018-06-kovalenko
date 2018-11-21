package my.orm.datasets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import my.orm.DataSet;

@Entity
@Table(name = "address")
public class AddressDataSet extends DataSet
{
	@Column(name = "street")
	private String street;
	
	public AddressDataSet()
	{
		this("");
	}
	
	public AddressDataSet(String street)
	{
		this.street = street;
	}
	
	public String getStreet()
	{
		return street;
	}
}
