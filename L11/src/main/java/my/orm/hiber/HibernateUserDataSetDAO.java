package my.orm.hiber;

import org.hibernate.Session;

import my.orm.datasets.UserDataSet;

public class HibernateUserDataSetDAO
{
	private Session session;
	
    public HibernateUserDataSetDAO(Session session)
    {
        this.session = session;
    }

    public void save(UserDataSet dataSet)
    {
        session.save(dataSet);
    }

    public UserDataSet load(long id)
    {
        return session.load(UserDataSet.class, id);
    }

}
