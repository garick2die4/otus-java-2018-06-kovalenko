package l16.dbserver.app;

import org.hibernate.Session;

import l16.dbserver.datasets.UserDataSet;

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

    public int count()
    {
    	return ((Long)session.createQuery("select count(*) from UserDataSet").uniqueResult()).intValue();
    }
}
