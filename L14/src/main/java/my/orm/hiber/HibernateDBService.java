package my.orm.hiber;

import java.util.function.Consumer;
import java.util.function.Function;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.stereotype.Service;

import my.orm.datasets.AddressDataSet;
import my.orm.datasets.PhoneDataSet;
import my.orm.datasets.UserDataSet;
import my.orm.dbservice.DBServiceException;
import my.orm.dbservice.IDBService;

@Service
public class HibernateDBService implements IDBService
{
	private final SessionFactory sessionFactory;
	
	public HibernateDBService(String dbName, String userName, String password)
	{
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(UserDataSet.class);
        configuration.addAnnotatedClass(AddressDataSet.class);
        configuration.addAnnotatedClass(PhoneDataSet.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/" + dbName);
        configuration.setProperty("hibernate.connection.username", userName);
        configuration.setProperty("hibernate.connection.password", password);
        //configuration.setProperty("hibernate.show_sql", "true");
        //configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.connection.useSSL", "false");
        configuration.setProperty("hibernate.connection.allowPublicKeyRetrieval", "true");
        configuration.setProperty("hibernate.connection.serverTimezone", "UTC");
        configuration.setProperty("hibernate.enable_lazy_load_no_trans", "true");

        sessionFactory = createSessionFactory(configuration);	
	}
	
	@Override
	public void prepare() throws DBServiceException
	{
	}

	@Override
	public String getMetaData()
	{
        return runInSessionFunction(session ->
        {
            return session.getTransaction().getStatus().name();
        });
	}

	@Override
	public void save(UserDataSet user) throws DBServiceException
	{
		runInSessionConsumer(session ->
		{
        	HibernateUserDataSetDAO dao = new HibernateUserDataSetDAO(session);
            dao.save(user);
        });
	}

	@Override
	public UserDataSet load(long id) throws DBServiceException
	{
        return runInSessionFunction(session ->
        {
        	HibernateUserDataSetDAO dao = new HibernateUserDataSetDAO(session);
            UserDataSet user = dao.load(id);
            return user;
        });
	}

	@Override
	public int usersCount() throws DBServiceException
	{
        return runInSessionFunction(session ->
        {
        	HibernateUserDataSetDAO dao = new HibernateUserDataSetDAO(session);
            int count = dao.count();
            return count;
        });
	}
	
	@Override
	public void shutdown() throws DBServiceException
	{
	}

	@Override
	public void close() throws Exception
	{
		sessionFactory.close();
	}
	
    private static SessionFactory createSessionFactory(Configuration configuration)
    {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
	
    private <R> R runInSessionFunction(Function<Session, R> function)
    {
        try (Session session = sessionFactory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }
    
    private void runInSessionConsumer(Consumer<Session> function)
    {
        try (Session session = sessionFactory.openSession())
        {
            Transaction transaction = session.beginTransaction();
            function.accept(session);
            transaction.commit();
        }
    }
}
