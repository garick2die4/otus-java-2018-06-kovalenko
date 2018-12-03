package l16.dbserver.datasets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "phone")
public class PhoneDataSet extends DataSet
{
	@Column(name = "number")
	private String number;
	
	public PhoneDataSet()
	{
		number = "";
	}

	public PhoneDataSet(String num)
	{
		this.number = num;
	}
	
	public String getNumber()
	{
		return number;
	}
}
