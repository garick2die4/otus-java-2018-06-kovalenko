package l16.dbserver.datasets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
